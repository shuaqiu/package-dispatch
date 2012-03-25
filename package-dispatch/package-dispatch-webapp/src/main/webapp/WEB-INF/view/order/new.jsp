<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="order" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <fieldset>
      <legend>寄件人信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="order_new_senderName">姓名: </label></td>
            <td><input id="order_new_senderName" name="senderName" value="${user.name }" data-dojo-type="dijit.form.TextBox" data-dojo-props="placeholder: '寄件人的姓名', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_new_senderTel">电话号码: </label></td>
            <td><input id="order_new_senderTel" name="senderTel" value="${user.tel }" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '寄件人的电话号码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_new_senderAddress">地址: </label></td>
            <td><input id="order_new_senderAddress" name="senderAddress" value="${user.address }" data-dojo-type="dijit.form.ValidationTextBox"
              data-dojo-props="placeholder: '寄件人所在的地址', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_new_senderCompany">公司: </label></td>
            <td><input id="order_new_senderCompany" name="senderCompany" value="${user.company }" data-dojo-type="dijit.form.ValidationTextBox"
              data-dojo-props="placeHolder: '寄件人的公司名称', required: true" /></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <fieldset>
      <legend>收件人信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="order_new_receiverName">姓名: </label></td>
            <td><input id="order_new_receiverName" name="receiverName" data-dojo-type="dijit.form.TextBox"
              data-dojo-props="placeholder: '收件人的姓名', required: true, onKeyUp: function(event){require(['qiuq/order/new'], function(order){order.showSuggestReceiver(event.target)})}" />
              <button data-dojo-type="dijit.form.Button" data-dojo-props="label: '选择', onClick: function(){require(['qiuq/order/new'], function(order){order.popupReceiverList()})}" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_new_receiverTel">电话号码: </label></td>
            <td><input id="order_new_receiverTel" name="receiverTel" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '收件人的电话号码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_new_receiverAddress">地址: </label></td>
            <td><input id="order_new_receiverAddress" name="receiverAddress" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '收件人所在的地址', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_new_receiverCompany">公司: </label></td>
            <td><select id="order_new_receiverCompany" name="receiverCompany" data-dojo-type="dijit.form.ComboBox" data-dojo-props="value: '', placeHolder: '收件人的公司名称', required: true">
                <c:forEach var="var" items="${company }">
                  <option value="${var.key }">${var.value }</option>
                </c:forEach>
            </select></td>
          </tr>
          <tr>
            <td class="labelCell"></td>
            <td><input name="isAddConsignee" data-dojo-type="dijit.form.CheckBox" data-dojo-props="" value="true" /><label for="isAdd">保存收件人</label></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '下单', onClick : function(){require(['qiuq/order/new'], function(order){order.save()})}" /></td>
      </tr>
    </table>
  </form>
</body>
</html>