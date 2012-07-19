define([
        "require",
        "dojo/dom-form",
        "dojo/_base/xhr",
        "dojo/_base/lang",
        "dojo/_base/Deferred",
        "dijit/registry",
        "./tab",
        "./widget/MessageDialog",
        "./widget/LoadingDialog",
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
        "dijit/form/Button" ], function(require, domform, xhr, lang, Deferred, registry, tab, MessageDialog,
        LoadingDialog, message) {

    return {
        resourceUrl : null,
        listGrid : null,

        viewTabName : null,
        createTabName : null,
        modifyTabName : null,
        viewTab : null,
        editingTab : null,
        editingForm : null,

        doView : function() {
            var grid = registry.byId(this.listGrid);
            var items = grid.selection.getSelected();
            if (items.length != 1) {
                MessageDialog.error(message["err.NOT_SELECTED"]);
                return;
            }

            var item = items[0];

            tab.show([], {
                title : this.viewTabName,
                href : this.resourceUrl + "/view/" + item["id"],
            }, this.viewTab, true);
        },

        doCreate : function(setting) {
            tab.show([], {
                title : this.createTabName,
                href : this.resourceUrl + "/edit",
            }, this.editingTab, true);

        },

        doSave : function() {
            var data = this._getValidData();
            if (data == null) {
                return;
            }

            var dialog = new LoadingDialog({});
            dialog.show();

            var saved = lang.hitch(this, this._saved);
            this._saveByXhr(data).then(function(result) {
                dialog.hide();
                saved(result);
            });
        },

        _getValidData : function() {
            var form = document.forms[this.editingForm];

            var dijitForm = registry.byNode(form);
            if (dijitForm.isValid() == false) {
                MessageDialog.error(message["err.INVALID"]);
                return null;
            }
            return domform.toJson(form);
        },

        _saveByXhr : function(data) {
            var form = document.forms[this.editingForm];
            var itemId = form["id"].value;

            if (itemId) {
                return xhr.put({
                    url : this.resourceUrl + "/" + itemId,
                    putData : data,
                    handleAs : "json",
                    contentType : "application/json"
                });
            } else {
                return xhr.post({
                    url : this.resourceUrl,
                    postData : data,
                    handleAs : "json",
                    contentType : "application/json"
                });
            }
        },

        _saved : function(result) {
            if (result.ok) {
                var grid = registry.byId(this.listGrid);
                if (grid) {
                    grid._refresh(true);
                }
                tab.close(this.editingTab);
            } else {
                if (result.errCode == "NOT_LOGINED") {
                    require([ "qiuq/login" ], function(login) {
                        login.reLogin();
                    });
                    return;
                }
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

            var deferred = new Deferred();
            tab.show([], {
                title : this.modifyTabName,
                href : this.resourceUrl + "/edit",
                onLoad : function() {
                    deferred.resolve();
                }
            }, this.editingTab, true);

            var self = this;
            deferred.then(function() {
                self._initForm(items[0]);
            });
        },

        _initForm : function(item) {
            var form = document.forms[this.editingForm];

            for ( var p in item) {
                this._initFormItem(item, form, p);
            }
        },

        _initFormItem : function(item, form, p) {
            var elem = form[p];
            if (elem) {
                if (elem.id) {
                    registry.byId(elem.id).set("value", item[p]);
                } else {
                    elem.value = item[p];
                }
            }
        },

        doDelete : function() {
            var grid = registry.byId(this.listGrid);

            var selected = grid.selection.getSelected();
            if (selected.length == 0) {
                return;
            }

            for ( var i = 0; i < selected.length; i++) {
                xhr.del({
                    url : this.resourceUrl + "/" + grid.store.getIdentity(selected[i]),
                });
            }

            grid.removeSelectedRows();
        }
    };
});