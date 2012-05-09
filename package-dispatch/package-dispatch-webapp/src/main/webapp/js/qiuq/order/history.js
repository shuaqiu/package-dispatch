define([ "dojo/_base/lang", "../resource", "dojo/date/locale", "../widget/ResourceGrid" ], function(lang, resource) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/order/history",
        listGrid : "history_list_grid"
    });
});