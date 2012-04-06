define([
        "dojo/query",
        "dojo/dom-attr",
        "dojo/json",
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "../resource",
        "dojo/i18n!./nls/schedule",
        "dojo/date/locale",
        "dojo/dnd/Source",
        "dojo/dnd/Target",
        "dijit/form/CheckBox",
        "dijit/form/Textarea",
        "../widget/ResourceGrid" ], function(query, attr, json, lang, xhr, registry, resource, message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/schedule",
        listGrid : "schedule_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "schedule_editing_tab",
        editingForm : "schedule_editing_form",

        doModify : function() {
            var grid = registry.byId(this.listGrid);
            var items = grid.selection.getSelected();
            if (items.length != 1) {
                MessageDialog.error(message["err.NOT_SELECTED"]);
                return;
            }

            var item = items[0];

            showTab([], {
                title : this.modifyTabName,
                href : this.resourceUrl + "/edit/" + item["id"]
            }, this.editingTab);
        },

        doSave : function() {
            var form = document.forms[this.editingForm];

            var fetcherLi = query("li", query("ul[name='fetcher']", form)[0]);
            if (fetcherLi.length == 0) {
                MessageBox.error(message["err.NOT_FETCHER"]);
                return;
            }
            var transiterLi = query("li", query("ul[name='transiter']", form)[0]);
            if (transiterLi.length == 0) {
                MessageBox.error(message["err.NOT_TRANSITER"]);
                return;
            }
            var delivererLi = query("li", query("ul[name='deliverer']", form)[0]);
            if (delivererLi.length == 0) {
                MessageBox.error(message["err.NOT_DELIVERER"]);
                return;
            }

            var fetcher = attr.get(fetcherLi[0], "data-handler");
            var transiter = new Array();
            transiterLi.forEach(function(li) {
                transiter.push(attr.get(li, "data-handler"));
            });
            var deliverer = attr.get(delivererLi[0], "data-handler");

            xhr.post({
                url : "web/schedule/edit/" + form["orderId"].value,
                postData : json.stringify({
                    fetcher : fetcher,
                    transiter : transiter,
                    deliverer : deliverer
                }),
                handleAs : "json",
                contentType : "application/json"
            }).then(function() {

            });
        }
    });
});