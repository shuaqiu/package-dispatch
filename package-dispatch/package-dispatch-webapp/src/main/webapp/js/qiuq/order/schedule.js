define([
        "require",
        "dojo/query",
        "dojo/dom-attr",
        "dojo/json",
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "../resource",
        "../widget/MessageDialog",
        "dojo/i18n!./nls/schedule",
        "dojo/date/locale",
        "dojo/dnd/Source",
        "dojo/dnd/Target",
        "dijit/form/CheckBox",
        "dijit/form/Textarea",
        "../widget/ResourceGrid" ], function(require, query, attr, json, lang, xhr, registry, resource, MessageDialog,
        message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/schedule",
        listGrid : "schedule_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "schedule_editing_tab",
        editingForm : "schedule_editing_form",

        doModify : function(orderId) {
            var self = this;
            showTab([], {
                title : this.modifyTabName,
                href : this.resourceUrl + "/edit/" + orderId,
                onLoad : function() {
                    self._initForm();
                }
            }, this.editingTab, true);
        },

        _initForm : function() {
            // overwrite the onDropExternal method
            fetcherTarget.onDropExternal = this.onDropExternal;
            delivererTarget.onDropExternal = this.onDropExternal;
        },

        onDropExternal : function(source, nodes, copy) {
            // query the li element within this container
            var self = this;
            // 
            query("li", this.parent).forEach(function(node) {
                // get item from container
                var t = self.getItem(node.id);
                // and then delete it
                self.delItem(node.id);

                // moved to the drop source container
                source.setItem(node.id, {
                    data : t.data,
                    type : t.type
                });
            })
            // and place the nodes to the source container
            .place(source.parent);

            var oldCreator = this._normalizedCreator;
            // transferring nodes from the source to the target
            // move nodes
            this._normalizedCreator = function(node, hint) {
                var t = source.getItem(node.id);
                source.delItem(node.id);
                return {
                    node : node,
                    data : t.data,
                    type : t.type
                };
            };
            this.selectNone();
            source.selectNone();
            this.insertNodes(true, nodes, false, null);
            source.deleteSelectedNodes();
            this._normalizedCreator = oldCreator;
        },

        _getValidData : function() {
            var fetcherLi = query("li", fetcherTarget.parent);
            if (fetcherLi.length == 0) {
                MessageDialog.error(message["err.NOT_FETCHER"]);
                return null;
            }
            var transiterLi = query("li", transiterTarget.parent);
            // if (transiterLi.length == 0) {
            // MessageDialog.error(message["err.NOT_TRANSITER"]);
            // return null;
            // }
            var delivererLi = query("li", delivererTarget.parent);
            if (transiterLi.length > 0 && delivererLi.length == 0) {
                MessageDialog.error(message["err.NOT_DELIVERER"]);
                return null;
            }

            var fetcher = this._getHandlerArr(fetcherLi);
            var transiter = this._getHandlerArr(transiterLi);
            var deliverer = this._getHandlerArr(delivererLi);

            var form = document.forms[this.editingForm];
            return json.stringify({
                FETCHED : fetcher,
                TRANSITING : transiter,
                DELIVERED : deliverer
            });
        },

        _getHandlerArr : function(nodes) {
            var arr = [];
            if (nodes.length > 0) {
                nodes.forEach(function(node) {
                    arr.push(attr.get(node, "data-handler"));
                });
            }
            return arr;
        },

        _saveByXhr : function(data) {
            var form = document.forms[this.editingForm];
            var state = form["orderState"].value;

            var method = state == 0 ? xhr.post : xhr.put;
            return method({
                url : this.resourceUrl + "/edit/" + form["orderId"].value,
                postData : data,
                handleAs : "json",
                contentType : "application/json"
            });
        },

        _saved : function(result) {
            if (result.ok) {
                var form = document.forms[this.editingForm];
                require([ "./schedulemonitor" ], function(schedulemonitor) {
                    schedulemonitor.removeItem(form["orderId"].value);
                });
            }
            lang.hitch(this, resource._saved)(result);
        }
    });
});