define([
        "dojo/_base/lang",
        "dojo/dom",
        "dijit/registry",
        "../resource",
        "../selection",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/customer",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, dom, registry, resource, selection, DataSelectionDialog, message) {

    return lang.mixin({}, resource, selection, {
        resourceUrl : "web/customer",
        listGrid : "customer_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "customer_editing_tab",
        editingForm : "customer_editing_form",

        selectionDialog : "customer_editing_company_dialog",
        selectionStoreTarget : "web/company",
        selectionStructure : [ {
            name : message["code"],
            field : "code",
            width : "150px"
        }, {
            name : message["name"],
            field : "name",
            width : "200px"
        }, {
            name : message["address"],
            field : "address",
            width : "250px"
        } ],

        doSelect : function(item) {
            var form = document.forms[this.editingForm];
            form["companyId"].value = item["id"];
            registry.byId(form["company"].id).set("value", item["name"]);
        }
    });
});