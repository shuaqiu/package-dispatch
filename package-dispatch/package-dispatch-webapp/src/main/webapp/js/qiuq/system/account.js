define([
        "dojo/_base/lang",
        "dojo/dom",
        "dijit/registry",
        "../resource",
        "../widget/MessageDialog",
        "dojo/i18n!./nls/account",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, dom, registry, resource, MessageDialog, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/customer",
        listGrid : "customer_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "customer_editing_tab",
        editingForm : "customer_editing_form",

        _customErrorCallback : function(result) {
            if (result.errCode == "DUPLICATE") {
                MessageDialog.error(message["err.DUPLICATE"]);
                return false;
            }
            return true;
        },

        _initForm : function(item) {
            dom.byId("customer_editing_password_row").style.display = "none";
            var password = registry.byId("customer_editing_password");
            password.set("value", "password");
            password.destroyRecursive();

            var form = document.forms[this.editingForm];

            for ( var p in item) {
                if (p == "customerType") {
                    var customerType = registry.byId("customer_editing_customerType");
                    if (customerType != null) {
                        customerType.set("value", item[p]);
                    }
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