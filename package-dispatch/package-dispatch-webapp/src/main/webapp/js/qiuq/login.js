define([
        "dijit/Dialog",
        "dijit/layout/ContentPane",
        "dijit/form/TextBox",
        "dijit/form/Button",
        "dojo/_base/Deferred",
        "dojo/_base/xhr",
        "dojo/i18n!./nls/login",
        "dojo/string",
        "dijit/registry",
        "./widget/MessageDialog" ], function(Dialog, ContentPane, TextBox, Button, Deferred, xhr, message, string,
        registry, MessageDialog) {

    var id = {
        dialogId : "login.dialog",
        dialogContent : "login.dialogContent",
        usercode : "login.usercode",
        password : "login.password",
        buttonPane : "login.buttonPane"
    };

    function doLogin() {
        var dialog = new Dialog({
            "id" : id.dialogId,
            // Dialog title
            "title" : message["title"],
            // Create Dialog content
            "content" : "<div id='" + id.dialogContent + "'></div>"
        });

        _createInput();

        var loginButton = _createButton();

        var deferred = new Deferred();
        loginButton.onClick = function() {
            _login().then(function(result) {
                if (result.ok) {
                    dialog.destroyRecursive();
                    deferred.resolve(result);
                } else {
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        };

        dialog.show();

        return deferred;
    }

    function _createInput() {
        new ContentPane({
            "content" : new TextBox({
                "id" : id.usercode,
                "placeHolder" : message["inputUsername"]
            }),
            "style" : {
                "padding" : "3px"
            }
        }).placeAt(id.dialogContent);

        var password = new TextBox({
            "id" : id.password,
            "placeHolder" : message["inputPassword"],
            "type" : "password"
        });
        new ContentPane({
            "content" : password,
            "style" : {
                "padding" : "3px"
            }
        }).placeAt(id.dialogContent);
    }

    function _createButton() {
        new ContentPane({
            "id" : id.buttonPane,
            "style" : {
                "padding" : "3px",
                "text-align" : "center"
            }
        }).placeAt(id.dialogContent);

        var loginButton = new Button({
            iconClass : "dijitIconKey",
            label : message["login"]
        }).placeAt(id.buttonPane);

        new Button({
            iconClass : "dijitIconClear",
            label : message["reset"],
            onClick : function() {
                _reset();
            }
        }).placeAt(id.buttonPane);

        return loginButton;
    }

    function _login() {
        var usercode = registry.byId(id.usercode).get("value");
        var password = registry.byId(id.password).get("value");
        if (string.trim(usercode) == "" || string.trim(password) == "") {
            MessageDialog.error(message["err.NULL"]);
            return new Deferred();
        }

        return xhr.post({
            "url" : "web/login/" + usercode,
            "content" : {
                "password" : password
            },
            "handleAs" : "json"
        });
    }

    function _reset() {
        registry.byId(id.usercode).reset();
        registry.byId(id.password).reset();
    }

    function isLogined() {
        var deferred = new Deferred();

        xhr.get({
            "url" : "web/login",
            "handleAs" : "json"
        }).then(function(json) {
            if (json.ok) {
                deferred.resolve();
            } else {
                deferred.reject();
            }
        });
        return deferred;
    }

    function doLogout() {
        xhr.del({
            "url" : "web/login",
            "handleAs" : "json"
        }).then(function(json) {
            doLogin();
        });
    }

    return {
        "doLogin" : doLogin,
        "isLogined" : isLogined,
        "doLogout" : doLogout
    };
});