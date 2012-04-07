<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="role_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="id" />
    <fieldset>
      <legend>角色信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="role_editing_code">登录帐号: </label></td>
            <td><input id="role_editing_code" name="code" disabled="disabled" data-dojo-type="dijit.form.ValidationTextBox" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="role_editing_name">姓名: </label></td>
            <td><input id="role_editing_name" name="name" disabled="disabled" data-dojo-type="dijit.form.ValidationTextBox" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="role_editing_tel">电话: </label></td>
            <td><input id="role_editing_tel" name="tel" maxlength="20" disabled="disabled" data-dojo-type="dijit.form.ValidationTextBox" /></td>
          </tr>
          <tr>
            <td class="labelCell"><label for="role_editing_department">部门: </label></td>
            <td><input id="role_editing_department" name="department" disabled="disabled" data-dojo-type="dijit.form.TextBox" /></td>
          </tr>
          <tr>
            <td class="labelCell"><label for="role_editing_role">角色: </label></td>
            <td><select id="role_editing_role" name="role_id" data-dojo-type="dijit.form.Select" data-dojo-props="placeHolder: '用户的角色'">
                <option value="3">收件人员</option>
                <option value="4">中转人员</option>
                <option value="5">派件人员</option>
                <option value="2">调度员</option>
                <option value="1">值班经理</option>
                <option value="0">系统管理员</option>
            </select></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '保存', onClick : function(){require(['qiuq/system/role'], function(resource){resource.doSave();});}" /></td>
      </tr>
    </table>
  </form>
</body>
</html>