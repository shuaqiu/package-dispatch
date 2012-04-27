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
        editingForm : "role_editing_form",
        
        _initForm : function(item) {
            var form = document.forms[this.editingForm];

            for ( var p in item) {
                if(p == "role_id"){
                    registry.byId("role_editing_role").set("value", item[p]);
                    return;
                }
                
                var elem = form[p];
                if (elem) {
                    if (elem.id) {
                        registry.byId(elem.id).set("value", item[p]);
                    } else {
                        elem.value = item[p];
                    }
                }
            }
        },
    });
});