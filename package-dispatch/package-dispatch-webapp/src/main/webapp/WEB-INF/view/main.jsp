<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<div id="appLayout" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline'">
  <div id="banner" data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'top'">
    <%--       <div id="showusername">${user.name }</div> --%>
    <div class="searchInputColumn">
      <input id="searchTerms" data-dojo-type="dijit.form.TextBox" data-dojo-props="placeHolder: '搜索订单(条形码/姓名/手机)', onKeyUp: doQuery">
    </div>
    <div id="nav"><%@ include file="menu.jsp"%></div>
  </div>
  <!--     <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="splitter: true, region: 'left'" style="width: 200px;">left</div> -->
  <div id="tab" data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region: 'center', tabPosition: 'top'">
    <div id="panel_order_list_tab" data-dojo-type="dijit.layout.ContentPane"
      data-dojo-props="
      title : '订单查询', 
      href : 'web/order/list'
      /*,
      onShow : function(){
          var self = this;
          require(['dijit/registry'], function(registry){
              var list = registry.byId('order_list');
              if(list != null){
                  list._onQuery();
                  
                  self.onHide();
                  self.refreshTimeout = setTimeout(function(){
                      self.onShow();
                  }, 15 * 1000);
              }
          });
      },
      onHide : function(){
          if(this.refreshTimeout != null){
              clearTimeout(this.refreshTimeout);
          }
      },
      uninitialize : function(){
          this.onHide();
      }*/"></div>
  </div>
  <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'bottom'" style="text-align: center;">Copyright © 2012-2012 慧信</div>
</div>
