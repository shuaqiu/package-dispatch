<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>惠信企业配送服务系统</title>
<link rel="stylesheet" href="js/dojo-1.7.2/dijit/themes/tundra/tundra.css" />
<link rel="stylesheet" href="js/dojo-1.7.2/dojox/grid/resources/tundraGrid.css" />
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
        locale : "zh-tw",
        extraLocale : [ "en", "zh-tw" ]
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
            "dijit/MenuItem" ], function() {
    });

    require([ "qiuq/login", "dojo/dom", "dojo/_base/xhr", "dijit/layout/ContentPane", "dojo/domReady!" ],
            function(login, dom, xhr, ContentPane) {
                login.isLogined().then(function() {
                    fetchMenu();
                }, function() {
                    login.doLogin().then(function(json) {
                        fetchMenu();
                        dom.byId("showusername").innerHTML = json.user.name;
                    });
                })

                function fetchMenu() {
                    xhr.get({
                        "url" : "web/menu"
                    }).then(function(content) {
                        dom.byId("nav").innerHTML = "";
                        new ContentPane({
                            "content" : content
                        }).placeAt("nav").startup();
                    });
                }
            });

    function showTab(moduleArr, conf, id, callback) {
        require(["dojo/_base/lang", "qiuq/tab" ], function(lang, tab) {
            var deferred = tab.show(moduleArr, conf, id);
            if(callback && lang.isFunction(callback)){
                deferred.then(callback);
            }
        });
    }
</script>
</head>
<body class="tundra">
  <div id="appLayout" data-dojo-type="dijit.layout.BorderContainer" data-dojo-props="design: 'headline'">
    <div id="banner" data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'top'">
      <div id="showusername"></div>
      <div class="searchInputColumn">
        <div class="searchInputColumnInner">
          <input id="searchTerms" placeholder="search terms"><button id="searchBtn">Search</button>
        </div>
      </div>
      <div id="nav">&nbsp;</div>
    </div>
    <!--     <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="splitter: true, region: 'left'" style="width: 200px;">left</div> -->
    <div id="tab" data-dojo-type="dijit.layout.TabContainer" data-dojo-props="region: 'center', tabPosition: 'top'">
      <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="title: 'About'">

        <h2>Flickr keyword photo search</h2>
        <p>Each search creates a new tab with the results as thumbnails</p>
        <p>Click on any thumbnail to view the larger image</p>

      </div>
      <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="title: 'About2'">

        <h2>Flickr keyword photo searchddadfafa</h2>
        <p>Each search creates a new tab with the results as thumbnails</p>
        <p>Click on any thumbnail to view the larger image</p>

      </div>
    </div>
    <div data-dojo-type="dijit.layout.ContentPane" data-dojo-props="region: 'bottom'" style="text-align: center;">版权所有, 惠信企业 @copy 2012-2012</div>
  </div>
</body>
</html>