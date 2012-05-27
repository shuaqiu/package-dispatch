define([
        "dojo/dom-construct",
        "dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/xhr",
        "dijit/form/Button",
        "dojox/form/uploader/FileList",
        "./DestroyWhenCloseDialog",
        "./MessageDialog",
        "dojo/i18n!./nls/UploaderDialog",
        "dojox/form/Uploader",
        "dojox/form/uploader/plugins/HTML5",
        "dojox/form/uploader/plugins/IFrame" ], function(domconstruct, declare, lang, xhr, Button, FileList,
        DestroyWhenCloseDialog, MessageDialog, message, template) {

    return declare("qiuq.widget.UploaderDialog", [ DestroyWhenCloseDialog ], {

        width : "600px",
        height : "300px",

        title : message["title"],

        _uploader : null,
        uploaderLabel : message["uploader"],
        uploaderUrl : null,
        uploaderProp : null,

        acceptType : [],

        submitLabel : message["submit"],

        _filelist : null,
        isShowFileList : true,
        filelistLabels : {
            headerType : message["headerType"],
            headerFilename : message["headerFilename"],
            headerFilesize : message["headerFilesize"],
        },
        filelistProp : null,

        postCreate : function() {
            this.inherited(arguments);
            var self = this;

            var form = domconstruct.create("form", {
                method : "post",
                enctype : "multipart/form-data"
            });

            var uploaderProp = lang.mixin({
                label : this.uploaderLabel,
                url : this.uploaderUrl,
                showInput : "before"
            }, this.uploaderProp);
            this._uploader = new dojox.form.Uploader(uploaderProp);
            this._uploader.placeAt(form);
            if (!this._uploader.supports("multiple")) {
                this._uploader.set("name", "uploadedfiles[]");

                domconstruct.create("input", {
                    type : "hidden",
                    name : "plugin",
                    value : "iframe"
                }, form);
            }

            var submitButton = new Button({
                label : this.submitLabel,
                type : "submit",
                onClick : lang.hitch(this, this.isSelectedFile)
            }).placeAt(form);

            if (this.isShowFileList) {
                var filelistProp = lang.mixin({
                    uploader : this._uploader
                }, this.filelistLabels, this.filelistProp);

                this._filelist = new FileList(filelistProp).placeAt(form);
            }

            this.set("content", form);

            // call the connectForm manually
            this._uploader.connectForm();
            this.connect(this._uploader, "onComplete", this.onComplete);
            this.connect(this._uploader, "onChange", this.onChange);
        },

        isSelectedFile : function() {
            if (this._uploader.getFileList().length == 0) {
                MessageDialog.error(message["err.NOT_FILE_SELECTED"]);
                return false;
            }
            return true;
        },

        onComplete : function(result) {
            if (result.ok) {
                if (result.obj == null) {
                    MessageDialog.alert(message["ok"]);
                } else {
                    var content = domconstruct.create("div", {
                        innerHTML : message["ok.WITH_ERROR"]
                    });
                    new Button({
                        label : message["downloadFail"],
                        onClick : function() {
                            var downloadFrame = window.frames["downloadFrame"];
                            if (downloadFrame == null) {
                                downloadFrame = domconstruct.create("iframe", {
                                    name : "downloadFrame",
                                    style : {
                                        display : "none"
                                    }
                                }, document.body);
                            }
                            downloadFrame.src = "web/download?f=" + result.obj;
                        }
                    }).placeAt(content);

                    MessageDialog.alert(content, function() {
                        xhr.del({
                            url : "web/download",
                            content : {
                                f : result.obj
                            },
                        });
                    });
                }
            } else {
                MessageDialog.error(result.message);
            }
        },

        onChange : function(fileArr) {
            if (this._isAccept(fileArr)) {
                return true;
            }
            MessageDialog.error(message["err.NEED_FOLLOWING_TYPE"] + this.acceptType.join(", "));
            this._uploader.reset();
            return false;
        },

        _isAccept : function(fileArr) {
            var acceptTypeMap = this._getAcceptTypeMap();

            for ( var i = 0; i < fileArr.length; i++) {
                var filetype = fileArr[i].type || this._uploader.getFileType(fileArr[i].name);
                if (acceptTypeMap != null && acceptTypeMap[filetype.toUpperCase()] == null) {
                    // wanted some special type, but current file is not in these type.
                    return false;
                }
            }
            return true;
        },

        _getAcceptTypeMap : function() {
            if (this.acceptType == null || this.acceptType.length == 0) {
                return null;
            }

            var acceptTypeMap = {};
            for ( var i = 0; i < this.acceptType.length; i++) {
                var aType = this.acceptType[i];
                acceptTypeMap[aType.toUpperCase()] = aType;
            }
            return acceptTypeMap;
        }
    });
});