<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>慧信企业配送服务系统</title>
<!-- <link rel="stylesheet" href="js/dojo-release-1.7.2/dijit/themes/tundra/tundra.css" /> -->
<!-- <link rel="stylesheet" href="js/dojo-release-1.7.2/dojox/grid/enhanced/resources/tundra/EnhancedGrid.css" /> -->
<link rel="stylesheet" href="js/dojo-release-1.7.2/dojo/resources/dojo.css" />
<link rel="stylesheet" href="js/dojo-release-1.7.2/dojo/resources/dnd.css" />
<link rel="stylesheet" href="js/dojo-release-1.7.2/dijit/themes/claro/claro.css" />
<link rel="stylesheet" href="js/dojo-release-1.7.2/dojox/grid/enhanced/resources/claro/EnhancedGrid.css" />
<link rel="stylesheet" href="js/dojo-release-1.7.2/dojox/form/resources/UploaderFileList.css" />
<link rel="stylesheet" href="style.css" />
<script type="text/javascript">
    var dojoConfig = {
        async : true,
        parseOnLoad : true,
        baseUrl : "js/",
        /// tlmSiblingOfDojo: false,
        packages : [ {
            name : "dojo",
            location : "dojo-release-1.7.2/dojo"
        }, {
            name : "dijit",
            location : "dojo-release-1.7.2/dijit"
        }, {
            name : "dojox",
            location : "dojo-release-1.7.2/dojox"
        }, {
            name : "qiuq",
            location : "qiuq"
        } ],
        locale : "zh"
    //, extraLocale : [ "en", "zh-tw" ]
    };
</script>
<script src="js/dojo-release-1.7.2/dojo/dojo.js"></script>
</head>
<!-- <body class="tundra"> -->
<body class="claro">
</body>
<script type="text/javascript">
    require(["qiuq/login", "dojo/domReady!" ], function(login) {
        login.tryLogin();
    });
    
    function showTab(moduleArr, conf, id, forceReload) {
        require([ "qiuq/tab" ], function(tab) {
            tab.show(moduleArr, conf, id, forceReload);
        });
    }

    function doQuery(evt) {
        if (evt.keyCode != "13") {
            return;
        }
        var value = this.get("value");
        require([ "dojo/_base/Deferred", "dijit/registry" ], function(Deferred, registry) {
            var deferred = new Deferred();
            var orderList = registry.byId("order_list");
            if (orderList) {
                showTab([], {}, "order_list_tab");
                deferred.resolve();
            } else {
                showTab([ "qiuq/order/order" ], {
                    title : "订单查询",
                    href : "web/order/list",
                    onLoad : function() {
                        deferred.resolve();
                    }
                }, "order_list_tab");
            }
            deferred.then(function() {
                registry.byId("order_list").queryWith(value);
            });
        });
    }
</script>
</html>