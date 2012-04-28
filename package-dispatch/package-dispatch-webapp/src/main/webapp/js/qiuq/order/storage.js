define([
        "dojo/string",
        "dojo/_base/xhr",
        "dijit/registry",
        "../widget/MessageDialog",
        "dojo/i18n!./nls/storage",
        "dijit/form/Textarea" ], function(string, xhr, registry, MessageDialog, message) {

    return {
        resourceUrl : "web/storage",
        inBarcode : "storage_in_barcode",
        outHandler : "storage_out_handler",
        outBarcode : "storage_out_barcode",

        doSave : function() {
            var barcodes = registry.byId(this.inBarcode).get("value");
            xhr.post({
                url : this.resourceUrl,
                content : {
                    barcodes : barcodes
                },
                handleAs : "json"
            }).then(function(result) {
                if (result.ok) {
                    MessageDialog.alert(message["inSuccess"]);
                    registry.byId(this.inBarcode).set("value", "");
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
            var barcodes = registry.byId(this.outBarcode).get("value");
            xhr.del({
                url : this.resourceUrl,
                content : {
                    handlerCode : handlerCode,
                    barcodes : barcodes
                },
                handleAs : "json"
            }).then(function(result) {
                if (result.ok) {
                    MessageDialog.alert(message["outSuccess"]);
                    registry.byId(this.outBarcode).set("value", "");
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
        }
    };
});