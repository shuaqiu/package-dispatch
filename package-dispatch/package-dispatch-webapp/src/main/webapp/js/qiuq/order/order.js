define([
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "../resource",
        "../suggest",
        "../widget/ResourceGrid",
        "../widget/MessageDialog",
        "../widget/LoadingDialog",
        "dojo/i18n!./nls/order",
        "dojo/date/locale",
        "dojo/date/stamp",
        "dijit/form/CheckBox",
        "dijit/form/Textarea" ], function(lang, xhr, registry, resource, suggest, ResourceGrid, MessageDialog,
        LoadingDialog, message) {

    return lang.mixin({}, resource, suggest, {
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
            width : "120px"
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
            this._selectedItem = item;
            
            var form = document.forms[this.editingForm];
            form["receiverId"].value = item["id"];
            registry.byId(form["receiverName"].id).set("value", item["name"]);
            registry.byId(form["receiverTel"].id).set("value", item["tel"]);
            registry.byId(form["receiverCompany"].id).set("value", item["company"]);
            registry.byId(form["receiverAddress"].id).set("value", item["address"]);
        },

        _selectedItem : null,

        onReceiverKeyUp : function() {
            if (this._selectedItem == null) {
                return;
            }

            var form = document.forms[this.editingForm];

            var company = registry.byId(form["receiverName"].id).get("value");
            if (company != this._selectedItem["name"]) {
                form["receiverId"].value = "-1";
            } else {
                form["receiverId"].value = this._selectedItem["id"];
            }
        },

        _initForm : function(item) {
            lang.hitch(this, resource._initForm)(item);
            this._selectedItem = {
                id : item.receiverId,
                name : item.receiverName,
                tel : receiverTel,
                company : item.receiverCompany
            };
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
                url : this.resourceUrl + "/" + orderId + "/identity/sender",
                handleAs : "json"
            }).then(function(result) {
                dialog.hide();

                if (result.ok) {
                    MessageDialog.alert(message["resendIdentityToSenderSucc"]);
                } else {
                    if(result.errCode == "OPERATE_FAIL"){
                        MessageDialog.error(message["err.OPERATE_FAIL.sender"]);
                        return;
                    }
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        },

        resendIdentityToReceiver : function(orderId) {
            var dialog = new LoadingDialog({});
            dialog.show();
            
            xhr.get({
                url : this.resourceUrl + "/" + orderId + "/identity/receiver",
                handleAs : "json"
            }).then(function(result) {
                dialog.hide();
                
                if (result.ok) {
                    MessageDialog.alert(message["resendIdentityToReceiverSucc"]);
                } else {
                    if(result.errCode == "OPERATE_FAIL"){
                        MessageDialog.error(message["err.OPERATE_FAIL.receiver"]);
                        return;
                    }
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        },

        regenerateReceiverIdentity : function(orderId) {
            var dialog = new LoadingDialog({});
            dialog.show();
            
            xhr.put({
                url : this.resourceUrl + "/" + orderId + "/identity/receiver",
                handleAs : "json"
            }).then(function(result) {
                dialog.hide();
                
                if (result.ok) {
                    MessageDialog.alert(message["regenerateReceiverIdentitySucc"]);
                } else {
                    if(result.errCode == "OPERATE_FAIL"){
                        MessageDialog.error(message["err.OPERATE_FAIL.receiver"]);
                        return;
                    }
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        }
    });
});