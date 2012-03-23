define([
        "dojo/_base/xhr",
        "dojo/dom-form",
        "dojo/store/Memory",
        "dojo/_base/Deferred",
        "dijit/registry",
        "dijit/Dialog",
        "../widget/MessageDialog",
        "../ErrCode",
        "dojo/i18n!./nls/order",
        "dijit/form/Form",
        "dijit/form/ValidationTextBox",
        "dijit/form/Button",
        "dijit/form/CheckBox",
        "dijit/form/ComboBox" ], function(xhr, domform, Memory, Deferred, registry, Dialog, MessageDialog, ErrCode,
        message) {

    function save() {
        var form = document.forms["order"];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            MessageDialog.error(message["err." + ErrCode.INVALID]);
            return;
        }

        xhr.post({
            "url" : "web/order",
            "postData" : domform.toJson(form),
            "handleAs" : "json",
            "contentType" : "application/json"
        }).then(function(result) {
            if (result.ok) {

            } else {
                MessageDialog.error(message["err." + result.errCode]);
            }
        });
    }

    var store = null;

    function initReceiverStore() {
        var deferred = new Deferred();
        if (store != null) {
            deferred.resolve();
            return deferred;
        }

        xhr.get({
            url : "web/order/receiver",
            handleAs : "json"
        }).then(function(data) {
            store = new Memory({
                data : data
            });
            deferred.resolve();
        });
        return deferred;
    }

    function initReceiverTable(query, isOmitHead) {
        var deferred = new Deferred();

        initReceiverStore().then(function() {
            var html = [];
            html.push("<table>");
            if (!isOmitHead) {
                html.push("<thead><tr>");
                html.push("<th>" + message["name"] + "</th>");
                html.push("<th>" + message["tel"] + "</th>");
                html.push("<th>" + message["address"] + "</th>");
                html.push("<th>" + message["company"] + "</th>");
                html.push("</tr></thead>");
            }
            html.push("<tbody>");
            store.query(query).forEach(function(aRec) {
                html.push("<tr>");
                html.push("<td>" + aRec.name + "</td>");
                html.push("<td>" + aRec.tel + "</td>");
                html.push("<td>" + aRec.address + "</td>");
                html.push("<td>" + aRec.company + "</td>");
                html.push("</tr>");
            });
            html.push("</tbody>");
            html.push("</table>");
            deferred.resolve(html.join(""));
        });

        return deferred;
    }

    function popupReceiverList() {
        initReceiverTable({}, false).then(function(html) {
            new Dialog({
                "title" : message["receiver"],
                "content" : html,
                style : {
                    width : "600px",
                    height : "400px"
                }
            }).show();
        });
    }

    function showSuggestReceiver(obj) {
        var value = registry.byId(obj.id).get("value");
        var reg = new RegExp(".*" + value + ".*");
        initReceiverTable({
            "name" : reg
        }, false).then(function(html) {
            new Dialog({
                "title" : "Receiver",
                "content" : html,
                style : {
                    width : "600px",
                    height : "400px"
                }
            }).show();
        });
    }

    return {
        "save" : save,
        "popupReceiverList" : popupReceiverList,
        "showSuggestReceiver" : showSuggestReceiver
    };
});