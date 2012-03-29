<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div style="width: 620px; height: 334px;">
    <div>
      <input id="user_new_company_list_querytext" data-dojo-type="dijit.form.TextBox" />
      <button data-dojo-type="dijit.form.Button" data-dojo-props="label: '查询'">
        <script type="dojo/method" data-dojo-event="onClick">
          require(['dijit/registry'], function(registry){
              var querytext = registry.byId('user_new_company_list_querytext').get('value');
              registry.byId('user_new_company_list_grid').setQuery({
                  query: querytext
              });
          });
       </script>
      </button>
    </div>
    <table id="user_new_company_list_grid" data-dojo-type="dojox.grid.DataGrid"
      data-dojo-props="
        store: new dojo.data.ObjectStore({
            objectStore: new dojo.store.JsonRest({target: 'web/company/', sortParam: 'sort'})
        }),
        width: '600px', height: '300px',
        onRowDblClick: function(evt){
            var idx = evt.rowIndex; 
            var item = this.getItem(idx);
            var id = this.store.getValue(item, 'id');
            var name = this.store.getValue(item, 'name');
            
            require(['qiuq/system/user'], function(user){
                user.selectCompany(id, name);
            });
        }">
      <thead>
        <tr>
          <th width="150px" field="code">编码</th>
          <th width="200px" field="name">公司名称</th>
          <th width="250px" field="address">地址</th>
        </tr>
      </thead>
    </table>
  </div>
</body>
</html>