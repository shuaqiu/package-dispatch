define([ "require", "dojo/_base/xhr", "dijit/registry" ], function(require, xhr, registry) {

    var MonitorHandlers = {
        "NEW_ORDER" : function(result) {
            // for all user, add a new item to order list
            require([ "./order/orderlistmonitor" ], function(monitor) {
                monitor.newOrder(result);
            });

            if (result.monitorUser.type == 1 && result.permit) {
                require([ "./order/schedulemonitor" ], function(monitor) {
                    monitor.doNotify(result);
                    // registry a destroy method.
                    destroyers["schedule"] = function() {
                        monitor.stopMonitor();
                    };
                });
            }
        },

        "HANDLE_ORDER" : function(result) {
            // for all user, update the order list
            require([ "./order/orderlistmonitor" ], function(monitor) {
                monitor.handleOrder(result);
            });

            // for all user, update the handle detail in the view page if it is opening
            require([ "./order/orderviewmonitor" ], function(monitor) {
                monitor.changeButton(result);
                monitor.addHandleDetail(result);
            });
        },

        "SCHEDULE_ORDER" : function(result) {
            // for all user, update the order list.
            require([ "./order/orderlistmonitor" ], function(monitor) {
                monitor.handleOrder(result);
            });

            // for all user, update the order list.
            if (result.monitorUser.type == 1) {
                require([ "./order/schedulemonitor" ], function(monitor) {
                    monitor.removeItem(result.order["id"]);
                });
            }

            // for self user, update the schedule detail in order view.
            require([ "./order/orderviewmonitor" ], function(monitor) {
                monitor.changeButton(result);

                if (result.monitorUser.type != 1) {
                    // user not in self company who can't view the schedule information.
                    return;
                }

                monitor.replaceScheduleDetail(result);
                monitor.replaceScheduleHistory(result);
            });
        }
    };

    var destroyers = {};

    return {
        resourceUrl : "monitor",

        monitorDeferred : null,

        doMonitor : function() {
            this.monitorDeferred = xhr.get({
                url : this.resourceUrl,
                content : {
                    t : new Date().getTime()
                },
                handleAs : "json"
            });
            var self = this;
            this.monitorDeferred.then(function(result) {
                if (result.ok) {
                    try {
                        var handler = MonitorHandlers[result.monitorType];
                        if (handler) {
                            handler(result);
                        }
                    } catch (e) {
                    }
                } else {
                    if (result.errCode == "NOT_LOGINED") {
                        return;
                    }
                    if (result.errCode == "NOT_PERMISSION") {
                        // return;
                    }
                }
                self.doMonitor();
            });
        },

        stopMonitor : function() {
            if (this.monitorDeferred) {
                try {
                    this.monitorDeferred.cancel();
                    delete this.monitorDeferred;
                } catch (e) {
                }
            }

            for ( var type in destroyers) {
                destroyers[type]();
            }
        }
    };
});