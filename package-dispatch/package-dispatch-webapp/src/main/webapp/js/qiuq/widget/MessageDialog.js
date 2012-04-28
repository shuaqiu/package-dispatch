define([
        "dojo/dom-construct",
        "dojo/_base/declare",
        "dojo/_base/lang",
        "dijit/layout/ContentPane",
        "dijit/form/Button",
        "./DestroyWhenCloseDialog",
        "dojo/i18n!./nls/MessageDialog" ], function(domconstruct, declare, lang, ContentPane, Button,
        DestroyWhenCloseDialog, message) {

    var MessageDialog = declare("qiuq.widget.MessageDialog", DestroyWhenCloseDialog, {
        iconClass : null,

        isShowOkButton : true,
        okButtonLabel : message["okButtonLabel"],
        okButton : null,
        onOk : function() {
            console.info("onOk");
        },

        isShowCancelButton : false,
        cancelButtonLabel : message["cancelButtonLabel"],
        cancelButton : null,
        onCancel : function() {
            console.info("onCancel");
        },

        postCreate : function() {
            this.inherited(arguments);

            domconstruct.create(this.containerNode, {
                style : {
                    "padding" : "0"
                }
            });

            this._createButton();
            var b = domconstruct.create("div", {
                className : "msgButtonBar"
            });
            if (this.isShowOkButton) {
                this.okButton.placeAt(b);
            }
            if (this.isShowCancelButton) {
                this.cancelButton.placeAt(b);
            }

            var c = domconstruct.create("div");

            var icon = domconstruct.create("div", {
                className : "msgIcon " + this.iconClass,
            }, c);
            new ContentPane({
                content : this.content,
                className : "msgContent"
            }).placeAt(c);

            var m = domconstruct.create("div", {});
            m.appendChild(c);
            m.appendChild(b);

            this.set("content", m);
        },

        _createButton : function() {
            if (this.isShowOkButton) {
                var action = lang.hitch(this, function() {
                    if (this.onOk && lang.isFunction(this.onOk)) {
                        this.onOk();
                    }
                    this.hide();
                });
                this.okButton = new Button({
                    label : this.okButtonLabel,
                    onClick : function() {
                        action();
                    }
                });
            }

            if (this.isShowCancelButton) {
                var action = lang.hitch(this, function() {
                    if (this.onCancel && lang.isFunction(this.onCancel)) {
                        this.onCancel();
                    }
                    this.hide();
                });
                this.cancelButton = new Button({
                    label : this.cancelButtonLabel,
                    onClick : function() {
                        action();
                    }
                });
            }
        }
    });

    function alert(content) {
        new MessageDialog({
            title : message["alertTitle"],
            iconClass : "alertIcon",
            content : content
        }).show();
    }

    function error(content) {
        new MessageDialog({
            title : message["errTitle"],
            iconClass : "errorIcon",
            content : content
        }).show();
    }

    function confirm(content, okMethod, cancelMethod) {
        new MessageDialog({
            title : message["confirmTitle"],
            iconClass : "confirmIcon",
            content : content,
            isShowCancelButton : true
        }).show();
    }

    return {
        alert : alert,
        error : error,
        confirm : confirm
    };
});