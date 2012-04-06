<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="schedule_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="orderId" value="${order.id }"/>
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
      <legend>人员选择</legend>
      <div class="simpleTableInfo">
        <div>
          <div style="width: 30%; float: left;">
            <div>
              <span>收件人员</span>
              <ul name="fetcher" class="" style="height: 30px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['fetcher']">
              </ul>
            </div>
            <div>
              <span>中转人员</span>
              <ul name="transiter" class="" style="min-height: 90px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['transiter']">
              </ul>
            </div>
            <div>
              <span>派件人员</span>
              <ul name="deliverer" class="" style="height: 30px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['deliverer']">
              </ul>
            </div>
          </div>
          <div style="width: 45%; float: left; margin-left: 5%;">
            <span>人员列表</span>
            <ul name="fetcherCandidate" class="" style="min-height: 30px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['fetcher']">
              <c:forEach var="var" items="${fetcher }">
                <li data-handler="${var.id }" class="dojoDndItem" dndType="fetcher">${var.name } ${var.tel }</li>
              </c:forEach>
            </ul>
            <ul name="transiterCandidate" class="" style="min-height: 30px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['transiter']">
              <c:forEach var="var" items="${transiter }">
                <li data-handler="${var.id }"  class="dojoDndItem" dndType="transiter">${var.name } ${var.tel }</li>
              </c:forEach>
            </ul>
            <ul name="delivererCandidate" class="" style="min-height: 30px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['deliverer']">
              <c:forEach var="var" items="${deliverer }">
                <li data-handler="${var.id }"  class="dojoDndItem" dndType="deliverer">${var.name } ${var.tel }</li>
              </c:forEach>
            </ul>
          </div>
        </div>
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