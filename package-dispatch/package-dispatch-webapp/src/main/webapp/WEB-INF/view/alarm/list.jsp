<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="qiuq.widget.ResourceGrid"
    data-dojo-props="
        listGrid : 'alarm_list_grid',
        storeTarget: 'web/alarm/',
        structure: [
            {name: '收件时间/出库时间', field: 'fetchTime', width: '170px', formatter: function(value){
                var d = new Date();
                d.setTime(value);
                return dojo.date.locale.format(d, {datePattern: 'yyyy-MM-dd', timePattern: 'HH:mm:ss'});
            }},
            {name: '用时 (分钟)', field: 'fetchTime', width: '100px', get: function(idx, item){
                var c = new Date().getTime();
                return parseInt((c - item['fetchTime']) / 60 / 1000);
            }},
            {name: '发件人', field: 'barCode', width: '100px'},
            {name: '发件人', field: 'senderName', width: '100px'},
            {name: '发件人电话', field: 'senderTel', width: '120px'},
            {name: '发件人公司', field: 'senderCompany', width: '200px'},
            {name: '发件人地址', field: 'senderAddress', width: '200px'},
            {name: '收件人', field: 'receiverName', width: '100px'},
            {name: '收件人电话', field: 'receiverTel', width: '120px'},
            {name: '收件人公司', field: 'receiverCompany', width: '200px'},
            {name: '收件人地址', field: 'receiverAddress', width: '200px'},
            {name: '物品', field: 'goodsName', width: '150px'},
            {name: '数量', field: 'quantity', width: '150px'}
        ],
        ">
  </div>
</body>
</html>