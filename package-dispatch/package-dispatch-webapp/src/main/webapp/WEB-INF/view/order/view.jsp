<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<body>
  <div class="details">
    <div class="detail">
      <div>${order.senderName }</div>
      <div>${order.senderTel }</div>
      <div>${order.senderAddress }</div>
    </div>

    <c:forEach var="var" items="${handleDetail }">
      <div class="arrow down"></div>
      <div class="detail">
        <div>${var.handlerName }</div>
        <div>${var.handlerTel }</div>
        <div>${var.handleTime }</div>
      </div>
    </c:forEach>
    <c:if test="${order.state == DELIVERED }">
      <div class="arrow down"></div>
      <div class="detail">
        <div>${order.receiverName }</div>
        <div>${order.receiverTel }</div>
        <div>${order.receiverAddress }</div>
      </div>
    </c:if>
  </div>
  <div>
    <!-- 加上这一个div, 是为了使得外层的ContentPane 不是只有一个子结点, 不增加overflow: hidden 的样式, 从而可以出现滚动条 -->
  </div>
</body>
</html>