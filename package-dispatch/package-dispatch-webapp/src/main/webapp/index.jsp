<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>惠信企业配送服务系统</title>
<link rel="stylesheet" href="js/dojo-1.7.2/dijit/themes/tundra/tundra.css" />
<link rel="stylesheet" href="js/dojo-1.7.2/dojox/grid/enhanced/resources/tundra/EnhancedGrid.css" />
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

    require([ "qiuq/login", "dojo/parser", "dojo/dom", "dojo/_base/xhr", "dojo/domReady!" ], function(login, parser,
            dom, xhr) {
        login.isLogined().then(function() {
            fetchMenu();
        }, function() {
            login.doLogin().then(function(json) {
                fetchMenu();
                // dom.byId("showusername").innerHTML = json.obj.name;
            });
        })

        function fetchMenu() {
            xhr.get({
                "url" : "web/menu"
            }).then(function(content) {
                dom.byId("nav").innerHTML = content;
                parser.parse("nav");
            });
        }
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
</head>
<!-- <body class="tundra"> -->
<body class="claro">
  <div id="appLayout" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline'">
    <div id="banner" data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'top'">
      <%--       <div id="showusername">${user.name }</div> --%>
      <div class="searchInputColumn">
        <input id="searchTerms" data-dojo-type="dijit.form.TextBox" data-dojo-props="placeHolder: '搜索订单', onKeyUp: doQuery">
      </div>
      <div id="nav">&nbsp;</div>
    </div>
    <!--     <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="splitter: true, region: 'left'" style="width: 200px;">left</div> -->
    <div id="tab" data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region: 'center', tabPosition: 'top'">
      <!--       <div id="panel_order_list_tab" data-dojo-type="dijit.layout.ContentPane" data-dojo-props="title: '订单查询', href: 'web/order/list'"></div> -->
      <!--       <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="title: 'About2'"> -->
      <!--         <h2>Flickr keyword photo searchddadfafa</h2> -->
      <!--         <p>Each search creates a new tab with the results as thumbnails</p> -->
      <!--         <p>Click on any thumbnail to view the larger image</p> -->
      <!--       </div> -->
    </div>
    <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'bottom'" style="text-align: center;">Copyright © 2012-2012 惠信</div>
  </div>
</body>
</html>