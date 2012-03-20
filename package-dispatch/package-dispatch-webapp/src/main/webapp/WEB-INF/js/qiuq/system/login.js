define([ "dijit/Dialog", "dijit/layout/ContentPane", "dijit/form/TextBox", "dijit/form/Button", "dojo/dom",
        "dojo/_base/Deferred", "dojo/_base/xhr", "dojo/i18n!./nls/login", "dojo/string", "../ErrCode" ], function(
        Dialog, ContentPane, TextBox, Button, dom, Deferred, xhr, message, string, ErrCode) {

    var id = {
        dialogContent : "dialogContent",
        usercode : "usercode",
        password : "password",
        buttonPane : "buttonPane"
    };

    function doLogin() {
        var dialog = new Dialog({
            "id" : "dialog",
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
                    dialog.destroy();
                    deferred.resolve();
                } else {
                    showErr(result.errCode);
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

    function showErr(errCode) {
        new Dialog({
            "title" : message["title"],
            "content" : message["err." + errCode]
        }).show();
    }

    function _login() {
        var usercode = dom.byId(id.usercode).value;
        var password = dom.byId(id.password).value;
        if (string.trim(usercode) == "" || string.trim(password) == "") {
            showErr(ErrCode.NULL);
            return new Deferred();
        }

        return xhr.post({
            "url" : "web/login",
            "content" : {
                "usercode" : usercode,
                "password" : password
            },
            "handleAs" : "json"
        });
    }

    function _reset() {
        dom.byId(id.usercode).value = "";
        dom.byId(id.password).value = "";
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

    return {
        "doLogin" : doLogin,
        "isLogined" : isLogined
    };
});