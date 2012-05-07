define([
        "dojo/_base/lang",
        "dojo/dom",
        "dijit/registry",
        "../resource",
        "dojo/i18n!./nls/account",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, dom, registry, resource, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/customer",
        listGrid : "customer_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "customer_editing_tab",
        editingForm : "customer_editing_form",
        
        _getValidData : function() {
            var form = document.forms[this.editingForm];

            var dijitForm = registry.byNode(form);
            if (dijitForm.isValid() == false) {
                MessageDialog.error(message["err.INVALID"]);
                return null;
            }

            xhr.get({
                url : this.resourceUrl + "/" + form["alias"].value,
                content : {
                    id : form["id"].value
                },
                sync : true,
                handleAs : "json"
            }).then(function(result) {
                if (result.count > 0) {
                    // some other user has the same alias
                    MessageDialog.error(message["err.DUPLICATE_ALIAS"]);
                    return null;
                }
            });

            return domform.toJson(form);
        }
    });
});