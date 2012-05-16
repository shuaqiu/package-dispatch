define([
        "dojo/string",
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
        "dijit/form/Textarea" ], function(string, lang, xhr, registry, resource, suggest, ResourceGrid, MessageDialog,
        LoadingDialog, message) {

    var order = lang.mixin({}, resource, suggest, {
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

            var receiverName = registry.byId(form["receiverName"].id).get("value");
            var receiverCompany = registry.byId(form["receiverCompany"].id).get("value");
            if (receiverName == this._selectedItem["name"] && receiverCompany == this._selectedItem["company"]) {
                form["receiverId"].value = this._selectedItem["id"];
            } else {
                form["receiverId"].value = "-1";
            }
        },

        _saved : function(result) {
            lang.hitch(this, resource._saved)(result);

            if (result.ok) {
                this.showPrint(result.obj.key);
            } else {
                this._selectedItem = null;
            }
        },

        _getValidData : function() {
            var form = document.forms[this.editingForm];

            if (string.trim(registry.byId(form["goodsName"].id).get("value")) == ""
                    || string.trim(registry.byId(form["quantity"].id).get("value")) == "") {
                MessageDialog.error(message["err.INVALID"]);
                return null;
            }

            return lang.hitch(this, resource._getValidData)();
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
                    if (result.errCode == "OPERATE_FAIL") {
                        MessageDialog.error(message["err.OPERATE_FAIL.sender"] + result.message);
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
                    if (result.errCode == "OPERATE_FAIL") {
                        MessageDialog.error(message["err.OPERATE_FAIL.receiver"] + result.message);
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
                    if (result.errCode == "OPERATE_FAIL") {
                        MessageDialog.error(message["err.OPERATE_FAIL.receiver"] + result.message);
                        return;
                    }
                    MessageDialog.error(message["err." + result.errCode]);
                }
            });
        },

        showPrint : function(orderId) {
            window.open("web/order/print?orderId=" + orderId, "", "");
        }
    });

    var companySuggestion = lang.mixin({}, suggest, {
        selectionDialog : "receiver_editing_company_dialog",
        selectionStoreTarget : "web/receivercompany",
        selectionStructure : [ {
            name : message["company"],
            field : "name",
            width : "150px"
        }, {
            name : message["address"],
            field : "address",
            width : "250px"
        } ],

        doSelect : function(item) {
            var form = document.forms[order.editingForm];

            registry.byId(form["receiverCompany"].id).set("value", item["name"]);
            var receiverAddress = registry.byId(form["receiverAddress"].id);
            if (receiverAddress.get("value") == "") {
                receiverAddress.set("value", item["address"]);
            }

            this.updateReceiverId(form);
        },

        onCompanyKeyUp : function() {
            var form = document.forms[order.editingForm];
            this.updateReceiverId(form);
        },

        updateReceiverId : function(form) {
            if (order._selectedItem == null) {
                return;
            }

            if (registry.byId(form["receiverCompany"].id).get("value") != order._selectedItem["company"]) {
                form["receiverId"].value = "-1";
            } else if (registry.byId(form["receiverName"].id).get("value") == order._selectedItem["name"]) {
                // selected name is the same as current name value, and selected company name is the same as current
                // company value too.
                form["receiverId"].value = order._selectedItem["id"];
            }
        }
    });

    order.companySuggestion = companySuggestion;

    return order;
});