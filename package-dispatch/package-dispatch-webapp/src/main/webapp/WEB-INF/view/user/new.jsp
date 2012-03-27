<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="user" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <fieldset>
      <legend>客户公司信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="user_new_code">编码: </label></td>
            <td><input id="user_new_code" name="code" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '客户公司的编码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="user_new_name">公司名称: </label></td>
            <td><input id="user_new_name" name="name" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '客户公司的名称', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="user_new_address">地址: </label></td>
            <td><input id="user_new_address" name="address" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '客户公司的地址', required: true" /></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '保存', onClick : function(){require(['qiuq/system/user'], function(user){user.save()})}" /></td>
      </tr>
    </table>
  </form>
</body>
</html>