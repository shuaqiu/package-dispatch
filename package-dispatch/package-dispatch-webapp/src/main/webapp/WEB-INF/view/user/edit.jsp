<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="user_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="id" /> <input type="hidden" name="companyId" value="0" /> <input type="hidden" name="company" value="惠信" /> <input type="hidden" name="type" value="1" />
    <%-- 惠信员工 --%>
    <fieldset>
      <legend>员工信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="user_editing_code">员工编号: </label></td>
            <td><input id="user_editing_code" name="code" readonly="readonly" value="${generatedCode }" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '员工的登录帐号', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="user_editing_alias">登录帐号: </label></td>
            <td><input id="user_editing_alias" name="alias" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '员工的登录帐号', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="user_editing_name">姓名: </label></td>
            <td><input id="user_editing_name" name="name" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '员工的姓名', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="user_editing_password">密码: </label></td>
            <td><input id="user_editing_password" name="password" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '员工的密码', type: 'password', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="user_editing_tel">电话: </label></td>
            <td><input id="user_editing_tel" name="tel" maxlength="20" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '员工的手机号码或电话号码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><label for="user_editing_short_number">短号: </label></td>
            <td><input id="user_editing_short_number" name="short_number" maxlength="20" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '员工的短号'" /></td>
          </tr>
          <tr>
            <td class="labelCell"><label for="user_editing_department">部门: </label></td>
            <td><input id="user_editing_department" name="department" data-dojo-type="dijit.form.TextBox" data-dojo-props="placeHolder: '员工所在的部门'" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="user_editing_role">角色: </label></td>
            <td><select id="user_editing_role" name="role_id" data-dojo-type="dijit.form.Select" data-dojo-props="placeHolder: '员工的角色'">
                <option value="3">收件人员/派件人员</option>
                <option value="4">中转人员</option>
                <option value="2">调度员</option>
                <option value="1">值班经理</option>
                <c:if test="${user.id == 0 }">
                  <option value="0">系统管理员</option>
                </c:if>
            </select></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '保存', onClick : function(){require(['qiuq/system/user'], function(resource){resource.doSave();});}" /></td>
      </tr>
    </table>
  </form>
</body>
</html>