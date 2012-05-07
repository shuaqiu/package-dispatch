define([
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "../resource",
        "../selection",
        "../widget/ResourceGrid",
        "../widget/MessageDialog",
        "../widget/LoadingDialog",
        "dojo/i18n!./nls/order",
        "dojo/date/locale",
        "dojo/date/stamp",
        "dijit/form/CheckBox",
        "dijit/form/Textarea" ], function(lang, xhr, registry, resource, selection, ResourceGrid, MessageDialog,
        LoadingDialog, message) {

    return lang.mixin({}, resource, selection, {
        resourceUrl : "web/order",
        listGrid : "order_list_grid",

        viewTabName : message["view"],
        createTabName : message["create"],
        modifyTabName : message["modify"],
        viewTab : "order_view_tab",
        editingTab : "order_editing_tab",
        editingForm : "order_editing_form",

        selectionDialog : "order_editing_receiver_dialog",
        selectionStoreTarget : "web/receiver/",
        selectionStructure : [ {
            name : message["name"],
            field : "name",
            width : "100px"
        }, {
            name : message["tel"],
            field : "tel",
            width : "100px"
        }, {
            name : message["company"],
            field : "company",
            width : "200px"
        }, {
            name : message["address"],
            field : "address",
            width : "200px"
        } ],

        doSelect : function(item) {
            var form = document.forms[this.editingForm];
            form["receiverId"].value = item["id"];
            registry.byId(form["receiverName"].id).set("value", item["name"]);
            registry.byId(form["receiverTel"].id).set("value", item["tel"]);
            registry.byId(form["receiverCompany"].id).set("value", item["company"]);
            registry.byId(form["receiverAddress"].id).set("value", item["address"]);
        },

        doView : function(orderId) {
            showTab([], {
                title : this.viewTabName,
                href : this.resourceUrl + "/view/" + orderId,
            }, this.viewTab, true);
        },

        resendIdentityToSender : function(orderId) {
            var dialog = new LoadingDialog({});
            dialog.show();

            xhr.get({
                url : this.resourceUrl + "/" + orderId + "/identity/sender"
            }).then(function(result) {
                dialog.hide();

                if (result.ok) {
                    MessageDialog.alert(message["resendIdentityToSenderSucc"]);
                } else {
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        },

        resendIdentityToReceiver : function(orderId) {
            var dialog = new LoadingDialog({});
            dialog.show();
            
            xhr.get({
                url : this.resourceUrl + "/" + orderId + "/identity/receiver"
            }).then(function(result) {
                dialog.hide();
                
                if (result.ok) {
                    MessageDialog.alert(message["resendIdentityToReceiverSucc"]);
                } else {
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        },

        regenerateReceiverIdentity : function(orderId) {
            var dialog = new LoadingDialog({});
            dialog.show();
            
            xhr.put({
                url : this.resourceUrl + "/" + orderId + "/identity/receiver"
            }).then(function(result) {
                dialog.hide();
                
                if (result.ok) {
                    MessageDialog.alert(message["regenerateReceiverIdentitySucc"]);
                } else {
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        }
    });
    //
    // function showSuggestReceiver(obj) {
    // var value = registry.byId(obj.id).get("value");
    // var reg = new RegExp(".*" + value + ".*");
    // initReceiverTable({
    // "name" : reg
    // }, false).then(function(html) {
    // new Dialog({
    // "title" : "Receiver",
    // "content" : html,
    // style : {
    // width : "600px",
    // height : "400px"
    // }
    // }).show();
    // });
    // }
});