define([ "dojo/dom-construct", "dojo/string", "dojo/_base/xhr", "dojo/i18n!./nls/alarm", "../widget/ResourceGrid" ],
        function(domconstruct, string, xhr, message) {

            return {
                startCheck : function() {
                    var self = this;
                    xhr.get({
                        url : "web/alarm/new",
                        handleAs : "json"
                    }).then(function(list) {
                        self._showAlarmPopup(list);
                        setTimeout(function() {
                            self.startCheck();
                        }, 60 * 1000);
                    });
                },

                _showAlarmPopup : function(list) {
                    if (list == null || list.length == 0) {
                        return;
                    }

                    var ul = document.getElementById("alarmList");
                    if (ul == null) {
                        ul = this._createContainer();
                    }

                    for ( var i = 0; i < list.length; i++) {
                        var aOrder = list[i];
                        var itemId = "alarm_list_" + aOrder["id"];
                        var sameIdItem = document.getElementById(itemId);
                        if (sameIdItem) {
                            ul.removeChild(sameIdItem);
                        }

                        var expentTime = parseInt((new Date().getTime() - aOrder["fetchTime"]) / 60 / 1000);
                        var desc = string.substitute(message["desc"], {
                            barCode : aOrder["barCode"],
                            senderName : aOrder["senderName"],
                            senderTel : aOrder["senderTel"],
                            receiverName : aOrder["receiverName"],
                            receiverTel : aOrder["receiverTel"],
                            expentTime : expentTime + ""
                        });
                        var color = expentTime >= 60 ? "red" : "yello";
                        domconstruct.create("li", {
                            id : itemId,
                            innerHTML : desc,
                            style : {
                                "color" : color
                            }
                        }, ul);
                    }
                },

                _createContainer : function() {
                    var div = domconstruct.create("div", {
                        className : "dijitDialog",
                        style : {
                            "position" : "absolute",
                            "top" : 0,
                            "right" : 0,
                            "z-index" : 1000,
                            "width" : "400px",
                            "background" : "#FFF"
                        }
                    }, document.body);

                    var title = domconstruct.create("div", {
                        className : "dijitDialogTitleBar",
                        style : {
                            cursor : "default"
                        }
                    }, div);

                    domconstruct.create("span", {
                        className : "dijitDialogTitle",
                        innerHTML : message["title"]
                    }, title);
                    domconstruct.create("span", {
                        className : "dijitDialogCloseIcon",
                        innerHTML : "<span class='closeText'>x</span>",
                        click : function() {
                            document.body.removeChild(div);
                        }
                    }, title);

                    var ul = domconstruct.create("ul", {
                        id : "alarmList",
                    }, div);

                    return ul;
                }
            };
        });