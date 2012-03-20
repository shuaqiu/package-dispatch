define([ "dojo/declare", "dijit/_WidgetBase", "dijit/_TemplatedMixin", "dijit/Dialog",
        "dojo/text!./templates/MsgDialog.html" ], function(declare, _WidgetBase, _TemplatedMixin, Dialog, template) {
    var MsgDialog = declare("qiuq.widget.MsgDialog", [ _WidgetBase, _TemplatedMixin ], {

        templateString : template,

        winTypes : null,

        superDom : null,

        dialog : null,

        // Flag to protect against accidental multi-startups.
        _started : false,

        postCreate : function() {
            this.winTypes = ({
                NEW : "NEW",
                VIEW : "VIEW",
                UPDATE : "UPDATE"
            });
        },
        startup : function() {
            if (!this._started) {
                this._started = true;
            }
            var dialogParms = {
                title : "系统提示",
                draggable : false,
                content : this._dlgNode
            };
            this.dialog = new Dialog(dialogParms);
            this.setStatus("error");
            this.show();
            this.close();
        },
        setStatus : function(status) {
            var self = this;
            var style = null;
            if (status == "ok") {
                style = {
                    "background" : "url(../../images/alert_ok.png) 25px 40px no-repeat"
                };
                self.setButtons(status);
            } else if (status == "error") {
                style = {
                    "background" : "url(../../images/alert_error.png) 25px 40px no-repeat"
                };
                self.setButtons(status);
            } else if (status == "cue") {
                style = {
                    "background" : "url(../../images/alert_cue.png) 25px 40px no-repeat"
                };
                self.setButtons(status);
            } else if (status == "ask") {
                style = {
                    "background" : "url(../../images/alert_ask.png) 25px 40px no-repeat"
                };
                self.setButtons(status);
            } else if (status == "networkError") {
                style = {
                    "background" : "url(../../images/alert_error.png) 25px 40px no-repeat"
                };
                self.setButtons(status);
            }
            dojo.query(".ok").style(style);
        },
        setDesc : function(desc) {
            this._msgOperDesc.innerHTML = desc;
        },
        setContent : function(content) {
            this._msgOperDetail.innerHTML = content;
        },
        setClosable : function(closable) {
            this.closable = closable;
            if (closable) {
                dojo.style(this.dialog.closeText, "display", "block");
            } else {
                dojo.style(this.dialog.closeText, "display", "none");
            }
        },
        setButtons : function(status) {
            // clear buttonNodes, stop from create button
            this._buttonNodes.innerHTML = "";
            if (status == "ok" || status == "cue" || status == "error") {
                var buttons = [];
                if (this.closable) {
                    buttons.push('CLOSE');
                } else {
                    buttons.push('FORCE_LOGOUT');
                }
                HmsCom.createButton({
                    buttons : buttons,
                    buttonNodes : this._buttonNodes,
                    widgetDom : this
                });
            } else if (status == "ask") {
                var buttons = [];
                buttons.push('CONFIRM');
                buttons.push('CANCEL_NO_ROLLBACK');
                HmsCom.createButton({
                    buttons : buttons,
                    buttonNodes : this._buttonNodes,
                    widgetDom : this
                });
            } else if (status == "networkError") {
            }
        },
        setTemplateString : function(templateString) {
            this.templateString = templateString;
        },
        setTitle : function(title) {
            this.dialog.set("title", title);
        },
        setMsgContent : function(data) {
            this._msgNode.innerHTML = data;
        },
        setSuperDom : function(superDom) {
            this.superDom = superDom;
        },
        show : function() {
            this.dialog.show();
        },
        close : function() {
            this.dialog.onCancel();
        }
    });

    return MsgDialog;
});
