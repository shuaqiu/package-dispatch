define([
        "dojo/_base/declare",
        "dijit/form/TextBox",
        "dijit/form/Button",
        "dojox/grid/DataGrid",
        "dojo/data/ObjectStore",
        "dojo/store/JsonRest",
        "./DestroyWhenCloseDialog",
        "dojo/i18n!./nls/DataSelectionDialog" ], function(declare, TextBox, Button, DataGrid, ObjectStore, JsonRest,
        DestroyWhenCloseDialog, message) {

    return declare("qiuq.widget.DataSelectionDialog", [ DestroyWhenCloseDialog ], {
        // size
        width : "600px",
        height : "300px",

        // query
        isQueryable : true,
        _queryInput : null,
        _queryButton : null,

        // grid
        storeTarget : null,
        structure : null,
        _grid : null,

        // dialog attributes
        title : message["selection"],

        postCreate : function() {
            this.inherited(arguments);

            var div = document.createElement("div");

            if (this.isQueryable) {
                var query = document.createElement("div");

                this._queryInput = new TextBox({}).placeAt(query);

                var timeout = null;
                var self = this;
                this.connect(this._queryInput, "onKeyUp", function(evt) {
                    // if (evt.keyCode == "13") {
                    // this._onQuery();
                    // }
                    if (timeout) {
                        clearTimeout(timeout);
                    }
                    timeout = setTimeout(function() {
                        self._onQuery();
                    }, 500);
                });

                // this._queryButton = new Button({
                // label : message["query"]
                // }).placeAt(query);
                // this.connect(this._queryButton, "onClick", function() {
                // this._onQuery();
                // });

                div.appendChild(query);
            }

            this._grid = new DataGrid({
                store : new ObjectStore({
                    objectStore : new JsonRest({
                        target : this.storeTarget,
                        sortParam : 'sort'
                    })
                }),
                structure : this.structure,
                width : this.width,
                height : this.height
            }).placeAt(div);

            this.connect(this._grid, "onRowDblClick", this._onRowDblClick);

            var dim = {
                width : this.width,
                height : this.height
            };
            if (this.isQueryable) {
                dim = {
                    width : (parseInt(this.width.replace("px", "")) + 20) + "px",
                    height : (parseInt(this.height.replace("px", "")) + 34) + "px"
                }
            }

            this.set("content", div);
            this.set("style", dim);
        },

        _onRowDblClick : function(evt) {
            var idx = evt.rowIndex;
            var item = this._grid.getItem(idx);

            this.onRowClick(item, idx);
            this.hide();
        },

        onRowClick : function(item, idx) {

        },

        _onQuery : function() {
            this._grid.setQuery({
                query : this._queryInput.get("value")
            });
        }
    });
});