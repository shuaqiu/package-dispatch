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
    });
});