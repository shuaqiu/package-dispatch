define([
        "dojo/_base/xhr",
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
            return;
        }

        var grid = registry.byId(id.listGrid);
        grid.store.newItem(domform.toObject(form));
        grid.store.save();

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
        "save" : save,
        "del" : del
    };
});