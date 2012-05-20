define([
        "dojo/_base/lang",
        "dijit/registry",
        "../resource",
        "../suggest",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/receiver",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, registry, resource, suggest, DataSelectionDialog, message) {

    return lang.mixin({}, resource, suggest, {
        resourceUrl : "web/receiver",
        listGrid : "receiver_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "receiver_editing_tab",
        editingForm : "receiver_editing_form",

        selectionDialog : "receiver_editing_company_dialog",
        selectionStoreTarget : "web/company",
        selectionStructure : [ {
            name : message["code"],
            field : "code",
            width : "150px"
        }, {
            name : message["name"],
            field : "name",
            width : "150px"
        }, {
            name : message["address"],
            field : "address",
            width : "250px"
        } ],

        doSelect : function(item) {
            var form = document.forms[this.editingForm];
            form["userCompanyId"].value = item["id"];
            registry.byId(form["userCompany"].id).set("value", item["name"]);
        },
    });
});