define([ "dojo/_base/lang", "../resource", "dojo/i18n!./nls/receivercompany" ], function(lang, resource, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/receivercompany",
        listGrid : "receivercompany_list_grid",

        creationTabName : message["creation"],
        creationTab : "receivercompany_creation_tab",
        creationForm : "receivercompany_creation"
    });
});