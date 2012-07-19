define([
        "require",
        "dojo/dom",
        "dojo/dom-construct",
        "dojo/string",
        "dojo/_base/lang",
        "dijit/registry",
        "../PopupTip",
        "dojo/i18n!./nls/schedulemonitor" ], function(require, dom, domconstruct, string, lang, registry, PopupTip,
        message) {

    return lang.mixin({}, PopupTip, {

        title : message["title"],
        audioName : "schedule",
        popupProp : {
            className : "dijitDialog popupTip",
            style : {
                width : "300px",
                right : "300px"
            }
        },

        doNotify : function(result) {
            this.popup([ result.order ]);

            var resourceGrid = registry.byId("schedule_list");
            if (resourceGrid) {
                resourceGrid._grid.store.newItem(result.order);
            }
        },

        _createItem : function(aOrder) {
            var desc = string.substitute(message["desc"], {
                senderName : aOrder["senderName"] || "",
                senderTel : aOrder["senderTel"] || "",
                receiverName : aOrder["receiverName"] || "",
                receiverTel : aOrder["receiverTel"] || ""
            });

            var self = this;
            domconstruct.create("li", {
                id : "schedule_item_" + aOrder["id"],
                innerHTML : desc,
                title : message["itemTip"],
                onclick : function() {
                    require([ 'qiuq/order/schedule' ], function(resource) {
                        resource.doModify(aOrder["id"]);
                    });
                }
            }, this._itemContainer);
        },

        removeItem : function(itemId) {
            if (this._itemContainer && dom.byId(this._itemContainer)) {
                var item = dom.byId("schedule_item_" + itemId);
                if (item) {
                    this._itemContainer.removeChild(item);
                }
            }
        },

        stopMonitor : function() {
            this.removePopup();
        }
    });
});