define([
        "dojo/_base/lang",
        "dojo/dom",
        "dijit/registry",
        "../resource",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/role",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, dom, registry, resource, DataSelectionDialog, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/role",
        listGrid : "role_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "role_editing_tab",
        editingForm : "role_editing_form"
    });
});