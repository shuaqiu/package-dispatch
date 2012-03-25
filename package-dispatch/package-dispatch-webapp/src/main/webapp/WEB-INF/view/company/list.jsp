<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="dijit.MenuBar">
    <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/system/company'], {title: '新建客户公司', href: 'web/company/new'}, this.id);}">新建</div>
<!--     <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){require(['dijit/registry'], function(registry){registry.byId('company_list_grid').addRow();});}">新建</div> -->
    <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){}">修改</div>
    <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){require(['qiuq/system/company'], function(company){company.del()});}">删除</div>
  </div>
  <table id="company_list_grid" data-dojo-type="dojox.grid.DataGrid" data-dojo-props="store: new dojo.data.ObjectStore({objectStore: new dojo.store.Cache(new dojo.store.JsonRest({target: 'web/company/', sortParam: 'sort'}), new dojo.store.Memory())})">
    <thead>
      <tr>
        <th width="50px" editable="true" field="id">ID</th>
        <th width="150px" editable="true" field="code">编码</th>
        <th width="250px" editable="true" field="name">公司名称</th>
        <th width="350px" editable="true" field="address">地址</th>
      </tr>
    </thead>
  </table>
</body>
</html>