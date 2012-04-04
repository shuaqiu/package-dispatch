define([
        "dojo/_base/lang",
        "dojo/dom",
        "dijit/registry",
        "../resource",
        "../selection",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/user",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, dom, registry, resource, selection, DataSelectionDialog, message) {

    return lang.mixin({}, resource, selection, {
        resourceUrl : "web/user",
        listGrid : "user_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "user_editing_tab",
        editingForm : "user_editing_form",

        customerTypeRow : "user_editing_customerType_row",
        userTypeId : "user_editing_type",

        selectionDialog : "user_editing_company_dialog",
        selectionStoreTarget : "web/company",
        selectionStructure : [ {
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