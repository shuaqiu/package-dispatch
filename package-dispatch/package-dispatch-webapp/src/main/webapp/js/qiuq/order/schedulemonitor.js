define([ "require", "dojo/_base/xhr", "dijit/registry" ], function(require, xhr, registry) {

    return {
        resourceUrl : "web/schedule",

        monitorDeferred : null,
        audio : null,

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
                    var scheduleList = registry.byId("schedule_list");
                    if (scheduleList) {
                        scheduleList._onQuert();
                    }
                    self.playSong();
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

        playSong : function() {
            var self = this;
            require([ "../SoundPlayer" ], function(SoundPlayer) {
                if (self.audio == null) {
                    self.audio = new SoundPlayer({
                        name : "schedule",
                        refNode : document.body
                    });
                }
                self.audio.play();
            })
        },

        stopMonitor : function() {
            if (this.monitorDeferred) {
                try {
                    this.monitorDeferred.cancel();
                    this.monitorDeferred = null;
                    this.audio = null;
                } catch (e) {
                }
            }
        }
    };
});