<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<div id="menuBar" data-dojo-type="dijit.MenuBar">
  <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/order/order'], {title: '自主下单', href: 'web/order/edit'}, this.id);}">自主下单</div>
  <div data-dojo-type="dijit.MenuBarItem">订单查询</div>
  <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/customer/receiver'], {title: '收件人管理', href: 'web/receiver/list'}, this.id);}">收件人管理</div>
  <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/customer/receivercompany'], {title: '收件公司管理', href: 'web/receivercompany/list'}, this.id);}">收件公司管理</div>
  <div data-dojo-type="dijit.PopupMenuBarItem">
    <span>系统管理</span>
    <div data-dojo-type="dijit.DropDownMenu">
      <div data-dojo-type="dijit.MenuItem"
        data-dojo-props="onClick: function(){showTab(['qiuq/system/company'], {title: '客户公司管理', href: 'web/company/list'}, this.id);}">客户公司管理</div>
      <div data-dojo-type="dijit.MenuItem"
        data-dojo-props="onClick: function(){showTab(['qiuq/system/user'], {title: '用户管理', href: 'web/user/list'}, this.id);}">用户管理</div>
      <div data-dojo-type="dijit.MenuItem" data-dojo-props="onClick: function(){}">修改密码</div>
      <div data-dojo-type="dijit.MenuItem" data-dojo-props="onClick: function(){require(['qiuq/login'], function(login){login.doLogout();});}">注销</div>
    </div>
  </div>
  <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){}">关于</div>
</div>
