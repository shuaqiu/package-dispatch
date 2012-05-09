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
            {name: '员工编号', field: 'code', width: '100px'},
            {name: '登录帐号', field: 'loginAccount', width: '150px'},
            {name: '姓名', field: 'name', width: '150px'},
            {name: '电话', field: 'tel', width: '120px'},
            {name: '短号', field: 'shortNumber', width: '120px'},
            {name: '部门', field: 'department', width: '250px'},
            {name: '角色', field: 'roleId', width: '250px',
                formatter: function(value){
                    switch(value){
                        case 3: 
                            return '收件人员/派件人员';
                        case 4: 
                            return '中转人员';
                        case 2: 
                            return '调度员';
                        case 1: 
                            return '值班经理';
                        case 0: 
                            return '系统管理员';
                    }
                    return '';
                }
            }
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