define([
        "require",
        "dojo/dom",
        "dojo/dom-construct",
        "dojo/string",
        "dojo/_base/xhr",
        "../SoundPlayer",
        "dojo/i18n!./nls/alarm" ], function(require, dom, domconstruct, string, xhr, SoundPlayer, message) {

    return {
        timeout : null,

        alarmPopup : null,

        alarmAudio : null,

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
                    self._showAlarmPopup(result.obj);
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

            if (this.alarmAudio) {
                this.alarmAudio.play();
            }

            for ( var i = 0; i < list.length; i++) {
                var aOrder = list[i];
                var itemId = "alarm_item_" + aOrder["id"];
                var sameIdItem = dom.byId(itemId);
                if (sameIdItem) {
                    ul.removeChild(sameIdItem);
                }

                var expentTime = parseInt((new Date().getTime() - aOrder["fetchTime"]) / 60 / 1000);
                var desc = string.substitute(message["desc"], {
                    barCode : aOrder["barCode"] || "",
                    senderName : aOrder["senderName"] || "",
                    senderTel : aOrder["senderTel"] || "",
                    receiverName : aOrder["receiverName"] || "",
                    receiverTel : aOrder["receiverTel"] || "",
                    expentTime : expentTime + ""
                });
                var color = expentTime >= 60 ? "red" : "#FF6200";
                domconstruct.create("li", {
                    id : itemId,
                    innerHTML : desc,
                    style : {
                        "color" : color
                    },
                    title : message["itemRemoveTip"],
                    ondblclick : function(){
                        ul.removeChild(this);
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

            this.alarmAudio = new SoundPlayer({
                name : "alarm",
                refNode : this.alarmPopup
            });

            var ul = domconstruct.create("ul", {
                id : "alarmList",
            }, this.alarmPopup);

            return ul;
        },

        removePopup : function() {
            if (this.alarmPopup != null && dom.byId(this.alarmPopup) != null) {
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