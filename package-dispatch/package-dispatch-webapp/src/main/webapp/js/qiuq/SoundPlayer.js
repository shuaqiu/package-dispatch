define(
        [ "dojo/dom", "dojo/dom-construct", "dojo/string", "dojo/_base/declare", "dojo/_base/lang", "dojo/_base/sniff" ],
        function(dom, domconstruct, string, declare, lang, has) {

            // declare a class, thus the player can be used in difference place .
            return declare("qiuq.SoundPlayer", null, {
                _player : null,

                name : null,
                refNode : document.body,
                position : "last",

                postscript : function(/* Object? */params) {
                    if (params) {
                        lang.mixin(this, params);
                    }

                    if (has("ie")) {
                        try {
                            this._createMediaPlayer();
                        } catch (e) {
                        }
                    } else {
                        this._createAudio();
                    }
                },

                _createMediaPlayer : function() {
                    var self = this;
                    this._player = domconstruct.create("object", {
                        classid : "CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6",
                        type : "application/x-oleobject",
                        style : {
                            display : "none"
                        },
                        innerHTML : '<param name="autoStart" value="false">' + '<param name="balance" value="0">'
                                + '<param name="currentPosition" value="0">' + '<param name="currentMarker" value="0">'
                                + '<param name="enableContextMenu" value="true">'
                                + '<param name="enableErrorDialogs" value="false">'
                                + '<param name="enabled" value="true">' + '<param name="fullScreen" value="false">'
                                + '<param name="invokeURLs" value="false">' + '<param name="mute" value="true">'
                                + '<param name="playCount" value="1">' + '<param name="rate" value="1">'
                                + '<param name="uiMode" value="none">' + '<param name="volume" value="100">',
                        onError : function() {
                            self._player = null;
                        }
                    }, this.refNode, this.position);
                },

                _createAudio : function() {
                    var sources = "<source src='sound/${name}.ogg' type='audio/ogg'/>"
                            + "<source src='sound/${name}.mp3' type='audio/mpeg'/>";
                    this._player = domconstruct.create("audio", {
                        style : {
                            display : "none"
                        },
                        innerHTML : string.substitute(sources, {
                            name : this.name
                        })
                    }, this.refNode, this.position);
                },

                play : function() {
                    if (!this._isPlayerPresent()) {
                        // player is not proper initialized
                        return;
                    }

                    try {
                        if (has("ie")) {
                            this._playByMediaPlayer();
                        } else {
                            this._playByAudio();
                        }
                    } catch (e) {
                    }
                },

                _isPlayerPresent : function() {
                    if (this._player == null) {
                        return false;
                    }
                    if (dom.byId(this._player) == null) {
                        return false;
                    }
                    return true;
                },

                _playByMediaPlayer : function() {
                    this._player.settings.autoStart = false;
                    this._player.url = "sound/" + this.name + ".mp3";
                    if (this._player.controls.isavailable('play')) {
                        this._player.controls.play();
                    }
                },

                _playByAudio : function() {
                    if (this._player.readyState >= 2) {
                        this._player.pause();
                        this._player.currentTime = 0;
                        this._player.play();
                    } else {
                        this._player.addEventListener("loadeddata", function() {
                            this.currentTime = 0;
                            this.play();
                        });
                    }
                }
            });
        });