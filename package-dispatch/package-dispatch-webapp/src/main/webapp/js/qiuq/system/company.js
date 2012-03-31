define([
        "dojo/_base/xhr",
        "dojo/dom-form",
        "dijit/registry",
        "../tab",
        "../widget/MessageDialog",
        "dojo/i18n!./nls/company",
        "dojo/data/ObjectStore",
        "dojo/store/JsonRest",
        "dojo/store/Memory",
        "dojo/store/Cache",
        "dojox/grid/DataGrid",
        "dijit/MenuBar",
        "dijit/MenuBarItem",
        "dijit/form/Form",
        "dijit/form/ValidationTextBox",
        "dijit/form/Button" ], function(xhr, domform, registry, tab, MessageDialog, message) {

    var id = {
        listGrid : "company_list_grid",
        creationTab : "company_creation_tab",
        form : "company"
    };

    function create() {
        tab.show([], {
            "title" : message["creation"],
            "href" : "web/company/new"
        }, id.creationTab);
    }

    function save() {
        var form = document.forms[id.form];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            MessageDialog.error(message["err.INVALID"]);
            return;
        }

        var grid = registry.byId(id.listGrid);
        if(grid){
            grid.store.newItem(domform.toObject(form));
            grid.store.save();
        }else{
            xhr.post({
                "url" : "web/company",
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
        "save" : save,
        "create" : create,
        "del" : del
    };
});