define([
        "dojo/query",
        "dojo/dom-attr",
        "dojo/json",
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/registry",
        "../resource",
        "../tab",
        "../widget/MessageDialog",
        "dojo/i18n!./nls/schedule",
        "dojo/date/locale",
        "dojo/dnd/Source",
        "dojo/dnd/Target",
        "dijit/form/CheckBox",
        "dijit/form/Textarea",
        "../widget/ResourceGrid" ], function(query, attr, json, lang, xhr, registry, resource, tab, MessageDialog,
        message) {

    return lang.mixin({}, resource, {
        resourceUrl : "web/schedule",
        listGrid : "schedule_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "schedule_editing_tab",
        editingForm : "schedule_editing_form",

        doModify : function(orderId) {
            showTab([], {
                title : this.modifyTabName,
                href : this.resourceUrl + "/edit/" + orderId
            }, this.editingTab, true);
        },

        _getValidData : function() {
            var form = document.forms[this.editingForm];

            var fetcherLi = query("li", query("ul[name='fetcher']", form)[0]);
            if (fetcherLi.length == 0) {
                MessageDialog.error(message["err.NOT_FETCHER"]);
                return null;
            }
            var transiterLi = query("li", query("ul[name='transiter']", form)[0]);
            // if (transiterLi.length == 0) {
            // MessageDialog.error(message["err.NOT_TRANSITER"]);
            // return null;
            // }
            var delivererLi = query("li", query("ul[name='deliverer']", form)[0]);
            if (transiterLi.length > 0 && delivererLi.length == 0) {
                MessageDialog.error(message["err.NOT_DELIVERER"]);
                return null;
            }

            var fetcher = attr.get(fetcherLi[0], "data-handler");
            var transiter = new Array();
            if (transiterLi.length > 0) {
                transiterLi.forEach(function(li) {
                    transiter.push(attr.get(li, "data-handler"));
                });
            }
            var deliverer = -1;
            if (delivererLi.length > 0) {
                deliverer = attr.get(delivererLi[0], "data-handler")
            }

            return json.stringify({
                fetcher : fetcher,
                transiter : transiter,
                deliverer : deliverer
            });
        },

        _saveByXhr : function(data) {
            var form = document.forms[this.editingForm];
            return xhr.post({
                url : this.resourceUrl + "/edit/" + form["orderId"].value,
                postData : data,
                handleAs : "json",
                contentType : "application/json"
            });
        }
    });
});