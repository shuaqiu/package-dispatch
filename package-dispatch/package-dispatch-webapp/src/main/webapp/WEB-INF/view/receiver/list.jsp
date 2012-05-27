<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
  <div data-dojo-type="qiuq.widget.ResourceGrid"
    data-dojo-props="
        listGrid : 'receiver_list_grid',
        queryInputProp : {
            placeHolder : '姓名/电话/公司/地址'
        },
        extButtons : new dijit.form.Button({
            label : '导入',
            iconClass : 'dijitIconTable',
            onClick : function(){
                require(['qiuq/customer/receiver'], function(resource){resource.doImport()});
            }
        }),
        storeTarget: 'web/receiver/',
        structure: [
<c:if test="${user.type == 1 }"><%--Type.TYPE_SELF --%>
            {name: '客户公司', field: 'userCompany', width: '250px'},
</c:if>
            {name: '姓名', field: 'name', width: '150px'},
            {name: '电话', field: 'tel', width: '120px'},
            {name: '收件人公司', field: 'company', width: '250px'},
            {name: '地址', field: 'address', width: '250px'}
        ],
        doCreate : function(){
            require(['qiuq/customer/receiver'], function(resource){resource.doCreate()});
        },
        doModify : function(){
            require(['qiuq/customer/receiver'], function(resource){resource.doModify()});
        },
        doDelete : function(){
            require(['qiuq/customer/receiver'], function(resource){resource.doDelete()});
        }
        ">
  </div>
</body>
</html>