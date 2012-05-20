<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<div id="menuBar" data-dojo-type="dijit.MenuBar">
  <c:if test="${function.order }">
    <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/order/order'], {title: '自主下单', href: 'web/order/edit'}, 'order_editing_tab');}">自主下单</div>
  </c:if>
  <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/order/order'], {title: '订单查询', href: 'web/order/list'}, 'order_list_tab');}">订单查询</div>
  <c:if test="${function.schedule }">
    <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/order/schedule'], {title: '订单调度', href: 'web/schedule/list'}, this.id);}">订单调度</div>
  </c:if>
  <c:if test="${function.alarm }">
    <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/order/alarm'], {title: '警报中心', href: 'web/alarm/list'}, this.id);}">警报中心</div>
  </c:if>
  <c:if test="${function.inStorage }">
    <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/order/storage'], {title: '快件入库', href: 'web/storage/edit'}, this.id);}">快件入库</div>
  </c:if>
  <c:if test="${function.outStorage }">
    <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/order/storage'], {title: '快件出库', href: 'web/storage/delete'}, this.id);}">快件出库</div>
  </c:if>
  <%--   <c:if test="${function.history }"> --%>
  <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/order/history'], {title: '历史订单', href: 'web/order/history/list'}, this.id);}">历史订单</div>
  <%--   </c:if> --%>
  <c:if test="${function.receiver }">
    <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){showTab(['qiuq/customer/receiver'], {title: '收件人管理', href: 'web/receiver/list'}, this.id);}">收件人管理</div>
  </c:if>
  <div data-dojo-type="dijit.PopupMenuBarItem">
    <span>系统管理</span>
    <div data-dojo-type="dijit.DropDownMenu">
      <c:if test="${function.customerCompany }">
        <div data-dojo-type="dijit.MenuItem" data-dojo-props="onClick: function(){showTab(['qiuq/system/company'], {title: '客户公司管理', href: 'web/company/list'}, this.id);}">客户公司管理</div>
      </c:if>
      <c:if test="${function.customer }">
        <div data-dojo-type="dijit.MenuItem" data-dojo-props="onClick: function(){showTab(['qiuq/system/customer'], {title: '客户管理', href: 'web/customer/list'}, this.id);}">客户管理</div>
      </c:if>
      <c:if test="${function.employer }">
        <div data-dojo-type="dijit.MenuItem" data-dojo-props="onClick: function(){showTab(['qiuq/system/user'], {title: '员工管理', href: 'web/user/list'}, this.id);}">员工管理</div>
      </c:if>
      <c:if test="${function.account }">
        <div data-dojo-type="dijit.MenuItem" data-dojo-props="onClick: function(){showTab(['qiuq/system/account'], {title: '账号管理', href: 'web/customer/list'}, this.id);}">账号管理</div>
      </c:if>
      <div data-dojo-type="dijit.MenuItem" data-dojo-props="onClick: function(){require(['qiuq/system/passwordmodifier'], function(modifier){modifier.show();});}">修改密码</div>
      <div data-dojo-type="dijit.MenuItem" data-dojo-props="onClick: function(){require(['qiuq/login'], function(login){login.doLogout();});}">注销</div>
    </div>
  </div>
  <div data-dojo-type="dijit.MenuBarItem" data-dojo-props="onClick: function(){}">关于</div>
</div>
