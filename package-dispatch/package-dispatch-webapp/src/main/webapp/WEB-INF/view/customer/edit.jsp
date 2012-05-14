<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="customer_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="id" />
    <c:if test="${user.type == 1 }">
      <%--Type.TYPE_SELF --%>
      <input type="hidden" name="companyId" />
    </c:if>
    <c:if test="${user.type == 2 }">
      <%--Type.TYPE_CUSTOMER --%>
      <input type="hidden" name="companyId" value="${user.companyId }" />
      <input type="hidden" name="company" value="${user.company }" />
      <input type="hidden" name="customerType" value="0" />
      <%--普通客户 --%>
    </c:if>
    <input type="hidden" name="type" value="2" />
    <%--Type.TYPE_CUSTOMER --%>
    <%-- 客户 --%>
    <fieldset>
      <c:if test="${user.type == 1 }">
        <legend>客户信息</legend>
      </c:if>
      <c:if test="${user.type == 2 }">
        <legend>账号信息</legend>
      </c:if>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><label for="customer_editing_code">编号: </label></td>
            <td><input id="customer_editing_code" name="code" readonly="readonly" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '编号由系统自动生成'" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="customer_editing_alias">登录帐号: </label></td>
            <td><input id="customer_editing_alias" name="loginAccount" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '用户的登录帐号', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="customer_editing_name">姓名: </label></td>
            <td><input id="customer_editing_name" name="name" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '用户的姓名', required: true" /></td>
          </tr>
          <tr id="customer_editing_password_row">
            <td class="labelCell"><em>*</em><label for="customer_editing_password">密码: </label></td>
            <td><input id="customer_editing_password" name="password" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '用户的密码', type: 'password', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="customer_editing_tel">电话: </label></td>
            <td><input id="customer_editing_tel" name="tel" maxlength="20" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '用户的手机号码或电话号码', required: true" /></td>
          </tr>
          <c:if test="${user.type == 1 }">
            <%--Type.TYPE_SELF--%>
            <tr>
              <td class="labelCell"><em>*</em><label for="customer_editing_company">公司: </label></td>
              <td><input id="customer_editing_company" name="company" data-dojo-type="dijit.form.ValidationTextBox"
                data-dojo-props="placeHolder: '用户的公司', required: true,
                    onKeyUp: function(event){
                        require(['qiuq/system/customer'], function(customer){
                            customer.suggest(event.target);
                            customer.onCompanyKeyUp();
                        });
                    }" />
                <button data-dojo-type="dijit.form.Button" data-dojo-props="label: '选择', onClick: function(){require(['qiuq/system/customer'], function(selection){selection.showSelectionDialog()})}" /></td>
            </tr>
          </c:if>
          <tr>
            <td class="labelCell"><label for="customer_editing_department">部门: </label></td>
            <td><input id="customer_editing_department" name="department" data-dojo-type="dijit.form.TextBox" data-dojo-props="placeHolder: '用户所在的部门'" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="customer_editing_address">地址: </label></td>
            <td><input id="customer_editing_address" name="address" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '用户的地址', required: true" /></td>
          </tr>
          <c:if test="${user.type == 1 }">
            <tr>
              <td class="labelCell"><label for="customer_editing_customerType">客户类型: </label></td>
              <td><select id="customer_editing_customerType" name="customerType" data-dojo-type="dijit.form.Select" data-dojo-props="placeHolder: '客户类型'">
                  <option value="0">普通客户</option>
                  <option value="1">客户管理员</option>
              </select></td>
            </tr>
          </c:if>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '保存', onClick : function(){require(['qiuq/system/customer'], function(resource){resource.doSave();});}" /></td>
      </tr>
    </table>
  </form>
</body>
</html>