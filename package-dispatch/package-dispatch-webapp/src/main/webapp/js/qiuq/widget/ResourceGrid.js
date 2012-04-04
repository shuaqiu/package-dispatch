define([
        "dojo/dom-construct",
        "dojo/_base/lang",
        "dojo/_base/declare",
        "dijit/_WidgetBase",
        "dijit/form/TextBox",
        "dijit/form/Button",
        "dojox/grid/EnhancedGrid",
        "dojo/data/ObjectStore",
        "dojo/store/JsonRest",
        "dijit/layout/BorderContainer",
        "dijit/layout/ContentPane",
        "dijit/MenuBar",
        "dijit/MenuBarItem",
        "dojo/i18n!./nls/ResourceGrid",
        "dojox/grid/enhanced/plugins/Pagination" ], function(domConstruct, lang, declare, _WidgetBase, TextBox, Button,
        EnhancedGrid, ObjectStore, JsonRest, BorderContainer, ContentPane, MenuBar, MenuBarItem, message) {

    return declare("qiuq.widget.ResourceGrid", [ _WidgetBase ], {
        listGrid : null,
        storeTarget : null,
        structure : null,

        doCreate : null,
        doModify : null,
        doDelete : null,

        _queryInput : null,
        _grid : null,
        _mainContainer : null,

        postCreate : function() {
            this._mainContainer = new BorderContainer({
                design : "headline"
            }).placeAt(this.domNode, "after");
            this.domNode.parentNode.removeChild(this.domNode);

            this._mainContainer.addChild(new ContentPane({
                region : "top",
                content : this._createOperation()
            }));

            this._mainContainer.addChild(new ContentPane({
                region : "center",
                content : this._createGrid()
            }));
        },

        _createOperation : function() {
            var queryBar = this._createQuery();
            var menuBar = this._createMenu();

            if (menuBar == null) {
                return queryBar;
            }

            var operation = domConstruct.create("div");
            operation.appendChild(queryBar);
            menuBar.placeAt(operation);
            return operation;
        },

        _createQuery : function() {
            this._queryInput = new TextBox({});
            this.connect(this._queryInput, "onKeyUp", function(evt) {
                if (evt.keyCode == "13") {
                    this._onQuery();
                }
            });
            var queryButton = new Button({
                label : message["query"]
            });
            this.connect(queryButton, "onClick", function() {
                this._onQuery();
            });

            var queryBar = domConstruct.create("div");
            this._queryInput.placeAt(queryBar);
            queryButton.placeAt(queryBar);
            return queryBar;
        },

        _createMenu : function() {
            var menuBar = new MenuBar({});

            if (this.doCreate && lang.isFunction(this.doCreate)) {
                MenuBarItem({
                    label : message["create"],
                    onClick : this.doCreate
                }).placeAt(menuBar);
            }
            if (this.doModify && lang.isFunction(this.doModify)) {
                MenuBarItem({
                    label : message["modify"],
                    onClick : this.doModify
                }).placeAt(menuBar);
            }
            if (this.doDelete && lang.isFunction(this.doDelete)) {
                MenuBarItem({
                    label : message["delete"],
                    onClick : this.doDelete
                }).placeAt(menuBar);
            }

            if (menuBar.getChildren().length > 0) {
                return menuBar;
            }
            menuBar.destroyRecursive();
            return null;
        },

        _onQuery : function() {
            this._grid.setQuery({
                query : this._queryInput.get("value")
            });
        },

        _createGrid : function() {
            this._grid = new EnhancedGrid({
                id : this.listGrid,
                store : new ObjectStore({
                    objectStore : new JsonRest({
                        target : this.storeTarget,
                        sortParam : 'sort'
                    })
                }),
                height : "600px",
                structure : this.structure,
                plugins : {
                    pagination : true
                }
            });
            return this._grid;
        },

        destroyRendering : function(/* Boolean? */preserveDom) {
            this.inherited(arguments);
            this._mainContainer.destroyRecursive(preserveDom);
        }
    });
});