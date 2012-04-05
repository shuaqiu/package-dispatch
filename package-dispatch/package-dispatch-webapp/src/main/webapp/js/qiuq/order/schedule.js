define([
        "dojo/_base/lang",
        "dijit/registry",
        "../resource",
        "../selection",
        "../widget/ResourceGrid",
        "dojo/i18n!./nls/schedule",
        "dojo/date/locale",
        "dojo/date/stamp",
        "dijit/form/CheckBox",
        "dijit/form/Textarea" ], function(lang, registry, resource, selection, ResourceGrid, message) {

    return lang.mixin({}, resource, selection, {
        resourceUrl : "web/schedule",
        listGrid : "schedule_list_grid",

        createTabName : message["create"],
        modifyTabName : message["modify"],
        editingTab : "schedule_editing_tab",
        editingForm : "schedule_editing_form",

        selectionDialog : "schedule_editing_receiver_dialog",
        selectionStoreTarget : "web/receiver/",
        selectionStructure : [ {
            name : "姓名",
            field : "name",
            width : "100px"
        }, {
            name : "电话",
            field : "tel",
            width : "100px"
        }, {
            name : "公司",
            field : "company",
            width : "200px"
        }, {
            name : "地址",
            field : "address",
            width : "200px"
        } ],

        doSelect : function(item) {
            var form = document.forms[this.editingForm];
            form["receiverId"].value = item["id"];
            registry.byId(form["receiverName"].id).set("value", item["name"]);
            registry.byId(form["receiverTel"].id).set("value", item["tel"]);
            registry.byId(form["receiverCompany"].id).set("value", item["company"]);
            registry.byId(form["receiverAddress"].id).set("value", item["address"]);
        },

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
        
        _initForm : function(item) {

        }
    });
    //
    // function showSuggestReceiver(obj) {
    // var value = registry.byId(obj.id).get("value");
    // var reg = new RegExp(".*" + value + ".*");
    // initReceiverTable({
    // "name" : reg
    // }, false).then(function(html) {
    // new Dialog({
    // "title" : "Receiver",
    // "content" : html,
    // style : {
    // width : "600px",
    // height : "400px"
    // }
    // }).show();
    // });
    // }
});