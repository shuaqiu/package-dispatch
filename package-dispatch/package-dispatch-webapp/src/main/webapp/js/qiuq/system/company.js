define([
        "dojo/_base/lang",
        "../resource",
        "dojo/i18n!./nls/company",
        "../widget/ResourceGrid",
        "dojox/grid/EnhancedGrid",
        "dojox/grid/enhanced/plugins/Pagination" ], function(lang, resource, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/company",
        listGrid : "company_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "company_editing_tab",
        editingForm : "company_editing_form"
    });

});