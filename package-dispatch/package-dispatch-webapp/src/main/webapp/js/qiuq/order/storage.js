define([
        "require",
        "dojo/string",
        "dojo/_base/xhr",
        "dijit/registry",
        "../widget/MessageDialog",
        "dojo/i18n!./nls/storage",
        "dijit/form/Textarea" ], function(require, string, xhr, registry, MessageDialog, message) {

    return {
        resourceUrl : "web/storage",
        inBarcode : "storage_in_barcode",
        outHandler : "storage_out_handler",
        outBarcode : "storage_out_barcode",

        doSave : function() {
            var barcodes = registry.byId(this.inBarcode).get("value");
            if (string.trim(barcodes) == "") {
                MessageDialog.error(message["err.NULL_BARCODE"]);
                return;
            }

            var self = this;
            xhr.post({
                url : this.resourceUrl,
                content : {
                    barcodes : barcodes
                },
                handleAs : "json"
            }).then(function(result) {
                if (result.ok) {
                    MessageDialog.alert(self._createMessage(result, message["inSuccess"]));
                    registry.byId(self.inBarcode).set("value", "");
                } else {
                    if (result.errCode == "NOT_LOGINED") {
                        require([ "qiuq/login" ], function(login) {
                            login.reLogin();
                        });
                        return;
                    }
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        },

        doDelete : function() {
            var handlerCode = registry.byId(this.outHandler).get("value");
            if (string.trim(handlerCode) == "") {
                MessageDialog.error(message["err.NULL_HANDLER_CODE"]);
                return;
            }
            var barcodes = registry.byId(this.outBarcode).get("value");
            if (string.trim(barcodes) == "") {
                MessageDialog.error(message["err.NULL_BARCODE"]);
                return;
            }

            var self = this;
            xhr.del({
                url : this.resourceUrl,
                content : {
                    handlerCode : handlerCode,
                    barcodes : barcodes
                },
                handleAs : "json"
            }).then(function(result) {
                if (result.ok) {
                    MessageDialog.alert(self._createMessage(result, message["outSuccess"]));
                    registry.byId(self.outBarcode).set("value", "");
                } else {
                    if (result.errCode == "NOT_LOGINED") {
                        require([ "qiuq/login" ], function(login) {
                            login.reLogin();
                        });
                        return;
                    }
                    if (result.errCode == "NOT_FOUND") {
                        MessageDialog.error(string.substitute(message["err.NOT_FOUND"], [ handlerCode ]));
                        return;
                    }
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        },

        _createMessage : function(result, okMsg) {
            var msg = [];
            if (result.obj.OK) {
                msg.push(okMsg + ": " + result.obj.OK.join(", "));
            }
            if (result.obj.INVALID) {
                msg.push(message["invalidBarcode"] + ": " + result.obj.INVALID.join(", "));
            }
            if (result.obj.NOT_CORRECT) {
                msg.push(message["orderStateIncorrect"] + ": " + result.obj.NOT_CORRECT.join(", "));
            }
            if (result.obj.UPDATE_FAIL) {
                msg.push(message["updateFail"] + ": " + result.obj.UPDATE_FAIL.join(", "));
            }
            return msg.join("<br/>");
        }
    };
});