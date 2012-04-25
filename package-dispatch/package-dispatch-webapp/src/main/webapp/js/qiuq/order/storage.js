define([ "dojo/_base/xhr", "dijit/registry", "dijit/form/Textarea" ], function(xhr, registry) {

    return {
        resourceUrl : "web/storage",
        inBarcode : "storage_in_barcode",
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
                    MessageDialog.alert(message["success"]);
                    registry.byId(this.inBarcode).set("value", "");
                } else {
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        },

        doDelete : function() {
            var barcodes = registry.byId(this.inBarcode).get("value");
            xhr.del({
                url : this.resourceUrl,
                content : {
                    barcodes : barcodes
                },
                handleAs : "json"
            }).then(function(result) {
                if (result.ok) {

                } else {
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        }
    };
});