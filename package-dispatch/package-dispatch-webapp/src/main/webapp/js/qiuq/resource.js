define([
        "dojo/_base/xhr",
        "dojo/dom-form",
        "dijit/registry",
        "./tab",
        "./widget/MessageDialog",
        "dojo/i18n!./nls/resource",
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

    function _saveByGrid(form, grid) {
        grid.store.newItem(domform.toObject(form));
        grid.store.save({
            onComplete : function() {
                grid._refresh(true);
            }
        });
    }

    function _saveByXhr(form, resourceUrl) {
        xhr.post({
            "url" : resourceUrl,
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

    return {
        resourceUrl : null,
        listGrid : null,

        creationTabName : null,
        creationTab : null,
        creationForm : null,

        create : function() {
            tab.show([], {
                "title" : this.creationTabName,
                "href" : this.resourceUrl + "/edit"
            }, this.creationTab);
        },

        save : function() {
            var form = document.forms[this.creationForm];

            var dijitForm = registry.byId(form.id);
            if (dijitForm.isValid() == false) {
                MessageDialog.error(message["err.INVALID"]);
                return;
            }

            var grid = registry.byId(this.listGrid);
            if (grid) {
                _saveByGrid(form, grid);
            } else {
                _saveByXhr(form, this.resourceUrl);
            }

            tab.close(this.creationTab);
        },

        del : function() {
            var grid = registry.byId(this.listGrid);
            grid.removeSelectedRows();
            grid.store.save();
        }
    };
});