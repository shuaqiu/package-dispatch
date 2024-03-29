define([
        "require",
        "dojo/parser",
        "dojo/dom",
        "dojo/string",
        "dojo/_base/Deferred",
        "dojo/_base/xhr",
        "dijit/registry",
        "dijit/Dialog",
        "dijit/layout/ContentPane",
        "dijit/form/TextBox",
        "dijit/form/Button",
        "./widget/MessageDialog",
        "./order/alarm",
        "./monitor",
        "dojo/i18n!./nls/login" ], function(require, parser, dom, string, Deferred, xhr, registry, Dialog, ContentPane,
        TextBox, Button, MessageDialog, alarm, monitor, message) {

    var id = {
        dialogId : "login_dialog",
        dialogContent : "login_dialogContent",
        usercode : "login_usercode",
        password : "login_password",
        buttonPane : "login_buttonPane"
    };

    function doLogin() {
        var dialog = new Dialog({
            id : id.dialogId,
            // Dialog title
            title : message["title"],
            // Create Dialog content
            content : "<div id='" + id.dialogContent + "'></div>"
        });

        var deferred = new Deferred();

        function lg() {
            _login().then(function(result) {
                if (result.ok) {
                    dialog.destroyRecursive();
                    deferred.resolve(result);
                } else {
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        }

        _createInput(lg);
        var loginButton = _createButton();

        loginButton.onClick = function() {
            lg();
        };

        dialog.show();

        try {
            var closeButton = dom.byId(id.dialogId).children[0].children[1];
            closeButton.style.width = "0";
            closeButton.style.height = "0";
        } catch (e) {
        }

        return deferred;
    }

    function _createInput(onPasswordEnter) {
        new ContentPane({
            content : new TextBox({
                id : id.usercode,
                placeHolder : message["inputUsername"]
            }),
            style : {
                "padding" : "3px"
            }
        }).placeAt(id.dialogContent);

        var password = new TextBox({
            id : id.password,
            placeHolder : message["inputPassword"],
            type : "password",
            onKeyPress : function(evt) {
                if (evt.keyCode == 13) {
                    // enter
                    onPasswordEnter();
                }
            }
        });
        new ContentPane({
            content : password,
            style : {
                "padding" : "3px"
            }
        }).placeAt(id.dialogContent);
    }

    function _createButton() {
        new ContentPane({
            id : id.buttonPane,
            className : "loginDialogButtonBar"
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
            url : "web/login/" + usercode,
            content : {
                password : password
            },
            handleAs : "json"
        });
    }

    function _reset() {
        registry.byId(id.usercode).reset();
        registry.byId(id.password).reset();
    }

    function isLogined() {
        var deferred = new Deferred();

        xhr.get({
            url : "web/login",
            content : {
                // add a time parameter to prevent cache
                t : new Date().getTime()
            },
            handleAs : "json"
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
            url : "web/login",
            handleAs : "json"
        }).then(function(json) {
            reLogin();
        });
    }

    function reLogin() {
        // destroy all current widget first
        alarm.destroy();
        monitor.stopMonitor();
        
        destroyAll("");
        tryLogin();
    }

    function tryLogin() {
        var deferred = new Deferred();
        isLogined().then(function() {
            deferred.resolve();
        }, function() {
            doLogin().then(function(json) {
                deferred.resolve();
            });
        });
        return deferred.then(loadBody);
    }

    function loadBody() {
        require([
                "dijit/layout/BorderContainer",
                "dijit/layout/TabContainer",
                "dijit/MenuBar",
                "dijit/MenuBarItem",
                "dijit/PopupMenuBarItem",
                "dijit/DropDownMenu",
                "dijit/MenuItem",
                "dijit/form/TextBox",
                "qiuq/order/order" ], function() {
            xhr.get({
                url : "web/index/body",
                content : {
                    // add a time parameter to prevent cache
                    t : new Date().getTime()
                }
            }).then(function(content) {
                // destroy all current widget first
                destroyAll(content);
                parser.parse(document.body);

                setTimeout(function() {
                    alarm.startCheck();
                    monitor.doMonitor();
                }, 5000);
            });
        });
    }

    function destroyAll(content) {
        var widgets = registry.findWidgets(document.body);
        for ( var i = 0; i < widgets.length; i++) {
            widgets[i].destroyRecursive();
        }
        dom.byId(document.body).innerHTML = content;
    }

    return {
        "tryLogin" : tryLogin,
        "isLogined" : isLogined,
        "doLogout" : doLogout,
        "reLogin" : reLogin
    };
});