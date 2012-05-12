define([
        "dojo/on",
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "dijit/popup",
        "dijit/layout/ContentPane",
        "./account",
        "../suggest",
        "dojo/i18n!./nls/customer",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(on, lang, xhr, registry, popup, ContentPane, account, suggest, message) {

    return lang.mixin({}, account, suggest, {
        createTabName : message["create"],
        modifyTabName : message["modify"],

        selectionDialog : "customer_editing_company_dialog",
        selectionStoreTarget : "web/company",
        selectionStructure : [ {
            name : message["code"],
            field : "code",
            width : "80px"
        }, {
            name : message["name"],
            field : "name",
            width : "180px"
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

        onCompanyKeyUps : function() {
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
            account._initForm(item);
            this._selectedItem = {
                id : item.companyId,
                name : item.company
            };
        }
    });
});