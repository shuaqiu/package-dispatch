<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="qiuq.widget.ResourceGrid"
    data-dojo-props="
        id : 'order_list',
        listGrid : 'order_list_grid',
        storeTarget: 'web/order/',
        structure: [
            {name: '操作', width: '70px', get: function(idx, item){
                return new dijit.form.Button({
                    label : '查看',
                    onClick : function(){
                        require(['qiuq/order/order'], function(resource){
                            resource.doView(item['id']);
                        });
                    }
                });
            }},
            {name: '下单时间', field: 'orderTime', width: '170px', formatter: function(value){
                var d = new Date();
                d.setTime(value);
                return dojo.date.locale.format(d, {datePattern: 'yyyy-MM-dd', timePattern: 'HH:mm:ss'});
            }},
            {name: '状态', field: 'stateDescribe', width: '150px'},
            {name: '条形码', field: 'barCode', width: '100px'},
<c:if test="${user.type ==  1}"><%--Type.TYPE_SELF--%>
            {name: '发件人', field: 'senderName', width: '100px'},
            {name: '发件人电话', field: 'senderTel', width: '120px'},
            {name: '发件人公司', field: 'senderCompany', width: '200px'},
            {name: '发件人地址', field: 'senderAddress', width: '200px'},
</c:if>
            {name: '收件人', field: 'receiverName', width: '100px'},
            {name: '收件人电话', field: 'receiverTel', width: '120px'},
            {name: '收件人公司', field: 'receiverCompany', width: '200px'},
            {name: '收件人地址', field: 'receiverAddress', width: '200px'},
            {name: '物品', field: 'goodsName', width: '150px'},
            {name: '数量', field: 'quantity', width: '150px'}
        ]
        ">
  </div>
</body>
</html>