<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="receiver_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="id" />
    <input type="hidden" name="userId" value="${user.id }" />
    <input type="hidden" name="companyId" />
    <fieldset>
      <legend>收件人信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="receiver_new_name">姓名: </label></td>
            <td><input id="receiver_new_name" name="name" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '收件人的姓名', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="receiver_new_tel">电话: </label></td>
            <td><input id="receiver_new_tel" name="tel" maxlength="20" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '收件人的手机号码或电话号码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="receiver_new_company">公司: </label></td>
            <td><input id="receiver_new_company" name="company" data-dojo-type="dijit.form.ValidationTextBox"
                data-dojo-props="placeholder: '收件人的公司', required: true, onClick: function(){require(['qiuq/customer/receiver'], function(receiver){receiver.showSelectionDialog();});}" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="receiver_new_address">地址: </label></td>
            <td><input id="receiver_new_address" name="address" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeholder: '收件人的地址', required: true" /></td>
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