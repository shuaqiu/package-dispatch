define([ "dojo/_base/declare", "dojo/_base/Deferred", "dojo/store/JsonRest" ], function(declare, Deferred, JsonRest) {

    return declare("qiuq.store.JsonRest", [ JsonRest ], {
        constructor : function(/* dojo.store.JsonRest */options) {
            declare.safeMixin(this, options);
        },

        put : function(object, options) {
            var deferred = new Deferred();
            deferred.resolve();
            return deferred;
        },
        remove : function(id) {
            var deferred = new Deferred();
            deferred.resolve();
            return deferred;
        }
    });
});