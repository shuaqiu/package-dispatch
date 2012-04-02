define([
        "dojo/_base/declare",
        "dijit/_WidgetBase",
        "dijit/_TemplatedMixin",
        "dijit/Dialog",
        "dijit/form/TextBox",
        "dijit/form/Button",
        "dojox/grid/DataGrid",
        "dojo/i18n!./nls/DataSelectionDialog",
        "dojo/text!./templates/DataSelectionDialog.html" ], function(declare, _WidgetBase, _TemplatedMixin, Dialog,
        TextBox, Button, DataGrid, message, template) {

    return declare("qiuq.widget.DataSelectionDialog", [ _WidgetBase, _TemplatedMixin ], {
        templateString : template,

        store : null,
        structure : null,

        width : "600px",
        height : "300px",

        isQueryable : true,

        title : message["selection"],

        _queryInput : null,
        _queryButton : null,
        _grid : null,
        _dialog : null,

        postCreate : function() {
            this._grid = new DataGrid({
                store : this.store,
                structure : this.structure,
                width : this.width,
                height : this.height
            }, this.gridNode);

            this.connect(this._grid, "onRowDblClick", this._onRowDblClick);

            if (this.isQueryable) {
                this._queryInput = new TextBox({}).placeAt(this.queryNode);

                this._queryButton = new Button({
                    label : message["query"]
                }).placeAt(this.queryNode);
                this.connect(this._queryButton, "onClick", function() {
                    this._onQuery();
                });
            }

            if (this.isQueryable) {
                this.dialogNode.style.width = (parseInt(this.width.replace("px", "")) + 20) + "px";
                this.dialogNode.style.height = (parseInt(this.height.replace("px", "")) + 34) + "px";
            } else {
                this.dialogNode.style.width = this.width;
                this.dialogNode.style.height = this.height;
            }

            this._dialog = new Dialog({
                title : this.title,
                content : this.dialogNode
            });
        },

        show : function() {
            this._dialog.show();
        },

        hide : function() {
            this._dialog.hide();
        },

        _onRowDblClick : function(evt) {
            var idx = evt.rowIndex;
            var item = this._grid.getItem(idx);

            this.onRowClick(item, idx);
            this.hide();
        },
        
        onRowClick : function(item, idx){
            
        },

        _onQuery : function() {
            this._grid.setQuery({
                query : this._queryInput.get("value")
            });
        }
    });
});