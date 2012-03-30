define([
        "dojo/_base/xhr",
        "dojo/dom-form",
        "dojo/store/Memory",
        "dojo/_base/Deferred",
        "dijit/registry",
        "dijit/Dialog",
        "../widget/MessageDialog",
        "../ErrCode",
        "dojo/i18n!./nls/order",
        "dijit/form/Form",
        "dijit/form/ValidationTextBox",
        "dijit/form/Button",
        "dijit/form/CheckBox",
        "dijit/form/ComboBox",
        "dojo/data/ObjectStore",
        "dojo/store/JsonRest",
        "dojox/grid/DataGrid" ], function(xhr, domform, Memory, Deferred, registry, Dialog, MessageDialog, ErrCode,
        message) {

    var id = {
        form : "order_creation",
        receiverDialog : "order_new_receiver_dialog"
    };

    function save() {
        var form = document.forms["order"];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            MessageDialog.error(message["err." + ErrCode.INVALID]);
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
            dialog = new Dialog({
                "id" : id.receiverDialog,
                "title" : message["receiver"],
                "href" : "web/order/receiver"
            });
        }
        dialog.show();
    }

    function selectReceiver(item) {
        var form = document.forms[id.form];
        form["receiverName"].value = item["name"];
        form["receiverTel"].value = item["tel"];
        form["receiverAddress"].value = item["address"];
        form["receiverCompany"].value = item["company"] + item["department"];

        registry.byId(id.companyDialog).hide();
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