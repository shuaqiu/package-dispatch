define([
        "dojo/_base/lang",
        "dijit/registry",
        "../resource",
        "../suggest",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/receiver",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, registry, resource, suggest, DataSelectionDialog, message) {

    return lang.mixin({}, resource, suggest, {
        resourceUrl : "web/receiver",
        listGrid : "receiver_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "receiver_editing_tab",
        editingForm : "receiver_editing_form",

        selectionDialog : "receiver_editing_company_dialog",
        selectionStoreTarget : "web/receivercompany",
        selectionStructure : [ {
            name : message["name"],
            field : "name",
            width : "150px"
        }, {
            name : message["address"],
            field : "address",
            width : "250px"
        } ],

        doSelect : function(item) {
            this._selectedItem = item;

            var form = document.forms[this.editingForm];
            form["companyId"].value = item["id"];
            registry.byId(form["company"].id).set("value", item["name"]);
            registry.byId(form["address"].id).set("value", item["address"]);
        },

        _selectedItem : null,

        onCompanyKeyUp : function() {
            if (this._selectedItem == null) {
                return;
            }

            var form = document.forms[this.editingForm];

            var company = registry.byId(form["company"].id).get("value");
            if (company != this._selectedItem["name"]) {
                form["companyId"].value = "-1";
            } else {
                form["companyId"].value = this._selectedItem["id"];
            }
        },

        _initForm : function(item) {
            lang.hitch(this, resource._initForm)(item);
            this._selectedItem = {
                id : item.companyId,
                name : item.company
            };
        }
    });
});