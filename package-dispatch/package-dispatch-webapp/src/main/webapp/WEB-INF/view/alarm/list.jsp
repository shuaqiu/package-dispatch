<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="qiuq.widget.ResourceGrid"
    data-dojo-props="
        id : 'alarm_list',
        listGrid : 'alarm_list_grid',
        queryInputProp : {
            placeHolder : '订单号/姓名/电话'
        },
        _onRowDblClick : function(evt) {
            // 双击进行查看
            var idx = evt.rowIndex;
            var item = this._grid.getItem(idx);
            require(['qiuq/order/order'], function(resource){
                resource.doView(item['id']);
            });
        },
        storeTarget: 'web/alarm/',
        structure: [
            {name : '操作', field : 'id', width : '70px', get : function(idx, item){
                return new dijit.form.Button({
                    label : '查看',
                    onClick : function(){
                        require(['qiuq/order/order'], function(resource){
                            resource.doView(item['id']);
                        });
                    }
                });
            }},
            {name: '收件时间/出库时间', field: 'fetchTime', width: '155px', formatter: function(value){
                if(value == null) {
                    return '';
                }
                var d = new Date();
                d.setTime(value);
                return dojo.date.locale.format(d, {datePattern: 'yyyy-MM-dd', timePattern: 'HH:mm:ss'});
            }},
            {name: '调度时间', field: 'scheduleTime', width: '155px', formatter: function(value){
                if(value == null) {
                    return '';
                }
                var d = new Date();
                d.setTime(value);
                return dojo.date.locale.format(d, {datePattern: 'yyyy-MM-dd', timePattern: 'HH:mm:ss'});
            }},
            {name: '用时 (分钟)', field: 'fetchTime', width: '100px', get: function(idx, item){
                if(item == null) {
                    return '';
                }
                var time = item['fetchTime'] || item['scheduleTime'] || 0;
                var c = new Date().getTime();
                return parseInt((c - time) / 60 / 1000);
            }},
            {name: '状态', field: 'stateDescribe', width: '150px'},
            {name: '条形码', field: 'barCode', width: '100px'},
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