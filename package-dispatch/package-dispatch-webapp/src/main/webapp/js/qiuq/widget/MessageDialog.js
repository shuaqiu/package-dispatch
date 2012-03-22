define([ "dijit/Dialog", "dojo/i18n!./nls/MessageDialog" ], function(Dialog, message) {

    function error(content) {
        new Dialog({
            "title" : message["errTitle"],
            "content" : content
        }).show();
    }
    
    function confirm(content, okMethod, cancelMethod){
        new Dialog({
            "title" : message["errTitle"],
            "content" : content
        }).show();
    }

    return {
        error : error
    };
});