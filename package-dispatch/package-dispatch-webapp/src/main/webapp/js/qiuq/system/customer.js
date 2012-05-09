define([
        "dojo/_base/lang",
        "dijit/registry",
        "./account",
        "../selection",
        "dojo/i18n!./nls/customer",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, registry, account, selection, message) {

    return lang.mixin({}, account, selection, {
        createTabName : message["create"],
        modifyTabName : message["modify"],

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
            registry.byId(form["address"].id).set("value", item["address"]);
        }
    });
});