define([
        "dojo/string",
        "dojo/json",
        "dojo/_base/xhr",
        "dijit/registry",
        "dijit/layout/ContentPane",
        "dijit/form/TextBox",
        "dijit/form/Button",
        "../widget/DestroyWhenCloseDialog",
        "../widget/MessageDialog",
        "dojo/i18n!./nls/passwordmodifier" ], function(string, json, xhr, registry, ContentPane, TextBox, Button,
        DestroyWhenCloseDialog, MessageDialog, message) {

    var id = {
        dialogId : "modifier_dialog",
        dialogContent : "modifier_dialogContent",
        buttonPane : "modifier_buttonPane",
        currentPassword : "modifier_currentPassword",
        newPassword : "modifier_newPassword",
        newPasswordConfirm : "modifier_newPasswordConfirm"
    };

    function show() {
        var dialog = new DestroyWhenCloseDialog({
            "id" : id.dialogId,
            "title" : message["title"],
            "content" : "<div id='" + id.dialogContent + "'></div>"
        });

        _createInput();
        _createButton();

        dialog.show();
    }

    function _createInput() {
        new ContentPane({
            "content" : new TextBox({
                "id" : id.currentPassword,
                "placeHolder" : message["currentPassword"],
                "type" : "password"
            }),
            "style" : {
                "padding" : "3px"
            }
        }).placeAt(id.dialogContent);

        new ContentPane({
            "content" : new TextBox({
                "id" : id.newPassword,
                "placeHolder" : message["newPassword"],
                "type" : "password"
            }),
            "style" : {
                "padding" : "3px"
            }
        }).placeAt(id.dialogContent);

        new ContentPane({
            "content" : new TextBox({
                "id" : id.newPasswordConfirm,
                "placeHolder" : message["newPasswordConfirm"],
                "type" : "password"
            }),
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

        new Button({
            label : message["modify"],
            onClick : function() {
                _modify();
            }
        }).placeAt(id.buttonPane);

        new Button({
            label : message["reset"],
            onClick : function() {
                _reset();
            }
        }).placeAt(id.buttonPane);
    }

    function _modify() {
        var currentPassword = registry.byId(id.currentPassword).get("value");
        var newPassword = registry.byId(id.newPassword).get("value");
        var newPasswordConfirm = registry.byId(id.newPasswordConfirm).get("value");
        if (string.trim(currentPassword) == "" || string.trim(newPassword) == ""
                || string.trim(newPasswordConfirm) == "") {
            MessageDialog.error(message["err.NULL"]);
            return;
        }
        if (newPassword != newPasswordConfirm) {
            MessageDialog.error(message["err.NOT_SAME"]);
            return;
        }

        xhr.put({
            "url" : "web/user/password",
            "putData" : json.stringify({
                "currentPassword" : currentPassword,
                "newPassword" : newPassword
            }),
            "handleAs" : "json",
            "contentType" : "application/json"
        }).then(function(result) {
            if (result.ok) {
                MessageDialog.alert(message["success"]);
                registry.byId(id.dialogId).destroyRecursive();
            } else {
                MessageDialog.error(message["err." + result.errCode]);
            }
        });
    }

    function _reset() {
        registry.byId(id.currentPassword).reset();
        registry.byId(id.newPassword).reset();
        registry.byId(id.newPasswordConfirm).reset();
    }

    return {
        "show" : show
    };
});