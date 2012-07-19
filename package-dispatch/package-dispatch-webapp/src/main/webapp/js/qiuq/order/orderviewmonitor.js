define([ "require", "dojo/dom", "dojo/dom-construct", "dojo/date/locale", "dijit/registry", "dijit/MenuBarItem" ],
        function(require, dom, domconstruct, locale, registry, MenuBarItem) {

            return {
                idPrefix : {
                    handleDetails : "order_handle_details_",
                    scheduleDetails : "order_schedule_details_",
                    scheduleHistories : "order_schedule_history_",

                    scheduleButton : "order_schedule_button_",
                    closeButton : "order_close_button_",
                    cancelButton : "order_cancel_button_",

                    operationBar : "order_operationbar_"
                },

                isViewing : function(orderId) {
                    return this.getOrderHandleDetails(orderId) != null;
                },

                getOrderHandleDetails : function(orderId) {
                    return dom.byId(this.idPrefix.handleDetails + orderId);
                },

                formatDate : function(time) {
                    var d = new Date();
                    d.setTime(time);
                    return locale.format(d, {
                        datePattern : 'yyyy-MM-dd',
                        timePattern : 'HH:mm:ss'
                    });
                },

                addHandleDetail : function(result) {
                    var order = result.order;
                    var orderHandleDetails = this.getOrderHandleDetails(order["id"]);
                    if (orderHandleDetails == null) {
                        // didn't open the view page.
                        return;
                    }

                    var detail = result.detail;

                    // @see order/view.jsp
                    domconstruct.create("div", {
                        className : "arrow"
                    }, orderHandleDetails);

                    var html = "";
                    if (result.monitorUser.type == 1) {
                        // user in self company who can view the handler information.
                        html += "<div>" + detail["handlerName"] + "(" + detail["handlerTel"] + ")</div>";
                    }
                    html += "<div>" + detail["description"] + "</div>";
                    html += "<div>" + this.formatDate(detail["handleTime"]) + "</div>";

                    domconstruct.create("div", {
                        className : "detail",
                        innerHTML : html
                    }, orderHandleDetails);
                },

                replaceScheduleDetail : function(result) {
                    var order = result.order;

                    var orderHandleDetails = this.getOrderHandleDetails(order["id"]);
                    if (orderHandleDetails == null) {
                        // didn't open the view page.
                        return;
                    }

                    var scheduleDetailsId = this.idPrefix.scheduleDetails + order["id"];
                    var scheduleDetails = dom.byId(scheduleDetailsId);
                    if (scheduleDetails) {
                        domconstruct.destroy(scheduleDetails);
                    }

                    var html = [];
                    html.push('<div class="desc">调度信息</div>');
                    html.push('<div class="detail">');
                    html.push('  <div>' + order["senderName"] + '(' + order["senderTel"] + ')</div>');
                    html.push('  <div>' + order["senderAddress"] + '</div>');
                    html.push('</div>');

                    for ( var i = 0; i < result.detail["scheduleDetail"].length; i++) {
                        var detail = result.detail["scheduleDetail"][i];

                        html.push('<div class="arrow"></div>');
                        html.push('<div class="detail">');
                        html.push('  <div>&nbsp;</div>');
                        html.push('  <div>' + detail["handlerName"] + '(' + detail["handlerTel"] + ')</div>');
                        html.push('  <div>&nbsp;</div>');
                        html.push('</div>');
                    }

                    html.push('<div class="arrow"></div>');
                    html.push('<div class="detail">');
                    html.push('  <div>' + order["receiverName"] + '(' + order["receiverTel"] + ')</div>');
                    html.push('  <div>' + order["receiverAddress"] + '</div>');
                    html.push('</div>');

                    domconstruct.create("div", {
                        id : scheduleDetailsId,
                        className : "details schedule",
                        innerHTML : html.join("")
                    }, orderHandleDetails, "after");
                },

                replaceScheduleHistory : function(result) {
                    var order = result.order;
                    if (!this.isViewing(order["id"])) {
                        // didn't open the view page.
                        return;
                    }

                    var scheduleDetailsId = this.idPrefix.scheduleDetails + order["id"];
                    var scheduleDetails = dom.byId(scheduleDetailsId);
                    if (scheduleDetails == null) {
                        // didn't open the view page.
                        return;
                    }

                    var scheduleHistoriesId = this.idPrefix.scheduleHistories + order["id"];
                    var scheduleHistories = dom.byId(scheduleHistoriesId);
                    if (scheduleHistories) {
                        domconstruct.destroy(scheduleHistories);
                    }

                    var html = [];
                    html.push('<div class="desc">调度历史</div>');

                    for ( var i = 0; i < result.detail["scheduleHistory"].length; i++) {
                        var detail = result.detail["scheduleHistory"][i];
                        html.push('<div class="detail">');
                        html.push('  <div>' + detail["schedulerName"] + '(' + detail["schedulerTel"] + ')</div>');
                        html.push("  <div>" + this.formatDate(detail["scheduleTime"]) + "</div>");
                        html.push('</div>');
                        html.push('<div class="arrow" style="background-image: none; height: 10px;"></div>');
                    }

                    domconstruct.create("div", {
                        id : scheduleHistoriesId,
                        className : "details schedule",
                        innerHTML : html.join("")
                    }, scheduleDetails, "after");
                },

                changeButton : function(result) {
                    var order = result.order;

                    if (order["state"] >= 6) {
                        var scheduleButton = registry.byId(this.idPrefix.scheduleButton + order["id"]);
                        if (scheduleButton != null) {
                            scheduleButton.destroyRecursive();
                        }
                        var closeButton = registry.byId(this.idPrefix.closeButton + order["id"]);
                        if (closeButton != null) {
                            closeButton.destroyRecursive();
                        }
                        var cancelButton = registry.byId(this.idPrefix.cancelButton + order["id"]);
                        if (cancelButton != null) {
                            cancelButton.destroyRecursive();
                        }
                        return;
                    }

                    if (result.monitorUser.type == 1) {
                        var scheduleButton = registry.byId(this.idPrefix.scheduleButton + order["id"]);
                        if (scheduleButton != null) {
                            scheduleButton.set("label", "修改调度");
                        }

                        var closeButton = registry.byId(this.idPrefix.closeButton + order["id"]);
                        var operationBar = registry.byId(this.idPrefix.operationBar + order["id"]);
                        if (closeButton == null && operationBar != null) {
                            closeButton = new MenuBarItem({
                                id : this.idPrefix.closeButton + order["id"],
                                onClick : function() {
                                    require([ './order' ], function(orderfunc) {
                                        orderfunc.close(order["id"]);
                                    });
                                },
                                label : "关闭订单"
                            }).placeAt(operationBar);
                        }
                    } else {
                        if (order["state"] >= 2) { // after got, can't cancel the order
                            var cancelButton = registry.byId(this.idPrefix.cancelButton + order["id"]);
                            if (cancelButton != null) {
                                cancelButton.destroyRecursive();
                            }
                        }
                    }
                }
            };
        });