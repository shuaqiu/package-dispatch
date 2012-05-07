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
        },

        isShowCancelButton : false,
        cancelButtonLabel : message["cancelButtonLabel"],
        cancelButton : null,
        onCancel : function() {
        },

        postCreate : function() {
            this.inherited(arguments);

            domconstruct.create(this.containerNode, {
                style : {
                    "padding" : "0"
                }
            });

            this._createButtons();
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

            domconstruct.create("div", {
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

        _createButtons : function() {
            if (this.isShowOkButton) {
                // var action = lang.hitch(this, function() {
                // if (this.onOk && lang.isFunction(this.onOk)) {
                // this.onOk();
                // }
                // this.hide();
                // });
                // this.okButton = new Button({
                // label : this.okButtonLabel,
                // onClick : function() {
                // action();
                // }
                // });
                this.okButton = this._createButton(this.okButtonLabel, this.onOk);
            }

            if (this.isShowCancelButton) {
                // var action = lang.hitch(this, function() {
                // if (this.onCancel && lang.isFunction(this.onCancel)) {
                // this.onCancel();
                // }
                // this.hide();
                // });
                // this.cancelButton = new Button({
                // label : this.cancelButtonLabel,
                // onClick : function() {
                // action();
                // }
                // });
                this.cancelButton = this._createButton(this.cancelButtonLabel, this.onCancel);
            }
        },

        _createButton : function(label, clickCallback) {
            var action = lang.hitch(this, function() {
                if (clickCallback && lang.isFunction(clickCallback)) {
                    clickCallback();
                }
                this.hide();
            });
            return new Button({
                label : label,
                onClick : function() {
                    action();
                }
            });
        }
    });

    function alert(content, onOk) {
        var dialog = new MessageDialog({
            title : message["alertTitle"],
            iconClass : "alertIcon",
            content : content
        });

        if (onOk && lang.isFunction(onOk)) {
            dialog.onOk = onOk;
        }

        dialog.show();
    }

    function error(content, onOk) {
        var dialog = new MessageDialog({
            title : message["errTitle"],
            iconClass : "errorIcon",
            content : content
        });

        if (onOk && lang.isFunction(onOk)) {
            dialog.onOk = onOk;
        }

        dialog.show();
    }

    function confirm(content, onOk, onCancel) {
        var dialog = new MessageDialog({
            title : message["confirmTitle"],
            iconClass : "confirmIcon",
            content : content,
            isShowCancelButton : true
        });

        if (onOk && lang.isFunction(onOk)) {
            dialog.onOk = onOk;
        }
        if (onCancel && lang.isFunction(onCancel)) {
            dialog.onCancel = onCancel;
        }

        dialog.show();
    }

    return {
        alert : alert,
        error : error,
        confirm : confirm
    };
});