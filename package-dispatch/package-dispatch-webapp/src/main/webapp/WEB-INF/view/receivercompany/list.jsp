<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="qiuq.widget.ResourceGrid"
    data-dojo-props="
        listGrid : 'receivercompany_list_grid',
        storeTarget: 'web/receivercompany/',
        structure: [
            {name: '公司名称', field: 'name', width: '250px'},
            {name: '地址', field: 'address', width: '350px'}
        ],
        doCreate:function(){
            require(['qiuq/customer/receivercompany'], function(resource){resource.doCreate()});
        },
        doModify:function(){
            require(['qiuq/customer/receivercompany'], function(resource){resource.doModify()});
        },
        doDelete:function(){
            require(['qiuq/customer/receivercompany'], function(resource){resource.doDelete()});
        }
        ">
  </div>
</body>
</html>