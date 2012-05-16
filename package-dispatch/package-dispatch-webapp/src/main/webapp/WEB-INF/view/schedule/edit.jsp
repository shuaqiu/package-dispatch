<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="schedule_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="orderId" value="${order.id }" />
    <fieldset>
      <legend>寄件人/收件人</legend>
      <div class="simpleTableInfo">
        <table class="formTable">
          <tr>
            <th width="15%"></th>
            <th width="20%">姓名</th>
            <th width="15%">电话号码</th>
            <th width="30%">公司</th>
            <th width="35%">地址</th>
          </tr>
          <tr>
            <td>寄件人</td>
            <td>${order.senderName }</td>
            <td>${order.senderTel }</td>
            <td>${order.senderCompany }</td>
            <td>${order.senderAddress }</td>
          </tr>
          <tr>
            <td>收件人</td>
            <td>${order.receiverName }</td>
            <td>${order.receiverTel }</td>
            <td>${order.receiverCompany }</td>
            <td>${order.receiverAddress }</td>
          </tr>
        </table>
      </div>
    </fieldset>
    <fieldset>
      <legend>收件人员(从右边的人员列表拖动到左边方框内进行调度)</legend>
      <div class="simpleTableInfo">
        <div>
          <div style="width: 30%; float: left;">
            <ul name="fetcher" class="" style="height: 40px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['fetcher'], copyState : function(){return false;}">
              <li data-handler="${scheduledFetcher.id }" class="dojoDndItem" dndType="fetcher" style="line-height: 25px; margin-top: 3px;">${scheduledFetcher.name } ${scheduledFetcher.tel }</li>
            </ul>
          </div>
          <div style="width: 30%; float: left; margin-left: 5%;">
            <ul name="fetcherCandidate" class="container" style="min-height: 40px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source"
              data-dojo-props="accept: ['fetcher'], copyState : function(){return false;}">
              <c:forEach var="var" items="${fetcher }">
                <li data-handler="${var.id }" class="dojoDndItem" dndType="fetcher" style="line-height: 25px; margin-top: 3px;">${var.name } ${var.tel }</li>
              </c:forEach>
            </ul>
          </div>
        </div>
      </div>
    </fieldset>
    <fieldset>
      <legend>中转人员(从右边的人员列表拖动到左边方框内进行调度)</legend>
      <div class="simpleTableInfo">
        <div>
          <div style="width: 30%; float: left;">
            <ul name="transiter" class="" style="min-height: 90px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source"
              data-dojo-props="accept: ['transiter'], copyState : function(){return false;}">
              <c:forEach var="var" items="${scheduledTransiter }">
                <li data-handler="${var.id }" class="dojoDndItem" dndType="transiter" style="line-height: 25px; margin-top: 3px;">${var.name } ${var.tel }</li>
              </c:forEach>
            </ul>
          </div>
          <div style="width: 30%; float: left; margin-left: 5%;">
            <ul name="transiterCandidate" class="" style="min-height: 90px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source"
              data-dojo-props="accept: ['transiter'], copyState : function(){return false;}">
              <c:forEach var="var" items="${transiter }">
                <li data-handler="${var.id }" class="dojoDndItem" dndType="transiter" style="line-height: 25px; margin-top: 3px;">${var.name } ${var.tel }</li>
              </c:forEach>
            </ul>
          </div>
        </div>
      </div>
    </fieldset>
    <fieldset>
      <legend>派件人员(从右边的人员列表拖动到左边方框内进行调度)</legend>
      <div class="simpleTableInfo">
        <div>
          <div style="width: 30%; float: left;">
            <ul name="deliverer" class="" style="height: 40px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source"
              data-dojo-props="accept: ['deliverer'], copyState : function(){return false;}">
              <li data-handler="${scheduledDeliverer.id }" class="dojoDndItem" dndType="fetcher" style="line-height: 25px; margin-top: 3px;">${scheduledDeliverer.name } ${scheduledDeliverer.tel }</li>
            </ul>
          </div>
          <div style="width: 30%; float: left; margin-left: 5%;">
            <ul name="delivererCandidate" class="" style="min-height: 40px; border: 1px solid #CCCCCC" data-dojo-type="dojo.dnd.Source"
              data-dojo-props="accept: ['deliverer'], copyState : function(){return false;}">
              <c:forEach var="var" items="${deliverer }">
                <li data-handler="${var.id }" class="dojoDndItem" dndType="deliverer" style="line-height: 25px; margin-top: 3px;">${var.name } ${var.tel }</li>
              </c:forEach>
            </ul>
          </div>
        </div>
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