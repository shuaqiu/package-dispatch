<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="dijit.MenuBar">
    <div data-dojo-type="dijit.MenuBarItem"
      data-dojo-props="onClick: function(){
            require(['qiuq/order/order'], function(order){
                order.doView(${order.id});
            });
        }">刷新</div>
    <c:if test="${user.type == 1 && function.schedule && order.state >= 0 && order.state < DELIVERED}">
      <div data-dojo-type="dijit.MenuBarItem"
        data-dojo-props="onClick: function(){
            require(['qiuq/order/schedule'], function(schedule){
                schedule.doModify(${order.id});
            });
        }">
        <c:if test="${order.state == 0 }">调度</c:if>
        <c:if test="${order.state >=1 }">修改调度</c:if>
      </div>
    </c:if>
    <c:if test="${user.type == 1 && order.state >= 1 && order.state < DELIVERED}">
      <div data-dojo-type="dijit.MenuBarItem"
        data-dojo-props="onClick: function(){
            require(['qiuq/order/order'], function(order){
                order.close(${order.id});
            });
        }">关闭订单</div>
    </c:if>
    <c:if test="${user.type == 2 && order.state <= 1}">
      <div data-dojo-type="dijit.MenuBarItem"
        data-dojo-props="onClick: function(){
            require(['qiuq/order/order'], function(order){
                order.cancel(${order.id});
            });
        }">取消订单</div>
    </c:if>
  </div>
  <div class="details">
    <div class="desc">
      <span>处理情况</span>
      <c:if test="${order.barCode != null }">: <span class="bolder">${order.barCode }</span>
      </c:if>
    </div>
    <div class="detail">
      <div>${order.senderName }(${order.senderTel })</div>
      <div>${order.senderAddress }</div>
    </div>
    <c:forEach var="var" items="${handleDetail }">
      <div class="arrow"></div>
      <div class="detail">
        <c:if test="${user.type == 1 }">
          <%--Type.TYPE_SELF--%>
          <div>${var.handlerName }(${var.handlerTel })</div>
        </c:if>
        <div>${var.description }</div>
        <div>
          <fmt:formatDate value="${var.handleTime }" pattern="yyyy-MM-dd HH:mm:ss" />
        </div>
      </div>
    </c:forEach>
    <c:if test="${order.state == DELIVERED }">
      <div class="arrow"></div>
      <div class="detail">
        <div>${order.receiverName }(${order.receiverTel })</div>
        <div>${order.receiverAddress }</div>
      </div>
    </c:if>
  </div>
  <c:if test="${user.type == 1 && order.state >= 1}">
    <%--Type.TYPE_SELF--%>
    <div class="details schedule">
      <div class="desc">调度信息</div>
      <div class="detail">
        <div>${order.senderName }(${order.senderTel })</div>
        <div>${order.senderAddress }</div>
      </div>
      <c:forEach var="var" items="${scheduleDetail }">
        <div class="arrow"></div>
        <div class="detail">
          <div>&nbsp;</div>
          <div>${var.handlerName }(${var.handlerTel })</div>
          <div>&nbsp;</div>
        </div>
      </c:forEach>
      <div class="arrow"></div>
      <div class="detail">
        <div>${order.receiverName }(${order.receiverTel })</div>
        <div>${order.receiverAddress }</div>
      </div>
    </div>
  </c:if>
  <div>
    <!-- 加上这一个div, 是为了使得外层的ContentPane 不是只有一个子结点, 不增加overflow: hidden 的样式, 从而可以出现滚动条 -->
  </div>
</body>
</html>