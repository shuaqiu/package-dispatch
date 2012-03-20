define([ "require", "dijit/registry", "dijit/layout/ContentPane",
		"dijit/layout/TabContainer" ],
		function(require, registry, ContentPane) {

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

			function isArray(it) {
				return Object.prototype.toString.call(it) == "[object Array]";
			}

			function show(conf, specId) {
				// get panel or create panel
				var panel = getPanel(conf, specId);
				// make this tab selected
				getTab().selectChild(panel);
			}

			return {
				show : function(moduleArr, conf, specId) {
					if (!isArray(moduleArr)) {
						show(moduleArr, conf);
						return;
					}
					require(moduleArr, function() {
						show(conf, specId);
					});
				}
			};
		});