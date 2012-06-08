define([
        "dojo/dom-construct",
        "dojo/string",
        "dojo/json",
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "dijit/form/Textarea",
        "../resource",
        "../suggest",
        "../widget/ResourceGrid",
        "../widget/MessageDialog",
        "../widget/LoadingDialog",
        "dojo/i18n!./nls/order",
        "dojo/date/locale",
        "dojo/date/stamp",
        "dijit/form/CheckBox",
        "dijit/form/Select" ], function(domconstruct, string, json, lang, xhr, registry, Textarea, resource, suggest,
        ResourceGrid, MessageDialog, LoadingDialog, message) {

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
            var self = this;
            var timeout = setTimeout(function() {
                self.doView(orderId);
            }, 15 * 1000);
            showTab([], {
                title : this.viewTabName,
                href : this.resourceUrl + "/view/" + orderId,
                onClose : function() {
                    clearTimeout(timeout);
                    return true;
                }
            }, this.viewTab, true);
        },

        resendIdentityToSender : function(orderId) {
            var dialog = new LoadingDialog({});
            dialog.show();

            xhr.get({
                url : this.resourceUrl + "/" + orderId + "/identity/sender",
                content : {
                    // add a time parameter to prevent cache
                    t : new Date().getTime()
                },
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
                content : {
                    // add a time parameter to prevent cache
                    t : new Date().getTime()
                },
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
        },

        cancel : function(orderId) {
            var self = this;
            MessageDialog.confirm(message["cancelConfirm"], function() {
                self.doCancel(orderId);
            });
        },

        doCancel : function(orderId) {
            var dialog = new LoadingDialog({});
            dialog.show();

            var canceledOrClosed = lang.hitch(this, this.canceledOrClosed);
            xhr.put({
                url : this.resourceUrl + "/cancel/" + orderId,
                handleAs : "json"
            }).then(function(result) {
                dialog.hide();
                canceledOrClosed(result, orderId);
            });
        },

        close : function(orderId) {
            var div = domconstruct.create("div", {
                innerHTML : "<div>" + message["closeReason"] + "</div>"
            });
            var reasonInput = new Textarea({
                style : {
                    width : "300px"
                }
            }).placeAt(div);

            var self = this;
            MessageDialog.confirm(div, function() {
                var reason = reasonInput.get("value");
                if (string.trim(reason) == "") {
                    MessageDialog.error(message["err.NULL_CLOSE_RESON"]);
                    return false;
                }

                self.doClose(orderId, reason);
            });
        },

        doClose : function(orderId, reason) {
            var dialog = new LoadingDialog({});
            dialog.show();

            var canceledOrClosed = lang.hitch(this, this.canceledOrClosed);
            xhr.put({
                url : this.resourceUrl + "/close/" + orderId,
                putData : json.stringify({
                    reason : reason
                }),
                handleAs : "json",
                contentType : "application/json"
            }).then(function(result) {
                dialog.hide();
                canceledOrClosed(result, orderId);
            });
        },

        canceledOrClosed : function(result, orderId) {
            if (result.ok) {
                this.doView(orderId);
                var grid = registry.byId(this.listGrid);
                if (grid) {
                    grid._refresh(true);
                }
            } else {
                if (result.errCode == "NOT_LOGINED") {
                    require([ "qiuq/login" ], function(login) {
                        login.reLogin();
                    });
                    return;
                }
                MessageDialog.error(message["err." + result.errCode]);
            }
        }
    });
});