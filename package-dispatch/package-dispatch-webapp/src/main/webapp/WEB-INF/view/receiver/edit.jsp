<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="receiver_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="id" />
    <c:if test="${user.type == 1 }">
      <%--Type.TYPE_SELF --%>
      <input type="hidden" name="userCompanyId" />
    </c:if>
    <c:if test="${user.type == 2 }">
      <%--Type.TYPE_CUSTOMER --%>
      <input type="hidden" name="userCompanyId" value="${user.companyId }" />
      <input type="hidden" name="userCompany" value="${user.company }" />
    </c:if>
    <fieldset>
      <legend>收件人信息</legend>
      <div>
        <table class="formTable">
          <c:if test="${user.type == 1 }">
            <%--Type.TYPE_SELF --%>
            <tr>
              <td class="labelCell"><em>*</em><label for="receiver_editing_userCompany">客户公司: </label></td>
              <td><input id="receiver_editing_userCompany" name="userCompany" readonly="readonly" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '客户的公司', required: true" />
                <button data-dojo-type="dijit.form.Button" data-dojo-props="label: '选择', onClick: function(){require(['qiuq/customer/receiver'], function(selection){selection.showSelectionDialog()})}" /></td>
            </tr>
          </c:if>
          <tr>
            <td class="labelCell"><em>*</em><label for="receiver_editing_name">姓名: </label></td>
            <td><input id="receiver_editing_name" name="name" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '收件人的姓名', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="receiver_editing_tel">电话: </label></td>
            <td><input id="receiver_editing_tel" name="tel" maxlength="20" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '收件人的手机号码或电话号码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="receiver_editing_company">收件人公司: </label></td>
            <td><input id="receiver_editing_company" name="company" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '收件人的公司', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="receiver_editing_address">收件人地址: </label></td>
            <td><input id="receiver_editing_address" name="address" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '收件人的地址', required: true" /></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '保存', onClick : function(){require(['qiuq/customer/receiver'], function(resource){resource.doSave();});}" /></td>
      </tr>
    </table>
  </form>
</body>
</html>