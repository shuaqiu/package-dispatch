define([
        "require",
        "dojo/_base/lang",
        "dojo/_base/Deferred",
        "dijit/registry",
        "dijit/layout/ContentPane",
        "dijit/layout/TabContainer" ], function(require, lang, Deferred, registry, ContentPane) {

    function getTab() {
        return registry.byId("tab");
    }

    function getPanelId(specId) {
        return "panel_" + specId;
    }

    function getPanel(conf, specId, forceReload) {
        if (specId) {
            var panelId = getPanelId(specId);

            var panel = registry.byId(panelId);
            if (panel) {
                if (forceReload) {
                    for ( var p in conf) {
                        if (p.indexOf("on") == 0) {
                            // reset the panel event, as the name of these events are start with "on"
                            panel[p] = conf[p];
                        } else {
                            // try to set other properties
                            try {
                                panel.set(p, conf[p]);
                            } catch (e) {
                            }
                        }
                    }
                }
                return panel;
            }

            conf["id"] = panelId;
        }

        // var confOnLoad = conf["onLoad"] || function() {
        // };
        // conf["onLoad"] = function(data) {
        // if (data == "") {
        // require([ "qiuq/login", function(login) {
        // login.reLogin();
        // } ]);
        // }
        // confOnLoad(data);
        // };

        conf["closable"] = true;
        var panel = new ContentPane(conf);
        getTab().addChild(panel);
        return panel;
    }

    function show(conf, specId, forceReload) {
        // get panel or create panel
        var panel = getPanel(conf, specId, forceReload);
        // make this tab selected
        getTab().selectChild(panel);
    }

    function close(specId) {
        var panel = registry.byId(getPanelId(specId));
        getTab().closeChild(panel);
    }

    return {
        "show" : function(moduleArr, conf, specId, forceReload) {
            require(moduleArr, function() {
                show(conf, specId, forceReload);
            });
        },
        "close" : close
    };
});