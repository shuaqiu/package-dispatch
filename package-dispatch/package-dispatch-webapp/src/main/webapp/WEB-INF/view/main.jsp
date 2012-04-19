<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<div id="appLayout" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline'">
  <div id="banner" data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'top'">
    <%--       <div id="showusername">${user.name }</div> --%>
    <div class="searchInputColumn">
      <input id="searchTerms" data-dojo-type="dijit.form.TextBox" data-dojo-props="placeHolder: '搜索订单', onKeyUp: doQuery">
    </div>
    <div id="nav"><%@ include file="menu.jsp"%></div>
  </div>
  <!--     <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="splitter: true, region: 'left'" style="width: 200px;">left</div> -->
  <div id="tab" data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region: 'center', tabPosition: 'top'">
    <div id="panel_order_list_tab" data-dojo-type="dijit.layout.ContentPane" data-dojo-props="title: '订单查询', href: 'web/order/list'"></div>
  </div>
  <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'bottom'" style="text-align: center;">Copyright © 2012-2012 慧信</div>
</div>
