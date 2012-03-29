define([
        "dojo/_base/xhr",
        "dojo/dom-form",
        "dijit/registry",
<<<<<<< HEAD
        "../tab",
=======
        "dijit/Dialog",
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
        "../widget/MessageDialog",
        "../ErrCode",
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
<<<<<<< HEAD
        "dijit/form/Button" ], function(xhr, domform, registry, tab, MessageDialog, ErrCode, message) {

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
=======
        "dijit/form/Button" ], function(xhr, domform, registry, Dialog, MessageDialog, ErrCode, message) {

    var id = {
        listGrid : "company_list_grid",
        creationDialog : "company_creation_dialog"
    };
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d

    function save() {
        var form = document.forms[id.form];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            MessageDialog.error(message["err.INVALID"]);
            return;
        }

        var grid = registry.byId(id.listGrid);
        grid.store.newItem(domform.toObject(form));
        grid.store.save();

<<<<<<< HEAD
        tab.close(id.creationTab);
    }

=======
        registry.byId(id.creationDialog).destroyRecursive();
    }

    function create() {
        new Dialog({
            "id" : id.creationDialog,
            "title" : "Create Company",
            "href" : "web/company/new"
        }).show();
    }

>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
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