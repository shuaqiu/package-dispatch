define([ "dojo/_base/declare", "dijit/Dialog" ], function(declare, Dialog) {

    return declare("DestroyWhenCloseDialog", Dialog, {
        refocus : false,

        onHide : function() {
            this.destroyRecursive();
        }
    });
});