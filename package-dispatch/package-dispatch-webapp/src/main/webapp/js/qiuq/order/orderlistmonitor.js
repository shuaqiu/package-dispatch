define([ "dijit/registry" ], function(registry) {
    return {
        gridId : "order_list_grid",

        newOrder : function(result) {
            if (result.monitorUser.type == 1 || (result.monitorUser.id == result.order.senderId)) {
                var orderListGrid = registry.byId(this.gridId);
                orderListGrid.store.newItem(result.order);
            }
        },

        handleOrder : function(result) {
            var orderListGrid = registry.byId(this.gridId);
            var order = result.order;
            var itemIndex = orderListGrid.getItemIndex(order);
            if (itemIndex >= 0) {
                var item = orderListGrid.getItem(itemIndex);
                if (order["state"] >= 6) {
                    // already delivered, or canceled, or closed.
                    orderListGrid.store.deleteItem(item);
                } else {
                    // order is still in progressing, just update its state.
                    for ( var p in order) {
                        orderListGrid.store.setValue(item, p, order[p]);
                    }
                }
            }
        }
    };
});