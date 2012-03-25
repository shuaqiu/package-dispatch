define([
        "dojo/_base/xhr",
        "dijit/registry",
        "dojo/dom-form",
        "../widget/MessageDialog",
        "../ErrCode",
        "dojo/i18n!./nls/company",
        "dojox/grid/DataGrid",
        "dojo/data/ObjectStore",
        "dojo/store/JsonRest",
        "dojo/store/Memory",
        "dojo/store/Cache",
        "dijit/MenuBar",
        "dijit/MenuBarItem",
        "dijit/form/Form",
        "dijit/form/ValidationTextBox",
        "dijit/form/Button" ], function(xhr, registry, domform, MessageDialog, ErrCode, message) {

    function save() {
        var form = document.forms["company"];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            MessageDialog.error(message["err." + ErrCode.INVALID]);
            return;
        }

        xhr.post({
            "url" : "web/company",
            "postData" : domform.toJson(form),
            "handleAs" : "json",
            "contentType" : "application/json"
        }).then(function(result) {
            if (result.ok) {
                dijitForm.reset();
            } else {
                MessageDialog.error(message["err." + result.errCode]);
            }
        });
    }
    
    function del(){
        var grid = registry.byId("company_list_grid");
        grid.removeSelectedRows();
        grid.store.save();
    }

    return {
        "save" : save,
        "del" : del
    };
});