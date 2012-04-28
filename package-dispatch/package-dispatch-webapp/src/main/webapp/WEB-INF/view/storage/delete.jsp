<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <form name="storage_out_form" method="post" data-dojo-type="dijit.form.Form" data-dojo-props="">
    <fieldset>
      <legend>出库</legend>
      <div>
        <table class="formTable">
          <tr>
            <td class="labelCell"><em>*</em><label for="storage_out_handler">中转/派单人员编号: </label></td>
            <td><input id="storage_out_handler" data-dojo-type="dijit.form.TextBox" data-dojo-props="placeHolder: '中转/派单人员的编号', required: true" /></td>
          </tr>
          <tr>
            <td class="labelCell"><em>*</em><label for="storage_out_barcode">条形码(一行一个): </label></td>
            <td><textarea id="storage_out_barcode" data-dojo-type="dijit.form.Textarea" data-dojo-props="required: true"></textarea></td>
          </tr>
        </table>
      </div>
    </fieldset>
    <table class="formTable">
      <tr>
        <td class="labelCell"></td>
        <td><button data-dojo-type="dijit.form.Button" data-dojo-props="label: '出库', onClick : function(){require(['qiuq/order/storage'], function(resource){resource.doDelete()})}" /></td>
      </tr>
    </table>
  </form>
  <div>
    <!-- 加上这一个div, 是为了使得外层的ContentPane 不是只有一个子结点, 不增加overflow: hidden 的样式, 从而可以出现滚动条 -->
  </div>
</body>
</html>