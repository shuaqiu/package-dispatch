define([
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dojo/dom",
        "dojo/dom-form",
        "dijit/registry",
        "../resource",
        "../widget/DataSelectionDialog",
        "../widget/MessageDialog",
        "dojo/i18n!./nls/user",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, xhr, dom, domform, registry, resource, DataSelectionDialog,
        MessageDialog, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/user",
        listGrid : "user_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "user_editing_tab",
        editingForm : "user_editing_form",

        _checkAccount : function(isSync) {
            var form = document.forms[this.editingForm];

            var content = {};
            if (form["id"].value != "") {
                content["id"] = form["id"].value;
            }

            return xhr.get({
                url : this.resourceUrl + "/check/" + form["loginAccount"].value,
                content : content,
                sync : true,
                handleAs : "json"
            });
        },

        _customErrorCallback : function(result) {
            if (result.errCode == "DUPLICATE") {
                MessageDialog.error(message["err.DUPLICATE"]);
                return false;
            }
            return true;
        },

        _initForm : function(item) {
            dom.byId("user_editing_password_row").style.display = "none";
            var password = registry.byId("user_editing_password");
            password.set("value", "password");
            password.destroyRecursive();

            var form = document.forms[this.editingForm];

            for ( var p in item) {
                if (p == "roleId") {
                    registry.byId("user_editing_role").set("value", item[p]);
                    return;
                }

                var elem = form[p];
                if (elem) {
                    if (elem.id) {
                        registry.byId(elem.id).set("value", item[p]);
                    } else {
                        elem.value = item[p];
                    }
                }
            }
        }
    });
});