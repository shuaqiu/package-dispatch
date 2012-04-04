define([
        "dojo/_base/lang",
        "dojo/dom",
        "dijit/registry",
        "../resource",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/user",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, dom, registry, resource, DataSelectionDialog, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/user",
        listGrid : "user_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "user_editing_tab",
        editingForm : "user_editing_form",

        companyDialog : "user_editing_company_dialog",
        customerTypeRow : "user_editing_customerType_row",
        userTypeId : "user_new_type",

        showSelectionDialog : function() {
            var dialog = registry.byId(this.companyDialog);
            if (!dialog) {
                var doSelect = lang.hitch(this, this.doSelect);
                dialog = new DataSelectionDialog({
                    id : this.companyDialog,
                    storeTarget : 'web/company',
                    structure : [ {
                        name : "编码",
                        field : "code",
                        width : "150px"
                    }, {
                        name : "名称",
                        field : "name",
                        width : "200px"
                    }, {
                        name : "地址",
                        field : "address",
                        width : "250px"
                    } ],
                    onRowClick : function(item) {
                        doSelect(item);
                    }
                });
            }
            dialog.show();
        },

        doSelect : function(item) {
            var form = document.forms[this.editingForm];
            form["companyId"].value = item["id"];
            registry.byId(form["company"].id).set("value", item["name"]);
        },

        typeChanged : function() {
            var customerTypeRow = dom.byId(this.customerTypeRow);
            var val = registry.byId(this.userTypeId).get("value");
            if (val == 1) {
                customerTypeRow.style.display = "none";
            } else {
                customerTypeRow.style.display = "";
            }
        }
    });
});