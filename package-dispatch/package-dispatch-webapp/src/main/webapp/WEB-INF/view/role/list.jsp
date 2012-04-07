<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="qiuq.widget.ResourceGrid"
    data-dojo-props="
        listGrid : 'role_list_grid',
        storeTarget: 'web/role/',
        structure: [
            {name: '登录账户', field: 'code', width: '150px'},
            {name: '姓名', field: 'name', width: '150px'},
            {name: '电话', field: 'tel', width: '120px'},
            {name: '部门', field: 'department', width: '250px'},
            {name: '角色', field: 'role_id', width: '250px',
                formatter: function(value){
                    switch(value){
                        case 3: 
                            return '收件人员';
                        case 4: 
                            return '中转人员';
                        case 5: 
                            return '派件人员';
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
        doModify:function(){
            require(['qiuq/system/role'], function(resource){resource.doModify();});
        }
        ">
  </div>
</body>
</html>