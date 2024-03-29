{
	"id": "multiSceneApp",
	"name": "Multi Scene App",
	"description": "A multiSceneApp",
	"splash": "splash",

	"dependencies": [
        "dojox/mobile/View", // Temporary work around for getting a null when
								// calling getParent
		"dojox/mobile/TabBar",
		"dojox/mobile/RoundRect",
		"dojox/mobile/TabBarButton",
		"dojox/mobile/Button",
		"dojox/mobile/RoundRect",
		"dojox/mobile/Heading"
	],
	// Modules for the app. The are basically used as the second
	// array of mixins in a dojo.declare(). Modify the top level behavior
	// of the app, how it processes the config or any other life cycle
	// by creating and including one or more of these
	"modules": [
		"dojox/app/module/env",
		"dojox/app/module/history",
        "dojox/app/module/lifecycle"
		// "dojox/app/module/phonegap",
		// "dojox/app/module/somePlugin"
	],

	// stores we are using
	"stores": {},

	"template": "application.html",

	// models and instantiation parameters for the models. Including 'type' as a
	// property allows
	// one to overide the class that will be used for the model. By default it
	// is dojox/mvc/model
	"models": {}, 

	// the name of the scene to load when the app is initialized.
	"defaultView": "home", 

	"defaultTransition": "slide",
	// scenes are groups of views and models loaded at once
	"views": {

		// simple scene which loads all views and shows the default first
		"home": { 
			"type": "dojox.app.view",
			"dependencies":["dojox/mobile/RoundRectList","dojox/mobile/ListItem", "dojox/mobile/EdgeToEdgeCategory"],
			"template": "views/simple/home.html"
		},

		"main":{
			// all views in the main scene will be bound to the user model
			"models": [],
			"type": "dojox.app.scene",
			"template": "simple.html",	
			"defaultView": "main",
			"defaultTransition": "slide",
			// the views available to this scene
			"views": { 
				"main":{
					"template": "views/simple/main.html"
				},
				"second":{
					"template": "views/simple/second.html" 
				},
				"third":{
					"template": "views/simple/third.html" 
				}
			},
			"dependencies":["dojox/mobile/RoundRectList","dojox/mobile/ListItem","dojox/mobile/EdgeToEdgeCategory","dojox/mobile/EdgeToEdgeList"]
		},
		// simple scene which loads all views and shows the default first
		"tabscene": { 
			// all views in the second scene will be bound to the user model
			"models": [],
			"template": "tabScene.html",	
			"defaultView": "tab1",
			"defaultTransition": "flip",
			"type": "dojox.app.scene",
			// the views available to this scene
			"views": { 
				"tab1":{
					"template": "views/tabs/tab1.html" 
				},
				"tab2":{
					"template": "views/tabs/tab2.html" 
				},
				"tab3":{
					"template": "views/tabs/tab3.html" 
				}
			},
			"dependencies":["dojox/mobile/RoundRectList","dojox/mobile/ListItem", "dojox/mobile/EdgeToEdgeCategory"]
		},
		// simple scene which loads all views and shows the default first
		"gallery": { 
			// all views in the main scene will be bound to the user model
			"models": [],
	
			"defaultView": "welcome",
			"defaultTransition": "flip",
			"type": "dojox.app.scene",
			"template": "gallery.html",
			// the views available to this scene
			"views": { 
				"welcome": {
					"template": "views/gallery/welcome.html"
				},
				"tabbar": {
					"template": "views/gallery/tabbar.html"
				},
				"navigation": {
					"template": "views/gallery/navigation.html"
				},
				"map": {
					"template": "views/gallery/map.html"
				},
				"list": {
					"template": "views/gallery/list.html"
				},
				"jsonp": {
					"template": "views/gallery/jsonp.html"
				},
				"icons": {
					"template": "views/gallery/icons.html"
				},
				"headings": {
					"template": "views/gallery/headings.html"
				},
				"forms": {
					"template": "views/gallery/forms.html"
				},
				"flippableViews": {
					"template": "views/gallery/flippableViews.html"
				},
				"buttons": {
					"type": "dojox.app.scene",
					"template": "views/gallery/buttonScene.html",
					"defaultView": "tab1",
					"views": {
						"tab1":{
							"template": "views/gallery/buttons/tab1.html"
						},
						"tab2":{
							"template": "views/gallery/buttons/tab2.html"
						},
						"tab3":{
							"template": "views/gallery/buttons/tab3.html"
						}
					}
				},
				"animations": {
					"template": "views/gallery/animations.html"
				},
				"ajaxLoad": {
					"template": "views/gallery/ajaxLoad.html"
				},
				"ajax": {
					"template": "views/gallery/ajax.html"
				}
			},

			"dependencies": [
				"dojox/mobile/Button"
			]
		}
	}	
}
