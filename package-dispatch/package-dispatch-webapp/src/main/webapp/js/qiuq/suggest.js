define([
        "dojo/dom-class",
        "dojo/on",
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "dijit/popup",
        "dijit/layout/ContentPane",
        "./selection" ], function(domclass, on, lang, xhr, registry, popup, ContentPane, selection) {

    return lang.mixin({}, selection, {

        _suggestPane : null,
        _suggestQueryResult : null,
        _forWidget : null,

        suggest : function(obj) {
            var self = this;
            this._forWidget = registry.byId(obj.id);

            xhr.get({
                url : this.selectionStoreTarget,
                content : {
                    query : this._forWidget.get("value")
                },
                handleAs : "json"
            }).then(function(result) {
                self._suggestQueryResult = result;

                self.initSuggestPane();

                var html = self.buildSuggestContent(result);
                self._suggestPane.set("content", html);

                self.showSuggestPane();
            });
        },

        initSuggestPane : function() {
            if (this._suggestPane != null) {
                return;
            }

            this._suggestPane = new ContentPane({
                style : {
                    overflow : "visible"
                }
            });
            popup.moveOffScreen(this._suggestPane);

            this.bindAction();
        },

        bindAction : function() {
            var self = this;
            on(self._suggestPane.domNode, "tr:click", function() {
                // this == tr
                var index = this.rowIndex;
                self.doSelect(self._suggestQueryResult[index]);
                self.hideSuggestPane();
            });
            on(self._suggestPane.domNode, "tr:mouseenter,tr:mouseover", function() {
                domclass.add(this, "dijitMenuItemHover");
            });
            on(self._suggestPane.domNode, "tr:mouseleave,tr:mouseout", function() {
                domclass.remove(this, "dijitMenuItemHover");
            });
        },

        buildSuggestContent : function(result) {
            var html = "<table class='dijit dijitMenu dijitReset dijitMenuTable dijitMenuPassive'>";
            for ( var i = 0; i < result.length; i++) {
                html += "<tr class='dijitReset dijitMenuItem'>";
                for ( var j = 0; j < this.selectionStructure.length; j++) {
                    var style = "width : " + this.selectionStructure[j].width + ";";
                    var text = result[i][this.selectionStructure[j].field];
                    html += "<td class='dijitReset dijitMenuItemLabel' style='" + style + "'>" + text + "</td>";
                }
                html += "</tr>";
            }
            html += "</table>";
            return html;
        },

        showSuggestPane : function() {
            popup.open({
                parent : this._forWidget,
                popup : this._suggestPane,
                around : this._forWidget.domNode,
                onExecute : lang.hitch(this, this.onPopupExecute || function() {
                }),
                onClose : lang.hitch(this, this.onPopupClose || function() {
                })
            });

            this._forWidget.onBlur = lang.hitch(this, function() {
                this.hideSuggestPane();
            });
        },

        hideSuggestPane : function() {
            popup.close(this._suggestPane);
            this._forWidget.onBlur = function() {
            };
            this._suggestQueryResult = null;
        },

        onPopupExecute : function() {
        },

        onPopupClose : function() {
        },
    });

});