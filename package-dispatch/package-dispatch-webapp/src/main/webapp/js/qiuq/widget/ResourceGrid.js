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

        gridOption : null,

        doView : null,
        doCreate : null,
        doModify : null,
        doDelete : null,
        _viewLabel : message["view"],
        _createMenuLabel : message["create"],
        _modifyMenuLabel : message["modify"],
        _deleteMenuLabel : message["delete"],

        _queryButtonLabel : message["query"],
        _refreshButtonLabel : message["refresh"],
        queryInputProp : null,
        _queryInput : null,

        extQueryInputs : null,

        _grid : null,
        _mainContainer : null,

        postCreate : function() {
            this._mainContainer = new BorderContainer({
                design : "headline"
            }).placeAt(this.domNode, "after");
            // this.domNode.parentNode.removeChild(this.domNode);

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
            this._queryInput = new TextBox(lang.mixin({}, this.queryInputProp));
            this.connect(this._queryInput, "onKeyUp", function(evt) {
                if (evt.keyCode == "13") {
                    this._onQuery();
                }
            });

            var queryButton = new Button({
                iconClass : "dijitIconSearch",
                label : this._queryButtonLabel
            });
            this.connect(queryButton, "onClick", function() {
                this._onQuery();
            });

            var refreshButton = new Button({
                iconClass : "dijitIconUndo",
                label : this._refreshButtonLabel
            });
            this.connect(refreshButton, "onClick", function() {
                this._onQuery();
            });

            var queryBar = domConstruct.create("div");
            this._queryInput.placeAt(queryBar);

            // add additional query if assigned @ 2012-5-19
            if (this.extQueryInputs != null) {
                if (lang.isArray(this.extQueryInputs)) {
                    for ( var i = 0; i < this.extQueryInputs.length; i++) {
                        this.extQueryInputs[i].placeAt(queryBar);
                    }
                } else {
                    this.extQueryInputs.placeAt(queryBar);
                }
            }

            queryButton.placeAt(queryBar);
            refreshButton.placeAt(queryBar);
            return queryBar;
        },

        _createMenu : function() {
            var menuBar = new MenuBar({});

            if (this.doView && lang.isFunction(this.doView)) {
                MenuBarItem({
                    label : this._viewMenuLabel,
                    onClick : this.doView
                }).placeAt(menuBar);
            }

            if (this.doCreate && lang.isFunction(this.doCreate)) {
                MenuBarItem({
                    label : this._createMenuLabel,
                    onClick : this.doCreate
                }).placeAt(menuBar);
            }
            if (this.doModify && lang.isFunction(this.doModify)) {
                MenuBarItem({
                    label : this._modifyMenuLabel,
                    onClick : this.doModify
                }).placeAt(menuBar);
            }
            if (this.doDelete && lang.isFunction(this.doDelete)) {
                MenuBarItem({
                    label : this._deleteMenuLabel,
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
            var query = {
                query : this._queryInput.get("value")
            };

            // add additional query if assigned @ 2012-5-19
            if (this.extQueryInputs != null) {
                if (lang.isArray(this.extQueryInputs)) {
                    for ( var i = 0; i < this.extQueryInputs.length; i++) {
                        query[this.extQueryInputs[i].get("name")] = this.extQueryInputs[i].get("value");
                    }
                } else {
                    query[this.extQueryInputs.get("name")] = this.extQueryInputs.get("value");
                }
            }

            this._grid.setQuery(query);
        },

        _createGrid : function() {
            var option = {
                id : this.listGrid,
                store : new ObjectStore({
                    objectStore : new JsonRest({
                        target : this.storeTarget,
                        sortParam : 'sort'
                    })
                }),
                structure : this.structure,
                plugins : {
                    pagination : true
                }
            };
            if (this.gridOption) {
                option = lang.mixin(option, this.gridOption);
            }

            this._grid = new EnhancedGrid(option);
            // add the double click action
            this.connect(this._grid, "onRowDblClick", this._onRowDblClick);

            return this._grid;
        },

        _onRowDblClick : function(evt) {
            // var idx = evt.rowIndex;
            // var item = this._grid.getItem(idx);
            if (this.doModify && lang.isFunction(this.doModify)) {
                // if modifiable, add the function that it can be modified by double click it. @ 2012-5-14
                this.doModify();
            }
        },

        destroy : function(/* Boolean */preserveDom) {
            this.inherited(arguments);
        },

        destroyRendering : function(/* Boolean? */preserveDom) {
            this.inherited(arguments);
            this._mainContainer.destroyRecursive(preserveDom);
        },

        queryWith : function(value) {
            this._queryInput.set("value", value);
            var q = lang.hitch(this, this._onQuery);
            setTimeout(function() {
                q();
            }, 100);
        }
    });
});