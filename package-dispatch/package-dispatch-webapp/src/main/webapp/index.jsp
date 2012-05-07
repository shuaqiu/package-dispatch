<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>慧信企业配送服务系统</title>
<!-- <link rel="stylesheet" href="js/dojo-1.7.2/dijit/themes/tundra/tundra.css" /> -->
<!-- <link rel="stylesheet" href="js/dojo-1.7.2/dojox/grid/enhanced/resources/tundra/EnhancedGrid.css" /> -->
<link rel="stylesheet" href="js/dojo-1.7.2/dijit/themes/claro/claro.css" />
<link rel="stylesheet" href="js/dojo-1.7.2/dojox/grid/enhanced/resources/claro/EnhancedGrid.css" />
<link rel="stylesheet" href="style.css" />
<script type="text/javascript">
    var dojoConfig = {
        async : true,
        parseOnLoad : true,
        baseUrl : "js/",
        /// tlmSiblingOfDojo: false,
        packages : [ {
            name : "dojo",
            location : "dojo-1.7.2/dojo"
        }, {
            name : "dijit",
            location : "dojo-1.7.2/dijit"
        }, {
            name : "dojox",
            location : "dojo-1.7.2/dojox"
        }, {
            name : "qiuq",
            location : "qiuq"
        } ],
        locale : "zh"
    //, extraLocale : [ "en", "zh-tw" ]
    };
</script>
<script src="js/dojo-1.7.2/dojo/dojo.js"></script>
<script type="text/javascript">
    require([
            "dijit/layout/BorderContainer",
            "dijit/layout/TabContainer",
            "dijit/MenuBar",
            "dijit/MenuBarItem",
            "dijit/PopupMenuBarItem",
            "dijit/DropDownMenu",
            "dijit/MenuItem",
            "dijit/form/TextBox",
            "qiuq/order/order" ], function() {
    });

    require([ "qiuq/login", "qiuq/order/alarm", "dojo/domReady!" ], function(login, alarm) {
        login.tryLogin().then(function(){
            setTimeout(function(){
                alarm.startCheck();
            }, 5000);
        });
    });

    function showTab(moduleArr, conf, id, forceReload) {
        require([ "qiuq/tab" ], function(tab) {
            tab.show(moduleArr, conf, id, forceReload);
        });
    }

    function doQuery(evt) {
        if (evt.keyCode != "\n") {
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
</head>
<!-- <body class="tundra"> -->
<body class="claro">
</body>
</html>