define([
        "require",
        "dojo/dom",
        "dojo/dom-construct",
        "dojo/string",
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "../PopupTip",
        "dojo/i18n!./nls/alarm" ],
        function(require, dom, domconstruct, string, lang, xhr, registry, PopupTip, message) {

            return lang.mixin({}, PopupTip, {
                checkInterval : 60 * 1000,
                timeout : null,
                title : message["title"],
                audioName : "alarm",

                startCheck : function() {
                    var self = this;
                    xhr.get({
                        url : "web/alarm/new",
                        content : {
                            // add a time parameter to prevent cache
                            t : new Date().getTime()
                        },
                        handleAs : "json"
                    }).then(function(result) {
                        if (result.ok) {
                            self.popup(result.obj);

                            var resourceGrid = registry.byId("alarm_list");
                            if (resourceGrid) {
                                resourceGrid._onQuery();
                            }
                        } else {
                            if (result.errCode == "NOT_LOGINED") {
                                require([ "qiuq/login" ], function(login) {
                                    login.reLogin();
                                });
                            }
                        }
                    });

                    self.timeout = setTimeout(function() {
                        self.startCheck();
                    }, self.checkInterval);
                },

                _createItem : function(aOrder) {
                    var itemId = "alarm_item_" + aOrder["id"];
                    var sameIdItem = dom.byId(itemId);
                    if (sameIdItem) {
                        this._itemContainer.removeChild(sameIdItem);
                    }

                    var expentTime, desc, style;
                    if (aOrder["state"] == 1) {
                        // just scheduled
                        expentTime = parseInt((new Date().getTime() - aOrder["scheduleTime"]) / 60 / 1000);
                        desc = string.substitute(message["desc.notfetch"], {
                            senderName : aOrder["senderName"] || "",
                            senderTel : aOrder["senderTel"] || "",
                            receiverName : aOrder["receiverName"] || "",
                            receiverTel : aOrder["receiverTel"] || "",
                            expentTime : expentTime + ""
                        });
                        style = {
                            "color" : "blue"
                        };
                    } else {
                        expentTime = parseInt((new Date().getTime() - aOrder["fetchTime"]) / 60 / 1000);
                        desc = string.substitute(message["desc.notdelivered"], {
                            barCode : aOrder["barCode"] || "",
                            senderName : aOrder["senderName"] || "",
                            senderTel : aOrder["senderTel"] || "",
                            receiverName : aOrder["receiverName"] || "",
                            receiverTel : aOrder["receiverTel"] || "",
                            expentTime : expentTime + ""
                        });
                        style = {
                            "color" : expentTime >= 60 ? "red" : "#FF6200"
                        };
                    }

                    var self = this;
                    domconstruct.create("li", {
                        id : itemId,
                        innerHTML : desc,
                        style : style,
                        title : message["itemTip"],
                        ondblclick : function() {
                            self._itemContainer.removeChild(this);
                        },
                        onclick : function() {
                            require([ 'qiuq/order/order' ], function(resource) {
                                resource.doView(aOrder["id"]);
                            });
                        }
                    }, this._itemContainer);
                },

                destroy : function() {
                    this.removePopup();
                    clearTimeout(this.timeout);
                }

            });
        });