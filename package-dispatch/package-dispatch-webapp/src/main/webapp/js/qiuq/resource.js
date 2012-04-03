define([
        "dojo/dom-form",
        "dojo/_base/xhr",
        "dojo/_base/lang",
        "dojo/_base/Deferred",
        "dijit/registry",
        "./tab",
        "./widget/MessageDialog",
        "dojo/i18n!./nls/resource",
        "dojo/data/ObjectStore",
        "dojo/store/JsonRest",
        "dojo/store/Memory",
        "dojo/store/Cache",
        "dojox/grid/EnhancedGrid",
        "dijit/MenuBar",
        "dijit/MenuBarItem",
        "dijit/form/Form",
        "dijit/form/ValidationTextBox",
        "dijit/form/Button" ], function(domform, xhr, lang, Deferred, registry, tab, MessageDialog, message) {

    return {
        resourceUrl : null,
        listGrid : null,

        creationTabName : null,
        creationTab : null,
        creationForm : null,

        doCreate : function() {
            var deferred = new Deferred();
            tab.show([], {
                "title" : this.creationTabName,
                "href" : this.resourceUrl + "/edit",
                onLoad : function() {
                    deferred.resolve();
                }
            }, this.creationTab);

            return deferred;
        },

        doSave : function() {
            var form = document.forms[this.creationForm];

            var dijitForm = registry.byNode(form);
            if (dijitForm.isValid() == false) {
                MessageDialog.error(message["err.INVALID"]);
                return;
            }

            var saved = lang.hitch(this, this._saved);
            this._saveByXhr(form).then(function(result) {
                saved(result);
            });

            tab.close(this.creationTab);
        },

        _saveByXhr : function(form) {
            var itemId = form["id"].value;

            if (itemId) {
                return xhr.put({
                    "url" : this.resourceUrl + "/" + itemId,
                    "putData" : domform.toJson(form),
                    "handleAs" : "json",
                    "contentType" : "application/json"
                });
            } else {
                return xhr.post({
                    "url" : this.resourceUrl,
                    "postData" : domform.toJson(form),
                    "handleAs" : "json",
                    "contentType" : "application/json"
                });
            }
        },

        _saved : function(result) {
            if (result.ok) {
                var grid = registry.byId(this.listGrid);
                if (grid) {
                    grid._refresh(true);
                }
            } else {
                MessageDialog.error(message["err." + result.errCode]);
            }
        },

        doModify : function() {
            var grid = registry.byId(this.listGrid);
            var items = grid.selection.getSelected();
            if (items.length != 1) {
                MessageDialog.error(message["err.NOT_SELECTED"]);
                return;
            }

            var item = items[0];
            var _initForm = lang.hitch(this, this._initForm);
            this.doCreate().then(function() {
                _initForm(item);
            });
        },

        _initForm : function(item) {
            var form = document.forms[this.creationForm];

            for ( var p in item) {
                var elem = form[p];
                if (elem) {
                    if (elem.id) {
                        registry.byId(elem.id).set("value", item[p]);
                    } else {
                        elem.value = item[p];
                    }
                }
            }
        },

        doDelete : function() {
            var grid = registry.byId(this.listGrid);
            if (grid.selection.getSelected().length == 0) {
                return;
            }

            grid.removeSelectedRows();
            grid.store.save();
        }
    };
});