<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="qiuq.widget.ResourceGrid"
    data-dojo-props="
        listGrid : 'user_list_grid',
        storeTarget: 'web/user/',
        structure: [
            {name: '登录账户', field: 'code', width: '150px'},
            {name: '姓名', field: 'name', width: '150px'},
            {name: '电话', field: 'tel', width: '100px'},
            {name: '公司', field: 'company', width: '200px'},
            {name: '部门', field: 'department', width: '150px'},
            {name: '地址', field: 'address', width: '250px'},
            {name: '类型', field: 'type', width: '100px', get: function(value){if(value == 1){return '惠信员工';}else{return '客户';}} },
            {name: '客户类型', field: 'customerType', width: '100px', get: function(value){if(value == 1){return '客户管理员';}else{return '普通客户';}} }
        ],
        doCreate:function(){
            require(['qiuq/system/user'], function(resource){resource.doCreate();});
        },
        doModify:function(){
            require(['qiuq/system/user'], function(resource){resource.doModify();});
        },
        doDelete:function(){
            require(['qiuq/system/user'], function(resource){resource.doDelete();});
        }
        ">
  </div>
</body>
</html>