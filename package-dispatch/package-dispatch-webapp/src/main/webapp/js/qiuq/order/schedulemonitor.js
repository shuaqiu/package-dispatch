define([
        "require",
        "dojo/dom",
        "dojo/dom-construct",
        "dojo/string",
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "../PopupTip",
        "dojo/i18n!./nls/schedulemonitor" ], function(require, dom, domconstruct, string, lang, xhr, registry,
        PopupTip, message) {

    return lang.mixin({}, PopupTip, {
        resourceUrl : "web/schedule",

        monitorDeferred : null,

        title : message["title"],
        audioName : "schedule",
        popupProp : {
            className : "dijitDialog popupTip",
            style : {
                width : "300px",
                right : "400px"
            }
        },

        doMonitor : function() {
            this.monitorDeferred = xhr.get({
                url : this.resourceUrl + "/monitor",
                content : {
                    t : new Date().getTime()
                },
                handleAs : "json"
            });
            var self = this;
            this.monitorDeferred.then(function(result) {
                if (result.ok) {
                    self.popup([ result.obj ]);

                    var resourceGrid = registry.byId("schedule_list");
                    if (resourceGrid) {
                        resourceGrid._onQuery();
                    }
                } else {
                    if (result.errCode == "NOT_LOGINED") {
                        return;
                    }
                    if (result.errCode == "NOT_PERMISSION") {
                        return;
                    }
                }
                self.doMonitor();
            });
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
            if (this.monitorDeferred) {
                try {
                    this.monitorDeferred.cancel();
                    delete this.monitorDeferred;
                } catch (e) {
                }
            }
        }
    });
});