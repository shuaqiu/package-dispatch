define([ "dojox/grid/DataGrid", "dojo/data/ObjectStore", "dojo/store/JsonRest" ], function(DataGrid, JsonRest) {
    var store, dataStore, grid;

    function list() {
        store = new JsonRest({
            target : "web/company"
        });
        grid = new DataGrid({
            store : dataStore = ObjectStore({
                objectStore : store
            }),
            structure : [ {
                name : "code",
                field : "code",
                width : "200px"
            }, {
                name : "name",
                field : "name",
                width : "200px"
            }, {
                name : "address",
                field : "address",
                width : "200px"
            } ]
        }, "target-node-id"); // make sure you have a target HTML element with this id
        grid.startup();
    }

    return {
        "list" : list
    };
});