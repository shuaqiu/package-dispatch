define([
        "dojo/_base/lang",
        "dijit/registry",
        "../resource",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/receiver",
        "dijit/form/Select" ], function(lang, registry, resource, DataSelectionDialog, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/receiver",
        listGrid : "receiver_list_grid",

        creationTabName : message["creation"],
        creationTab : "receiver_creation_tab",
        creationForm : "receiver_creation",

        companyDialog : "receiver_creation_company_dialog",

        showReceiverCompany : function() {
            var dialog = registry.byId(this.companyDialog);
            if (!dialog) {
                dialog = new DataSelectionDialog({
                    id : this.companyDialog,
                    store : new dojo.data.ObjectStore({
                        objectStore : new dojo.store.JsonRest({
                            target : 'web/receivercompany/',
                            sortParam : 'sort'
                        })
                    }),
                    structure : [ {
                        name : "名称",
                        field : "name",
                        width : "250px"
                    }, {
                        name : "地址",
                        field : "address",
                        width : "350px"
                    } ],
                    onRowClick : function(item, idx) {
                        selectReceiverCompany(item);
                    }
                });
            }
            dialog.show();
        },

        selectReceiverCompany : function(companyId, company) {
            var form = document.forms[this.creationForm];
            registry.byId(form["companyId"].id).set("value", item["id"]);
            registry.byId(form["company"].id).set("value", item["name"]);
            registry.byId(form["address"].id).set("value", item["address"]);
        }
    });
});