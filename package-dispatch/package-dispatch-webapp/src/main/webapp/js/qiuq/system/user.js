define([
        "dojo/_base/lang",
        "dojo/dom",
        "dijit/registry",
        "../resource",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/user",
        "dijit/form/Select" ], function(lang, dom, registry, resource, DataSelectionDialog, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/user",
        listGrid : "user_list_grid",

        creationTabName : message["creation"],
        creationTab : "user_creation_tab",
        creationForm : "user_creation_form",

        companyDialog : "user_creation_company_dialog",
        customerTypeRow : "user_creation_customerType_row",
        userTypeId : "user_new_type",

        showCompany : function() {
            var dialog = registry.byId(this.companyDialog);
            if (!dialog) {
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
                    onRowClick : lang.hitch(this, this.selectCompany)
                });
            }
            dialog.show();
        },

        selectCompany : function(item) {
            var form = document.forms[this.creationForm];
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