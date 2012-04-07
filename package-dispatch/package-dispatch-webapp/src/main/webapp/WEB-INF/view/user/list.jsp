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
            {name: '电话', field: 'tel', width: '120px'},
            {name: '部门', field: 'department', width: '250px'}
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