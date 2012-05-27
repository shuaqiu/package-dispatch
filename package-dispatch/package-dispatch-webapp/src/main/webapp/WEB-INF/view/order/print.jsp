<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>订单打印页面</title>
<link rel="stylesheet" href="${pageContext.request.contextPath }/print.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath }/print.css" media="print" />
<style type="text/css" media="screen">
.desc {
    position: absolute;
    top: 0;
    left: 0;
    display: block;
}
</style>
</head>
<body>
  <table id="mainContent">
    <tr>
      <td>&nbsp;<%-- ${order.senderCode } --%></td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>${order.senderCompany }</td>
      <td>&nbsp;<%-- ${order.receiverCode } --%></td>
    </tr>
    <tr>
      <td>
        <table>
          <tr>
            <td>${order.senderName }</td>
            <td>${order.senderTel }</td>
          </tr>
        </table>
      </td>
      <td>${order.receiverCompany }</td>
    </tr>
    <tr>
      <td>${order.senderAddress }</td>
      <td>
        <table>
          <tr>
            <td>${order.receiverName }</td>
            <td>${order.receiverTel }</td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>${order.receiverAddress }</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td rowspan="4" style="padding-left: 0;">
        <table style="height: 100%;">
          <tr>
            <td style="width: 75%;">${order.goodsName }</td>
            <td style="width: 25%;">${order.quantity }</td>
          </tr>
        </table>
      </td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>${order.senderIdentityCode }</td>
      <td>&nbsp;</td>
    </tr>
  </table>
  <div class="desc">
    <button onclick="window.print();">打印 (请先通过打印预览调整页面设置, 设置: 1, 上下左右的边距都为0, 2, 不打印背景)</button>
  </div>
</body>
</html>