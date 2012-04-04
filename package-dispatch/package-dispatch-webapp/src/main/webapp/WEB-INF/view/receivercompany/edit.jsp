<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="receivercompany_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="id" />
    <input type="hidden" name="userId" value="${user.id }" />
    <fieldset>
      <legend>收件公司信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="company_editing_name">公司名称: </label></td>
            <td><input id="company_editing_name" name="name" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '客户公司的名称', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="company_editing_address">地址: </label></td>
            <td><input id="company_editing_address" name="address" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '客户公司的地址', required: true" /></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '保存', onClick : function(){require(['qiuq/customer/receivercompany'], function(resource){resource.doSave();});}" /></td>
      </tr>
    </table>
  </form>
</body>
</html>