<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
.formTable {
    width: 80%;
}

.formTable .labelCell {
    text-align: right;
    width: 35%;
}

em {
    color: red;
}

em:after {
    content: " "
}
</style>
</head>
<body>
  <form name="order" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <fieldset>
      <legend>寄件人信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="order.consigneeName">姓名: </label></td>
            <td><input id="order.consigneeName" name="consigneeName" value="${user.name }" data-dojo-type="dijit.form.TextBox" data-dojo-props="placeholder: '收件人的姓名', required: true" />
              </td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order.consigneeTel">电话号码: </label></td>
            <td><input id="order.consigneeTel" name="consigneeTel" value="${user.tel }" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '收件人的电话号码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order.consigneeAddress">地址: </label></td>
            <td><input id="order.consigneeAddress" name="consigneeAddress" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '收件人所在的地址', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order.consigneeCompany">公司: </label></td>
            <td><select id="order.consigneeCompany" name="consigneeCompany" data-dojo-type="dijit.form.ComboBox" data-dojo-props="value: '', placeHolder: '收件人的公司名称', required: true">
                <c:forEach var="var" items="${company }">
                  <option value="${var.key }">${var.value }</option>
                </c:forEach>
            </select></td>
          </tr>
          <tr>
            <td class="labelCell"></td>
            <td><input name="isAddConsignee" data-dojo-type="dijit.form.CheckBox" data-dojo-props="" value="true"/><label for="isAdd">保存收件人</label></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <fieldset>
      <legend>收件人信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="order.consigneeName">姓名: </label></td>
            <td><input id="order.consigneeName" name="consigneeName" data-dojo-type="dijit.form.TextBox" data-dojo-props="placeholder: '收件人的姓名', required: true" />
              <button data-dojo-type="dijit.form.Button" data-dojo-props="label: '选择'" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order.consigneeTel">电话号码: </label></td>
            <td><input id="order.consigneeTel" name="consigneeTel" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '收件人的电话号码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order.consigneeAddress">地址: </label></td>
            <td><input id="order.consigneeAddress" name="consigneeAddress" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '收件人所在的地址', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order.consigneeCompany">公司: </label></td>
            <td><select id="order.consigneeCompany" name="consigneeCompany" data-dojo-type="dijit.form.ComboBox" data-dojo-props="value: '', placeHolder: '收件人的公司名称', required: true">
                <c:forEach var="var" items="${company }">
                  <option value="${var.key }">${var.value }</option>
                </c:forEach>
            </select></td>
          </tr>
          <tr>
            <td class="labelCell"></td>
            <td><input name="isAddConsignee" data-dojo-type="dijit.form.CheckBox" data-dojo-props="" value="true"/><label for="isAdd">保存收件人</label></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '下单', onClick : function(){window.qiuq.order.save()}" /></td>
      </tr>
    </table>
  </form>
</body>
</html>