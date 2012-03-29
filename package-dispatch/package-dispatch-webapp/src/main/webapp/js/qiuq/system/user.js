define([
        "dojo/_base/xhr",
        "dojo/dom-form",
        "dijit/registry",
        "dijit/Dialog",
<<<<<<< HEAD
        "../tab",
        "../widget/MessageDialog",
        "../ErrCode",
        "dojo/i18n!./nls/user",
=======
        "../widget/MessageDialog",
        "../ErrCode",
        "dojo/i18n!./nls/company",
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
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
        "dijit/form/Select",
        "dijit/form/Button" ], function(xhr, domform, registry, Dialog, tab, MessageDialog, ErrCode, message) {

    var id = {
        listGrid : "user_list_grid",
        creationDialog : "user_creation_dialog",
        form : "user"
    };

    function create() {
        tab.show([], {
            "title" : message["creation"],
            "href" : "web/user/new"
        }, id.creationTab);
    }

    function showCompany() {
        xhr.get({
            "url" : "web/company/list"
        }).then(function(content){
            new Dialog({
                "title" : "Company",
                "content" : content
            }).show();
        });
    }

    function save() {
        var form = document.forms[id.form];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            MessageDialog.error(message["err.INVALID"]);
=======
        "dijit/form/Button" ], function(xhr, domform, registry, Dialog, MessageDialog, ErrCode, message) {

    var id = {
        listGrid : "company_list_grid",
        creationDialog : "company_creation_dialog"
    };

    function save() {
        var form = document.forms["company"];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            MessageDialog.error(message["err." + ErrCode.INVALID]);
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
            return;
        }

        var grid = registry.byId(id.listGrid);
        grid.store.newItem(domform.toObject(form));
        grid.store.save();

<<<<<<< HEAD
        tab.close(id.creationTab);
=======
        registry.byId(id.creationDialog).destroyRecursive();
    }

    function create() {
        new Dialog({
            "id" : id.creationDialog,
            "title" : "Create Company",
            "href" : "web/company/new"
        }).show();
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
    }

    function del() {
        var grid = registry.byId(id.listGrid);
        grid.removeSelectedRows();
        grid.store.save();
    }

    return {
<<<<<<< HEAD
        "create" : create,
        "showCompany" : showCompany,
        "save" : save,
=======
        "save" : save,
        "create" : create,
>>>>>>> e4d080920acc1d9281b9de272d984c1d9c99c60d
        "del" : del
    };
});