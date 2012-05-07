define([ "dojo/dom-construct", "dojo/_base/declare", "./DestroyWhenCloseDialog" ], function(domconstruct, declare,
        DestroyWhenCloseDialog) {
    return declare("qiuq.widget.LoadingDialog", DestroyWhenCloseDialog, {
        postCreate : function() {
            this.inherited(arguments);
            this.set("content", this.loadingMessage);
        },

        show : function() {
            this.inherited(arguments);
            domconstruct.create(this.titleBar, {
                style : {
                    display : "none"
                }
            });
        }
    });
});