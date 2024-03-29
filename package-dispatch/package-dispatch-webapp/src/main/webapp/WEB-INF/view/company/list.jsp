<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="qiuq.widget.ResourceGrid"
    data-dojo-props="
        listGrid : 'company_list_grid',
        queryInputProp : {
            placeHolder : '编码/名称/地址'
        },
        storeTarget: 'web/company/',
        structure: [
            {name: '编码', field: 'code', width: '150px'},
            {name: '公司名称', field: 'name', width: '250px'},
            {name: '地址', field: 'address', width: '350px'}
        ],
        doCreate:function(){
            require(['qiuq/system/company'], function(resource){resource.doCreate()});
        },
        doModify:function(){
            require(['qiuq/system/company'], function(resource){resource.doModify()});
        },
        doDelete:function(){
            require(['qiuq/system/company'], function(resource){resource.doDelete()});
        }
        ">
  </div>
</body>
</html>