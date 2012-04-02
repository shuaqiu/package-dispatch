<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline'">
    <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'top'">
      <div>
        <input id="receiver_list_querytext" data-dojo-type="dijit.form.TextBox" />
        <button data-dojo-type="dijit.form.Button" data-dojo-props="label: '查询'">
          <script type="dojo/method" data-dojo-event="onClick">
            require(['dijit/registry'], function(registry){
                var querytext = registry.byId('receiver_list_querytext').get('value');
                registry.byId('receiver_list_grid').setQuery({
                    query: querytext
                });
            });
          </script>
        </button>
      </div>
      <div data-dojo-type="dijit.MenuBar">
        <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){require(['qiuq/customer/receiver'], function(receiver){receiver.create()});}">新建</div>
        <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){require(['qiuq/customer/receiver'], function(receiver){receiver.del()});}">删除</div>
      </div>
    </div>

    <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'center'">
      <table id="receiver_list_grid" data-dojo-type="dojox.grid.DataGrid"
        data-dojo-props="
        store: new dojo.data.ObjectStore({
            objectStore: new dojo.store.Cache(new dojo.store.JsonRest({target: 'web/receiver/', sortParam: 'sort'}), new dojo.store.Memory())
        }),
        onApplyEdit: function(){this.store.save();}">
        <thead>
          <tr>
            <th width="150px" editable="true" field="name">姓名</th>
            <th width="100px" editable="true" field="tel">电话</th>
            <th width="250px" editable="true" field="company">公司</th>
            <th width="250px" editable="true" field="address">地址</th>
          </tr>
        </thead>
      </table>
    </div>

  </div>
</body>
</html>