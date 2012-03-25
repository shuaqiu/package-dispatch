define([ "require", "dojo/_base/lang", "dojo/_base/Deferred", "dijit/registry", "dijit/layout/ContentPane", "dijit/layout/TabContainer" ],
        function(require, lang, Deferred, registry, ContentPane) {

            function getTab() {
                return registry.byId("tab");
            }

            function getPanelId(specId) {
                return "panel_" + specId;
            }

            function getPanel(conf, specId) {
                if (specId) {
                    var panelId = getPanelId(specId);

                    var panel = registry.byId(panelId);
                    if (panel) {
                        return panel;
                    }

                    conf["id"] = panelId;
                }

                conf["closable"] = true;
                var panel = new ContentPane(conf);
                getTab().addChild(panel);
                return panel;
            }

            function show(conf, specId) {
                // get panel or create panel
                var panel = getPanel(conf, specId);
                // make this tab selected
                getTab().selectChild(panel);
            }

            return {
                show : function(moduleArr, conf, specId) {
                    var deferred = new Deferred();

                    if (lang.isArray(moduleArr)) {
                        require(moduleArr, function() {
                            show(conf, specId);
                            deferred.resolve();
                        });
                    } else {
                        show(moduleArr, conf);
                        deferred.resolve();
                    }

                    return deferred;
                }
            };
        });