define([
        "dojo/_base/xhr",
        "dojo/dom-form",
        "dojo/_base/Deferred",
        "dijit/registry",
        "dijit/Dialog",
        "../widget/MessageDialog",
        "../widget/DataSelectionDialog",
        "dojo/i18n!./nls/order",
        "dijit/form/Form",
        "dijit/form/ValidationTextBox",
        "dijit/form/Button",
        "dijit/form/CheckBox",
        "dijit/form/ComboBox",
        "dojo/data/ObjectStore",
        "dojo/store/JsonRest",
        "dojox/grid/DataGrid" ], function(xhr, domform, Deferred, registry, Dialog, MessageDialog, DataSelectionDialog,
        message) {

    var id = {
        form : "order_creation",
        receiverDialog : "order_new_receiver_dialog"
    };

    function save() {
        var form = document.forms[id.form];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            MessageDialog.error(message["err.INVALID"]);
            return;
        }

        xhr.post({
            "url" : "web/order",
            "postData" : domform.toJson(form),
            "handleAs" : "json",
            "contentType" : "application/json"
        }).then(function(result) {
            if (result.ok) {

            } else {
                MessageDialog.error(message["err." + result.errCode]);
            }
        });
    }

    function showReceiver() {
        var dialog = registry.byId(id.receiverDialog);
        if (!dialog) {
            dialog = new DataSelectionDialog({
                id : id.receiverDialog,
                storeTarget : 'web/receiver/',
                structure : [ {
                    name : "姓名",
                    field : "name",
                    width : "100px"
                }, {
                    name : "电话",
                    field : "tel",
                    width : "100px"
                }, {
                    name : "公司",
                    field : "company",
                    width : "200px"
                }, {
                    name : "地址",
                    field : "address",
                    width : "200px"
                } ],
                onRowClick : function(item, idx) {
                    selectReceiver(item);
                }
            });
        }
        dialog.show();
    }

    function selectReceiver(item) {
        var form = document.forms[id.form];
        registry.byId(form["receiverName"].id).set("value", item["name"]);
        registry.byId(form["receiverTel"].id).set("value", item["tel"]);
        registry.byId(form["receiverAddress"].id).set("value", item["address"]);
        registry.byId(form["receiverCompany"].id).set("value", item["company"]);
    }

    function showSuggestReceiver(obj) {
        var value = registry.byId(obj.id).get("value");
        var reg = new RegExp(".*" + value + ".*");
        initReceiverTable({
            "name" : reg
        }, false).then(function(html) {
            new Dialog({
                "title" : "Receiver",
                "content" : html,
                style : {
                    width : "600px",
                    height : "400px"
                }
            }).show();
        });
    }

    return {
        "save" : save,
        "showReceiver" : showReceiver,
        "showSuggestReceiver" : showSuggestReceiver
    };
});