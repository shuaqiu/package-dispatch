define([ "dojo/_base/lang", "../resource", "dojo/i18n!./nls/receivercompany", "../widget/ResourceGrid" ], function(
        lang, resource, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/receivercompany",
        listGrid : "receivercompany_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "receivercompany_editing_tab",
        editingForm : "receivercompany_editing_form"
    });
});