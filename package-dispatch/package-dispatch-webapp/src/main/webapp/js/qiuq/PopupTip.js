define([ "dojo/dom", "dojo/dom-construct", "dojo/dnd/Moveable", "./SoundPlayer" ], function(dom, domconstruct,
        Moveable, SoundPlayer) {

    return {
        title : null,

        draggable : true,

        _popup : null,
        _itemContainer : null,
        popupProp : {
            className : "dijitDialog popupTip"
        },

        audioName : null,
        _audio : null,

        popup : function(list) {
            if (list == null || list.length == 0) {
                return;
            }

            if (this._popup == null || dom.byId(this._popup) == null) {
                this._createPopup();
            }

            if (this._audio) {
                this._audio.play();
            }

            for ( var i = 0; i < list.length; i++) {
                this._createItem(list[i]);
            }
        },

        _createItem : function(item) {
        },

        _createPopup : function() {
            this._popup = domconstruct.create("div", this.popupProp, document.body);

            var title = domconstruct.create("div", {
                className : "dijitDialogTitleBar",
                style : {
                    cursor : "default"
                }
            }, this._popup);

            this._moveable = new Moveable(this._popup, {
                handle : title
            });
            // this.connect(this._moveable, "onMoveStop", "_endDrag");

            domconstruct.create("span", {
                className : "dijitDialogTitle",
                innerHTML : this.title
            }, title);

            var self = this;
            domconstruct.create("span", {
                className : "dijitDialogCloseIcon",
                innerHTML : "<span class='closeText'>x</span>",
                click : function() {
                    self.removePopup();
                }
            }, title);

            if (this.audioName != null) {
                this._audio = new SoundPlayer({
                    name : this.audioName,
                    refNode : this._popup
                });
            }

            this._itemContainer = domconstruct.create("ul", {}, this._popup);
        },

        removePopup : function() {
            if (this._popup != null && dom.byId(this._popup) != null) {
                document.body.removeChild(this._popup);
                delete this._popup;
                delete this._itemContainer;
                delete this._audio;
            }
            if (this._moveable) {
                this._moveable.destroy();
            }
        }
    };
});