define([
        "dojo/_base/xhr",
        "dojo/dom",
        "dojo/dom-form",
        "dijit/registry",
        "dijit/Dialog",
        "../tab",
        "../widget/MessageDialog",
        "../ErrCode",
        "dojo/i18n!./nls/user",
        "dojo/data/ObjectStore",
        "dojo/store/JsonRest",
        "dojo/store/Memory",
        "dojo/store/Cache",
        "dojox/grid/DataGrid",
        "dijit/MenuBar",
        "dijit/MenuBarItem",
        "dijit/form/Form",
        "dijit/form/ValidationTextBox",
        "dijit/form/Select",
        "dijit/form/Button" ], function(xhr, dom, domform, registry, Dialog, tab, MessageDialog, ErrCode, message) {

    var id = {
        listGrid : "user_list_grid",
        creationTab : "user_creation_tab",
        companyDialog : "user_creation_company_dialog",
        form : "user_creation",
        customerTypeRow : "user_new_customerType_row"
    };

    function create() {
        tab.show([], {
            "title" : message["creation"],
            "href" : "web/user/new"
        }, id.creationTab);
    }

    function showCompany() {
        var dialog = registry.byId(id.companyDialog);
        if (!dialog) {
            dialog = new Dialog({
                "id" : id.companyDialog,
                "title" : message["company"],
                "href" : "web/user/company"
            });
        }
        dialog.show();
    }

    function selectCompany(companyId, company) {
        var form = document.forms[id.form];
        form["companyId"].value = companyId;
        form["company"].value = company;
        
        registry.byId(id.companyDialog).hide();
    }
    
    function typeChanged(){
        var customerTypeRow = dom.byId(id.customerTypeRow);
        var val = registry.byId("user_new_type").get("value");
        if(val == 1){
            customerTypeRow.style.display = "none";
        }else{
            customerTypeRow.style.display = "";
        }
    }

    function save() {
        var form = document.forms[id.form];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            MessageDialog.error(message["err.INVALID"]);
            return;
        }

        var grid = registry.byId(id.listGrid);
        if(grid) {
            grid.store.newItem(domform.toObject(form));
            grid.store.save();
        }else{
            xhr.post({
                "url" : "web/user",
                "postData" : domform.toJson(form),
                "handleAs" : "json",
                "contentType" : "application/json"
            }).then(function(result) {
                if (result.ok) {

                } else {
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        }

        tab.close(id.creationTab);
    }

    function del() {
        var grid = registry.byId(id.listGrid);
        grid.removeSelectedRows();
        grid.store.save();
    }

    return {
        "create" : create,
        "showCompany" : showCompany,
        "selectCompany" : selectCompany,
        "typeChanged" : typeChanged,
        "save" : save,
        "del" : del
    };
});