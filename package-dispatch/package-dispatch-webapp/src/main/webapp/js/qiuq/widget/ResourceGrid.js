define([
        "dojo/dom-construct",
        "dojo/_base/lang",
        "dojo/_base/declare",
        "dijit/_WidgetBase",
        "dijit/form/TextBox",
        "dijit/form/Button",
        "dojox/grid/EnhancedGrid",
        "dojo/data/ObjectStore",
        // "dojo/store/JsonRest",
        "qiuq/store/JsonRest",
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

        /**
         * additional query conditions that would be placed at query bar should be an array of dijit or a single dijit,
         * which must have the "name" property (for query parameter name)
         */
        extQueryInputs : null,
        /**
         * additional buttons that would be placed at query bar
         */
        extButtons : null,

        _grid : null,
        _mainContainer : null,

        autoRefreshInterval : null,
        autoRefreshTimer : null,

        postCreate : function() {
            this._mainContainer = new BorderContainer({
                design : "headline",
                style : {
                    width : "100%",
                    height : "100%"
                }
            }).placeAt(this.domNode, "after");
            // not to remove the domNode, as the grid could not be destroy
            // this.domNode.parentNode.removeChild(this.domNode);

            this._mainContainer.addChild(new ContentPane({
                region : "top",
                content : this._createOperation()
            }));

            this._mainContainer.addChild(new ContentPane({
                region : "center",
                content : this._createGrid()
            }));

            // try to start refresh automatically (if the autoRefreshInterval is assigned).
            this.startAutoRefresh();

            this.onCreate();
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
            // create the query input
            this._queryInput = new TextBox(lang.mixin({}, this.queryInputProp));
            this.connect(this._queryInput, "onKeyUp", function(evt) {
                if (evt.keyCode == "13") {
                    this._onQuery();
                }
            });

            // create the query button
            var queryButton = new Button({
                iconClass : "dijitIconSearch",
                label : this._queryButtonLabel
            });
            this.connect(queryButton, "onClick", function() {
                this._onQuery();
            });

            // create the refresh button
            var refreshButton = new Button({
                iconClass : "dijitIconUndo",
                label : this._refreshButtonLabel
            });
            this.connect(refreshButton, "onClick", function() {
                this._onQuery();
            });

            // create the container
            var queryBar = domConstruct.create("div");

            // place the query input into the container
            this._queryInput.placeAt(queryBar);
            // add additional query if assigned @ 2012-5-19
            this._appendExtElement(this.extQueryInputs, queryBar);

            // place the buttons into the container
            queryButton.placeAt(queryBar);
            refreshButton.placeAt(queryBar);
            // add additional buttons if assigned @ 2012-5-20
            this._appendExtElement(this.extButtons, queryBar);

            return queryBar;
        },

        _appendExtElement : function(extElement, container) {
            if (extElement == null) {
                return;
            }
            if (lang.isArray(extElement)) {
                // the additional element may be an array of dijit.
                for ( var i = 0; i < extElement.length; i++) {
                    extElement[i].placeAt(container);
                }
            } else {
                // the additional element may be just an dijit.
                extElement.placeAt(container);
            }
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
                    pagination : {
                        defaultPageSize : 20,
                        pageSizes : [ 10, 20, 30, 50, 100, Infinity ],
                    }
                },
                style : {
                    width : "100%",
                    height : "100%"
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
            this.stopAutoRefresh();
            this.onDestroy();
        },

        destroyRendering : function(/* Boolean? */preserveDom) {
            this.inherited(arguments);
            this._mainContainer.destroyRecursive(preserveDom);
        },

        startAutoRefresh : function() {
            if (this.autoRefreshInterval) {
                var self = this;
                this.autoRefreshTimer = setTimeout(function() {
                    self._onQuery();
                    self.startAutoRefresh();
                }, this.autoRefreshInterval);
            }
        },

        stopAutoRefresh : function() {
            if (this.autoRefreshTimer != null) {
                clearTimeout(this.autoRefreshTimer);
            }
        },

        onCreate : function() {
        },

        onDestroy : function() {
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