<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="order_editing_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <input type="hidden" name="senderId" value="${user.id }" /><input type="hidden" name="senderCompanyId" value="${user.companyId }" /> <input type="hidden" name="receiverId" />
    <fieldset style="display: none;">
      <legend>寄件人信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_senderName">姓名: </label></td>
            <td><input id="order_editing_senderName" name="senderName" value="${user.name }" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '寄件人的姓名', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_senderTel">电话号码: </label></td>
            <td><input id="order_editing_senderTel" name="senderTel" value="${user.tel }" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '寄件人的电话号码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_senderCompany">公司: </label></td>
            <td><input id="order_editing_senderCompany" name="senderCompany" value="${user.company }" data-dojo-type="dijit.form.ValidationTextBox"
              data-dojo-props="placeHolder: '寄件人的公司名称', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_senderAddress">地址: </label></td>
            <td><input id="order_editing_senderAddress" name="senderAddress" value="${user.address }" data-dojo-type="dijit.form.ValidationTextBox"
              data-dojo-props="placeHolder: '寄件人所在的地址', required: true" /></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <fieldset>
      <legend>收件人信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_receiverName">姓名: </label></td>
            <td><input id="order_editing_receiverName" name="receiverName" data-dojo-type="dijit.form.ValidationTextBox"
              data-dojo-props="placeHolder: '收件人的姓名', required: true,
                    onKeyUp: function(event){
                        require(['qiuq/order/order'], function(order){
                            order.suggest(event.target);
                            order.onReceiverKeyUp();
                        });
                    }" />
              <button data-dojo-type="dijit.form.Button" data-dojo-props="label: '选择', onClick: function(){require(['qiuq/order/order'], function(selection){selection.showSelectionDialog();});}" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_receiverTel">电话号码: </label></td>
            <td><input id="order_editing_receiverTel" name="receiverTel" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '收件人的电话号码', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_receiverCompany">公司: </label></td>
            <td><input id="order_editing_receiverCompany" name="receiverCompany" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '收件人的公司名称', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_receiverAddress">地址: </label></td>
            <td><input id="order_editing_receiverAddress" name="receiverAddress" data-dojo-type="dijit.form.ValidationTextBox" data-dojo-props="placeHolder: '收件人所在的地址', required: true" /></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <fieldset>
      <legend>物品信息</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_goodsName">物品名称: </label></td>
            <td><textarea id="order_editing_goodsName" name="goodsName" data-dojo-type="dijit.form.Textarea" data-dojo-props="required: true"></textarea></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="order_editing_quantity">数量: </label></td>
            <td><textarea id="order_editing_quantity" name="quantity" data-dojo-type="dijit.form.Textarea" data-dojo-props="required: true"></textarea></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td>
          <button data-dojo-type="dijit.form.Button" data-dojo-props="label: '下单', onClick : function(){require(['qiuq/order/order'], function(resource){resource.doSave()})}" />
        </td>
      </tr>
    </table>
  </form>
  <div>
    <!-- 加上这一个div, 是为了使得外层的ContentPane 不是只有一个子结点, 不增加overflow: hidden 的样式, 从而可以出现滚动条 -->
  </div>
</body>
</html>