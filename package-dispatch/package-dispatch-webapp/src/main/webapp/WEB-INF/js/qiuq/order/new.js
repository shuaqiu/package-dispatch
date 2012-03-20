define([ "dojo/_base/xhr", "dojo/dom-form", "dijit/registry", "dijit/form/Form", "dijit/form/ValidationTextBox",
        "dijit/form/Button", "dijit/form/CheckBox", "dijit/form/ComboBox" ], function(xhr, domform, registry) {

    if (!window.qiuq) {
        window.qiuq = function() {
        };
    }

    if (!window.qiuq.order) {
        window.qiuq.order = function() {
        };
    }

    window.qiuq.order.save = function() {
        var form = document.forms["order"];

        var dijitForm = registry.byId(form.id);
        if (dijitForm.isValid() == false) {
            alert("a");
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
                alert(result.message);
            }
        });
    };

    return {};
});