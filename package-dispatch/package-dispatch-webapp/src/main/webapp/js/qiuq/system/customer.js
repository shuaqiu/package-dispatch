define([
        "dojo/_base/lang",
        "dijit/registry",
        "./account",
        "../selection",
        "dojo/i18n!./nls/customer",
        "dijit/form/Select",
        "../widget/ResourceGrid" ], function(lang, registry, account, selection, message) {

    return lang.mixin({}, account, selection, {
        createTabName : message["create"],
        modifyTabName : message["modify"],

        selectionDialog : "customer_editing_company_dialog",
        selectionStoreTarget : "web/company",
        selectionStructure : [ {
            name : message["code"],
            field : "code",
            width : "150px"
        }, {
            name : message["name"],
            field : "name",
            width : "200px"
        }, {
            name : message["address"],
            field : "address",
            width : "250px"
        } ],

        doSelect : function(item) {
            var form = document.forms[this.editingForm];
            form["companyId"].value = item["id"];
            registry.byId(form["company"].id).set("value", item["name"]);
        },
        
        _initForm : function(item) {
            var form = document.forms[this.editingForm];

            for ( var p in item) {
                if(p == "customer_type"){
                    registry.byId("customer_editing_customerType").set("value", item[p]);
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
        }
    });
});