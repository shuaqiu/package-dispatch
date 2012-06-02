<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <c:if test="${user.type == 1 }">
    <%--Type.TYPE_SELF--%>
    <div data-dojo-type="qiuq.widget.ResourceGrid"
      data-dojo-props="
        listGrid : 'customer_list_grid',
        queryInputProp : {
            placeHolder : '编号/登录帐号/姓名/地址'
        },
        storeTarget: 'web/customer/',
        structure: [
            {name: '编号', field: 'code', width: '100px'},
            {name: '登录帐号', field: 'loginAccount', width: '150px'},
            {name: '姓名', field: 'name', width: '150px'},
            {name: '电话', field: 'tel', width: '120px'},
            {name: '公司', field: 'company', width: '200px'},
            {name: '部门', field: 'department', width: '150px'},
            {name: '地址', field: 'address', width: '250px'},
            {name: '客户类型', field: 'customerType', width: '100px', get: 
                function(idx, item){
                    if(item == null){
                        return ''
                    }
                    if(item['type'] == 1){
                        return ''
                    }
                    if(item['customerType'] == 1){
                        return '客户管理员';
                    }
                    return '普通客户';
                } 
            }
        ],
        doCreate:function(){
            require(['qiuq/system/customer'], function(resource){resource.doCreate();});
        },
        doModify:function(){
            require(['qiuq/system/customer'], function(resource){resource.doModify();});
        },
        doDelete:function(){
            require(['qiuq/system/customer'], function(resource){resource.doDelete();});
        }
        ">
    </div>
  </c:if>
  <c:if test="${user.type == 2 }">
    <%--Type.TYPE_CUSTOMER--%>
    <div data-dojo-type="qiuq.widget.ResourceGrid"
      data-dojo-props="
        listGrid : 'customer_list_grid',
        storeTarget: 'web/customer/',
        structure: [
            {name: '编号', field: 'code', width: '100px'},
            {name: '登录帐号', field: 'loginAccount', width: '150px'},
            {name: '姓名', field: 'name', width: '150px'},
            {name: '电话', field: 'tel', width: '120px'},
            {name: '部门', field: 'department', width: '150px'},
            {name: '地址', field: 'address', width: '250px'}
        ],
        doCreate:function(){
            require(['qiuq/system/account'], function(resource){resource.doCreate();});
        },
        doModify:function(){
            require(['qiuq/system/account'], function(resource){resource.doModify();});
        },
        doDelete:function(){
            require(['qiuq/system/account'], function(resource){resource.doDelete();});
        }
        ">
    </div>
  </c:if>
</body>
</html>