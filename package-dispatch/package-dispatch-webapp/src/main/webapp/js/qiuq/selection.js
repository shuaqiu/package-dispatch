define([ "dojo/_base/lang", "dijit/registry", "./widget/DataSelectionDialog" ], function(lang, registry,
        DataSelectionDialog) {

    return {
        selectionDialog : null,
        selectionStoreTarget : null,
        selectionStructure : null,

        showSelectionDialog : function() {
            var dialog = registry.byId(this.selectionDialog);
            if (!dialog) {
                var doSelect = lang.hitch(this, this.doSelect);
                dialog = new DataSelectionDialog({
                    id : this.selectionDialog,
                    storeTarget : this.selectionStoreTarget,
                    structure : this.selectionStructure,
                    onRowClick : function(item) {
                        doSelect(item);
                    }
                });
            }
            dialog.show();
        },

        doSelect : function(item) {
        }
    };
});