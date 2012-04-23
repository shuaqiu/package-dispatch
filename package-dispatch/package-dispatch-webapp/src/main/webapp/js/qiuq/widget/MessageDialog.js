define([ "./DestroyWhenCloseDialog", "dojo/i18n!./nls/MessageDialog" ], function(DestroyWhenCloseDialog, message) {
    
    function alert(content) {
        new DestroyWhenCloseDialog({
            "title" : message["alertTitle"],
            "content" : content
        }).show();
    }

    function error(content) {
        new DestroyWhenCloseDialog({
            "title" : message["errTitle"],
            "content" : content
        }).show();
    }

    function confirm(content, okMethod, cancelMethod) {
        new DestroyWhenCloseDialog({
            "title" : message["confirmTitle"],
            "content" : content
        }).show();
    }

    return {
        alert : alert,
        error : error,
        confirm : confirm
    };
});