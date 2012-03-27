define([
        "dojo/_base/xhr",
        "dojo/dom-form",
        "dijit/registry",
        "dijit/Dialog",
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
            return;
        }

        var grid = registry.byId(id.listGrid);
        grid.store.newItem(domform.toObject(form));
        grid.store.save();

        registry.byId(id.creationDialog).destroyRecursive();
    }

    function create() {
        new Dialog({
            "id" : id.creationDialog,
            "title" : "Create Company",
            "href" : "web/company/new"
        }).show();
    }

    function del() {
        var grid = registry.byId(id.listGrid);
        grid.removeSelectedRows();
        grid.store.save();
    }

    return {
        "save" : save,
        "create" : create,
        "del" : del
    };
});