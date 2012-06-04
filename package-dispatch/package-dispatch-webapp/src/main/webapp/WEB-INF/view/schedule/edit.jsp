<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="schedule_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="orderId" value="${order.id }" /> <input type="hidden" name="orderState" value="${order.state }" />
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
          <div class="dndContainer">
            <c:set var="fetcherDndType" value="fetcher" />
            <c:set var="itemClass" value="dojoDndItem" />
            <c:if test="${scheduledFetcher != null && scheduledFetcher.handled == true }">
              <c:set var="fetcherDndType" value="handledFetcher" />
              <c:set var="itemClass" value="handledItem" />
            </c:if>
            <ul name="fetcher" class="dndTarget" data-dojo-type="dojo.dnd.Source" data-dojo-id="fetcherTarget" data-dojo-props="accept: ['${fetcherDndType }'], copyState : function(){return false;}">
              <c:if test="${scheduledFetcher != null }">
                <li data-handler="${scheduledFetcher.id }" class="item ${itemClass }" dndType="${fetcherDndType }">${scheduledFetcher.name } ${scheduledFetcher.tel } <c:if
                    test="${taskCount[scheduledFetcher.id] != null }">(${taskCount[scheduledFetcher.id] })</c:if> <c:if test="${taskCount[scheduledFetcher.id] == null }">(0)</c:if>
                </li>
              </c:if>
            </ul>
          </div>
          <div class="dndContainer">
            <ul name="fetcherCandidate" class="dndSource" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['fetcher'], copyState : function(){return false;}">
              <c:forEach var="var" items="${fetcher }">
                <li data-handler="${var.id }" class="item dojoDndItem" dndType="fetcher">${var.name } ${var.tel } <c:if test="${taskCount[var.id] != null }">(${taskCount[var.id] })</c:if> <c:if
                    test="${taskCount[var.id] == null }">(0)</c:if>
                </li>
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
          <div class="dndContainer">
            <ul name="transiter" class="dndTarget" style="height: auto; min-height: 90px;" data-dojo-type="dojo.dnd.Source" data-dojo-id="transiterTarget" data-dojo-id="transiterTarget"
              data-dojo-props="accept: ['transiter', 'handledTransiter'], copyState : function(){return false;}">
              <c:if test="${scheduledTransiter != null }">
                <c:forEach var="var" items="${scheduledTransiter }">
                  <c:set var="transiterDndType" value="transiter" />
                  <c:set var="itemClass" value="dojoDndItem" />
                  <c:if test="${var.handled == true }">
                    <c:set var="transiterDndType" value="handledTransiter" />
                    <c:set var="itemClass" value="handledItem" />
                  </c:if>
                  <li data-handler="${var.id }" class="item ${itemClass }" dndType="${transiterDndType }">${var.name } ${var.tel } <c:if test="${taskCount[var.id] != null }">(${taskCount[var.id] })</c:if>
                    <c:if test="${taskCount[var.id] == null }">(0)</c:if>
                  </li>
                </c:forEach>
              </c:if>
            </ul>
          </div>
          <div class="dndContainer">
            <ul name="transiterCandidate" class="dndSource" style="min-height: 90px;" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['transiter'], copyState : function(){return false;}">
              <c:forEach var="var" items="${transiter }">
                <li data-handler="${var.id }" class="item dojoDndItem" dndType="transiter">${var.name } ${var.tel } <c:if test="${taskCount[var.id] != null }">(${taskCount[var.id] })</c:if> <c:if
                    test="${taskCount[var.id] == null }">(0)</c:if>
                </li>
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
          <div class="dndContainer">
            <ul name="deliverer" class="dndTarget" data-dojo-type="dojo.dnd.Source" data-dojo-id="delivererTarget" data-dojo-props="accept: ['deliverer'], copyState : function(){return false;}">
              <c:if test="${scheduledDeliverer != null }">
                <li data-handler="${scheduledDeliverer.id }" class="item dojoDndItem" dndType="deliverer">${scheduledDeliverer.name } ${scheduledDeliverer.tel } <c:if
                    test="${taskCount[scheduledDeliverer.id] != null }">(${taskCount[scheduledDeliverer.id] })</c:if> <c:if test="${taskCount[scheduledDeliverer.id] == null }">(0)</c:if>
                </li>
              </c:if>
            </ul>
          </div>
          <div class="dndContainer">
            <ul name="delivererCandidate" class="dndSource" data-dojo-type="dojo.dnd.Source" data-dojo-props="accept: ['deliverer'], copyState : function(){return false;}">
              <c:forEach var="var" items="${deliverer }">
                <li data-handler="${var.id }" class="item dojoDndItem" dndType="deliverer">${var.name } ${var.tel } <c:if test="${taskCount[var.id] != null }">(${taskCount[var.id] })</c:if> <c:if
                    test="${taskCount[var.id] == null }">(0)</c:if>
                </li>
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