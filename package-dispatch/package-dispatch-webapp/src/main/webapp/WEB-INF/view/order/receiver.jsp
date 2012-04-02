<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div style="width: 620px; height: 334px;">
    <div>
      <input id="order_new_receiver_list_querytext" data-dojo-type="dijit.form.TextBox" />
      <button data-dojo-type="dijit.form.Button" data-dojo-props="label: '查询'">
        <script type="dojo/method" data-dojo-event="onClick">
          require(['dijit/registry'], function(registry){
              var querytext = registry.byId('order_new_receiver_list_querytext').get('value');
              registry.byId('order_new_receiver_list_grid').setQuery({
                  query: querytext
              });
          });
       </script>
      </button>
    </div>
    <table id="order_new_receiver_list_grid" data-dojo-type="dojox.grid.DataGrid"
      data-dojo-props="
        store: new dojo.data.ObjectStore({
            objectStore: new dojo.store.JsonRest({target: 'web/receiver/', sortParam: 'sort'})
        }),
        width: '600px', height: '300px',
        onRowDblClick: function(evt){
            var idx = evt.rowIndex; 
            var item = this.getItem(idx);
            
            require(['qiuq/order/new'], function(order){
                order.selectReceiver(item);
            });
        }">
      <thead>
        <tr>
          <th width="100px" field="name">姓名</th>
          <th width="100px" field="tel">电话</th>
          <th width="200px" field="company">公司</th>
          <th width="150px" field="department">部门</th>
          <th width="200px" field="address">地址</th>
        </tr>
      </thead>
    </table>
  </div>
</body>
</html>