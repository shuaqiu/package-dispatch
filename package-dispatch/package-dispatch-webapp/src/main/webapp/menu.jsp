<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<div id="menuBar" data-dojo-type="dijit.MenuBar">
  <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showMenu(['qiuq/order/new'], {title: '自主下单', href: 'web/order/new'}, this.id);}">
    <span>自主下单</span>
  </div>
  <div data-dojo-type="dijit.MenuBarItem">订单查询</div>
  <div data-dojo-type="dijit.MenuBarItem">收件人管理</div>
  <div data-dojo-type="dijit.PopupMenuBarItem">
    <span>test12112</span>
    <div data-dojo-type="dijit.DropDownMenu">
      <div data-dojo-type="dijit.MenuItem" data-dojo-props="onClick: function(){alert('test1')}">test1</div>
      <div data-dojo-type="dijit.MenuItem">test2</div>
    </div>
  </div>
  <div data-dojo-type="dijit.MenuBarItem">Help</div>
  <div data-dojo-type="dijit.MenuBarItem">About</div>
</div>
