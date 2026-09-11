/**自定义模块*/
layui.define(['layer'], function (exports) {
    var $ = layui.jquery,
        layer = layui.layer;
    var CmsCommon = {

        /**错误msg提示 */
        cmsLayErrorMsg:function (text) {
            layer.msg(text, {time: 3000,offset: "t",icon: 5});
        },
        /**成功 msg提示 */
        cmsLaySucMsg:function (text) {
            layer.msg(text, {time: 3000,offset: "t",icon: 6});
        },

        /**ajax Confirm 对话框*/
        ajaxCmsConfirm: function (title, text, url,param,reloadWindow) {
            layer.confirm(text, {
                title: title,
                resize: false,
                offset: '50px',
                btn: ['确定', '取消'],
                btnAlign: 'c',
                anim:1,
                icon: 3
            }, function () {
                $.ajax({
                    url : url,
                    type : 'post',
                    async: false,
                    data : param,
                    success : function(data) {
                        if(data.code == "0000"){
                            layer.msg(data.msg,{time: 3000,offset: "t",icon: 6});
                            reloadWindow.reload();
                        }else{
                            layer.msg(data.msg,{time: 3000,offset: "t",icon: 5});
                        }
                    },error:function(data){

                    }
                });
            }, function () {

            })

        },

        /** ajax 带遮罩层*/
        ajaxMask:function(url,params){
        	var i;
            $.ajax({
                url : url,
                type : 'post',
                async: false,
                data : params,
                beforeSend: function () {
    	        	i = layer.load(1, {
                        shade: [0.1,'#fff'] //0.1透明度的白色背景
                    });
    	        },success : function(data) {
                    if(data.code == "0000"){
                    	layer.msg(
                    			data.msg,
                    			{time: 3000,offset: "t",icon: 6},
                    			function(){
                    		 		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    		 		parent.layer.close(index); //再执行关闭
                    			});
                    }else{
                    	layer.msg(data.msg,
                    			{time: 3000,offset: "t",icon: 5});
                    }
                },error:function(data){
                	layer.msg("请求异常",{time: 3000,offset: "t",icon: 5});
                },complete:function(){
                    layer.close(i);
                }
            });
        },

        /**弹出层*/
        cmsLayOpen:function (title,url,width,height) {
            layui.layer.open({
                title : '<i class="layui-icon layui-icon-layer"></i>'+title,
                type : 2,
                skin : 'layui-layer-molv',
                offset: '50px',
                content : url,
                area: [width, height],
                resize:true,
                anim:1,
                success : function(layero, index){

                }
            });
        },

        /**弹出层(可选择皮肤)*/
        cmsLaySkinOpen:function (title,url,width,height,skin) {
            var realSkin;
            if(skin == undefined || skin === '' || skin == null){
                realSkin =  'layui-layer-molv';
            }else{
                realSkin =  skin;
            }

            layui.layer.open({
                title : title,
                type : 2,
                skin : realSkin,
                content : url,
                area: [width, height],
            });
        },

        /**弹出层-tip*/
        cmsLayOpenTip:function (title,url,width,height) {
           layui.layer.open({
                title : '<i class="layui-icon layui-icon-layer"></i>'+title,
                type : 2,
                skin : 'layui-layer-molv',
                content : url,
                offset: '50px',
                area: [width, height],
                resize:false,
                anim:1,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回', '.layui-layer-setwin .layui-layer-close', {
                            tips: [3, '#009f95']
                        });
                    },500)

                }
            });
        },

        /** 弹出即全屏**/
        cmsLayFull:function (url,width,height) {
            var index =layui.layer.open({
                type: 2,
                content: url,
                area: [width, height],
                maxmin: true
            });
            layer.full(index);
        },

        /**退出*/
        logOut: function (title, text, url, type, dataType, data, callback) {
            parent.layer.confirm(text, {
                title: title,
                resize: false,
                btn: ['确定退出系统', '不，我点错了！'],
                btnAlign: 'c',
                icon: 3
            }, function () {
                console.info("logout url="+url);
                window.location.href = url
            }, function () {

            })
        },



        /**先留着研究下怎么用*/
        showImg: function (url) {
        	var index = layui.layer.open({
                title : '<i class="layui-icon-layer layui-icon"></i>'+111111,
                type : 2,
                skin : 'layui-layer-molv',
                content : url,
                offset: '50px',
                resize:false,
                anim:1,
                success : function(layero, index){
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回', '.layui-layer-setwin .layui-layer-close', {
                            tips: [3, '#009f95']
                        });
                    },500)

                }
            });
        },

        /** 时间格式化*/
        formatTime:function(date,fmt){
            if(date == null){
                return '';
            }
            var date = new Date(date || new Date());
            var o = {
                "M+" : date.getMonth()+1,                 //月份
                "d+" : date.getDate(),                    //日
                "h+" : date.getHours(),                   //小时
                "m+" : date.getMinutes(),                 //分
                "s+" : date.getSeconds(),                 //秒
                "q+" : Math.floor((date.getMonth()+3)/3), //季度
                "S"  : date.getMilliseconds()             //毫秒
            };
            if(/(y+)/.test(fmt))
                fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
            for(var k in o)
                if(new RegExp("("+ k +")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            return fmt;
        }

    };
    exports('commCms', CmsCommon)
})



