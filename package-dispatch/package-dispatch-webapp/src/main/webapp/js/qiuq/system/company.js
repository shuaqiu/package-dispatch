define([
        "dojo/_base/lang",
        "../resource",
        "dojo/i18n!./nls/company",
        "../widget/ResourceList",
        "dojox/grid/EnhancedGrid",
        "dojox/grid/enhanced/plugins/Pagination" ], function(lang, resource, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/company",
        listGrid : "company_list_grid",

        creationTabName : message["creation"],
        creationTab : "company_creation_tab",
        creationForm : "company_creation_form"
    });

});