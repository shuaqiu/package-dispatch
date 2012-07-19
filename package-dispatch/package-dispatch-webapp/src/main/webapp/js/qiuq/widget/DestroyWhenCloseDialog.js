define([ "dojo/_base/declare", "dijit/Dialog" ], function(declare, Dialog) {

    return declare("qiuq.widget.DestroyWhenCloseDialog", Dialog, {
        refocus : false,

        onHide : function() {
            this.destroyRecursive();
        }
    });
});