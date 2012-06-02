define([
        "dojo/aspect",
        "dojo/dom-construct",
        "dojo/_base/declare",
        "dojo/_base/lang",
        "dijit/layout/ContentPane",
        "dijit/form/Button",
        "./DestroyWhenCloseDialog",
        "dojo/i18n!./nls/MessageDialog" ], function(aspect, domconstruct, declare, lang, ContentPane, Button,
        DestroyWhenCloseDialog, message) {

    var MessageDialog = declare("qiuq.widget.MessageDialog", DestroyWhenCloseDialog, {
        iconClass : null,

        isShowOkButton : true,
        okButtonLabel : message["okButtonLabel"],
        okButton : null,
        doOk : function() {
        },

        isShowCancelButton : false,
        cancelButtonLabel : message["cancelButtonLabel"],
        cancelButton : null,
        doCancel : function() {
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
                this.okButton = this._createButton(this.okButtonLabel, this.doOk);
            }

            if (this.isShowCancelButton) {
                this.cancelButton = this._createButton(this.cancelButtonLabel, this.doCancel);
            }
        },

        _createButton : function(label, clickCallback) {
            var action = lang.hitch(this, function() {
                var isHide = true;
                if (clickCallback && lang.isFunction(clickCallback)) {
                    // if the callback return none, the value of isHide is undefined.
                    isHide = clickCallback();
                }
                if (isHide == null || isHide == true) {
                    this.hide();
                }
            });
            return new Button({
                label : label,
                onClick : function() {
                    action();
                }
            });
        }
    });

    function alert(content, doOk) {
        if (doOk == null || !lang.isFunction(doOk)) {
            doOk = function() {
            };
        }

        var dialog = new MessageDialog({
            title : message["alertTitle"],
            iconClass : "alertIcon",
            content : content,
            doOk : doOk
        });
        aspect.before(dialog, "onCancel", doOk);

        dialog.show();
    }

    function error(content, doOk) {
        if (doOk == null || !lang.isFunction(doOk)) {
            doOk = function() {
            };
        }

        var dialog = new MessageDialog({
            title : message["errTitle"],
            iconClass : "errorIcon",
            content : content,
            doOk : doOk
        });
        aspect.before(dialog, "onCancel", doOk);

        dialog.show();
    }

    function confirm(content, doOk, doCancel) {
        if (doOk == null || !lang.isFunction(doOk)) {
            doOk = function() {
            };
        }
        if (doCancel == null || !lang.isFunction(doCancel)) {
            doCancel = function() {
            };
        }

        var dialog = new MessageDialog({
            title : message["confirmTitle"],
            iconClass : "confirmIcon",
            content : content,
            isShowCancelButton : true,
            doOk : doOk,
            doCancel : doCancel
        });
        aspect.before(dialog, "onCancel", doCancel);

        dialog.show();
    }

    return {
        alert : alert,
        error : error,
        confirm : confirm
    };
});