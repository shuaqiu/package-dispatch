define([
        "dojo/_base/lang",
        "dijit/registry",
        "../resource",
        "../selection",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/receiver",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, registry, resource, selection, DataSelectionDialog, message) {

    return lang.mixin({}, resource, selection, {
        resourceUrl : "web/receiver",
        listGrid : "receiver_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "receiver_editing_tab",
        editingForm : "receiver_editing_form",

        companyDialog : "receiver_editing_company_dialog",

        showSelectionDialog : function() {
            var dialog = registry.byId(this.companyDialog);
            if (!dialog) {
                var doSelect = lang.hitch(this, this.doSelect);
                dialog = new DataSelectionDialog({
                    id : this.companyDialog,
                    storeTarget : 'web/receivercompany/',
                    structure : [ {
                        name : "名称",
                        field : "name",
                        width : "250px"
                    }, {
                        name : "地址",
                        field : "address",
                        width : "350px"
                    } ],
                    onRowClick : function(item) {
                        doSelect(item);
                    }
                });
            }
            dialog.show();
        },

        doSelect : function(item) {
            var form = document.forms[this.editingForm];
            form["companyId"].value = item["id"];
            registry.byId(form["company"].id).set("value", item["name"]);
            registry.byId(form["address"].id).set("value", item["address"]);
        }
    });
});