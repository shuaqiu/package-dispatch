<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <!--   <div data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline'"> -->
  <!--     <div data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region: 'center', tabPosition: 'left'"> -->
  <!--         <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="title: '收件人员'"> -->
  <!--           <div></div> -->
  <!--         </div> -->
  <!--         <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="title: '中转人员'"></div> -->
  <!--         <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="title: '派件人员'"></div> -->
  <!--     </div> -->
  <!--     <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'bottom'"> -->
  <!--       <table class="formTable"> -->
  <!--         <tr> -->
  <!--           <td class="labelCell"></td> -->
  <!--           <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '调度', onClick : function(){require(['qiuq/order/schedule'], function(resource){resource.doSave()})}" /></td> -->
  <!--         </tr> -->
  <!--       </table> -->
  <!--     </div> -->
  <!--   </div> -->

  <form name="schedule_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="id" /> <input type="hidden" name="orderId" />
    <fieldset>
      <legend>寄件人</legend>
      <div class="simpleTableInfo">
        <table class="formTable">
          <tr>
            <th width="20%">姓名</th>
            <th width="15%">电话号码</th>
            <th width="30%">公司</th>
            <th width="35%">地址</th>
          </tr>
          <tr>
            <td>${order.senderName }</td>
            <td>${order.senderTel }</td>
            <td>${order.senderCompany }</td>
            <td>${order.senderAddress }</td>
          </tr>
        </table>
      </div>
    </fieldset>
    <fieldset>
      <legend>收件人员</legend>
      <div class="simpleTableInfo">
        <table class="formTable">
          <tr>
            <th width="20%">姓名</th>
            <th width="15%">电话号码</th>
            <th width="30%">公司</th>
            <th width="35%">地址</th>
          </tr>
          <tr>
            <td>${order.receiverName }</td>
            <td>${order.receiverTel }</td>
            <td>${order.receiverCompany }</td>
            <td>${order.receiverAddress }</td>
          </tr>
        </table>
      </div>
    </fieldset>
    <fieldset>
      <legend>收件人</legend>
      <div class="simpleTableInfo">
        <table class="formTable">
          <tr>
            <th width="20%">姓名</th>
            <th width="15%">电话号码</th>
            <th width="30%">公司</th>
            <th width="35%">地址</th>
          </tr>
          <tr>
            <td>${order.receiverName }</td>
            <td>${order.receiverTel }</td>
            <td>${order.receiverCompany }</td>
            <td>${order.receiverAddress }</td>
          </tr>
        </table>
      </div>
    </fieldset>
    <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'bottom'">
      <table class="formTable">
        <tr>
          <td class="labelCell"></td>
          <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '调度', onClick : function(){require(['qiuq/order/schedule'], function(resource){resource.doSave()})}" /></td>
        </tr>
      </table>
    </div>
  </form>
  <div>
    <!-- 加上这一个div, 是为了使得外层的ContentPane 不是只有一个子结点, 不增加overflow: hidden 的样式, 从而可以出现滚动条 -->
  </div>
</body>
</html>