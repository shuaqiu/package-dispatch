define([
        "dojo/dom",
        "dojo/dom-construct",
        "dojo/string",
        "dojo/_base/xhr",
        "../widget/ResourceGrid",
        "dojo/i18n!./nls/alarm" ],
    function(dom, domconstruct, string, xhr, message) {
    
        return {
            timeout : null,
    
            alarmPopup : null,
    
            alarmAudio : null,
    
            startCheck : function() {
                var self = this;
                xhr.get({
                    url : "web/alarm/new",
                handleAs : "json"
            }).then(function(list) {
                self._showAlarmPopup(list);
            });
    
            self.timeout = setTimeout(function() {
                self.startCheck();
            }, 60 * 1000);
        },
    
        _showAlarmPopup : function(list) {
            if (list == null || list.length == 0) {
                return;
            }
    
            var ul = dom.byId("alarmList");
            if (ul == null) {
                ul = this._createContainer();
            }
    
            this.alarmAudio.play();
    
            for ( var i = 0; i < list.length; i++) {
                var aOrder = list[i];
                var itemId = "alarm_item_" + aOrder["id"];
                var sameIdItem = dom.byId(itemId);
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
                var color = expentTime >= 60 ? "red" : "#FF6200";
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
            this.alarmPopup = domconstruct.create("div", {
                className : "dijitDialog alarmPopup",
            }, document.body);
    
            var title = domconstruct.create("div", {
                className : "dijitDialogTitleBar",
                style : {
                    cursor : "default"
                }
            }, this.alarmPopup);
    
            domconstruct.create("span", {
                className : "dijitDialogTitle",
                innerHTML : message["title"]
            }, title);
            var self = this;
            domconstruct.create("span", {
                className : "dijitDialogCloseIcon",
                innerHTML : "<span class='closeText'>x</span>",
                click : function() {
                    self.removePopup();
                }
            }, title);
    
            var sources = "<source src='sound/alarm.ogg' type='audio/ogg'/><source src='sound/alarm.mp3' type='audio/mpeg'/>"
            this.alarmAudio = domconstruct.create("audio", {
                style : {
                    display : "none"
                },
                innerHTML : sources
            }, this.alarmPopup);
    
            var ul = domconstruct.create("ul", {
                id : "alarmList",
                }, this.alarmPopup);
    
                return ul;
            },
    
            removePopup : function() {
                if (this.alarmPopup != null && dom.byNode(this.alarmPopup) != null) {
                    document.body.removeChild(this.alarmPopup);
                    this.alarmPopup = null;
                }
            },
    
            destroy : function() {
                this.removePopup();
                clearTimeout(this.timeout);
            }
    
        };
    });