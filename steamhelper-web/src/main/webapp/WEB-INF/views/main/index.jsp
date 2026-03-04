<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>STEAM HELPER</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="STEAM HELPER">
    <meta name="description" content="STEAM助手 管理账号和游戏相关信息">

    <link rel="shortcut icon" href="${ctx}/static/img/steam_logo.png">
    <link rel="stylesheet" href="${ctx}/static/layui/css/layui.css">
    <link rel="stylesheet" href="${ctx}/static/css/global.css">
    <link rel="stylesheet" href="${ctx}/static/css/fontcss.css">
    <link rel="stylesheet" href="${ctx}/static/css/main.css">
    <link rel="stylesheet" href="${ctx}/static/css/backstage.css">

    <script type="text/javascript" src="${ctx}/static/layui/layui.js"></script>


<script type="text/javascript">
var tab;
layui.config({
	base : "${ctx}/static/js/"
}).use(['jquery', 'commCms','layer','element','bodyTab', 'upload'], function () {
	var $ = layui.jquery,
	layer = layui.layer,
	common = layui.commCms,
	upload = layui.upload,
	element = layui.element;  //导航的hover效果、二级菜单等功能，需要依赖element模块
	tab = layui.bodyTab();


	/*初始化左边操作栏*/
	layer.ready(function () {
		ajaxMenuInit();
	});

	function ajaxMenuInit() {
		$.ajax({
			url: '${ctx}/menu/left.do',
			type : 'post',
			async: false,
			data:{
			},
			success : function(data) {
				if(!data) {
					$("#navBarId").empty();
				}
				var ulHtml = '<ul class="layui-nav layui-nav-tree layui-left-nav">';
				$.each(data,function(index,item) {
					ulHtml += '<li class="layui-nav-item">';

					if(item.children != null ) {
						ulHtml += '<a href="javascript:;">';
						ulHtml += '<i class="layui-icon '+item.icon+' " data-icon="'+item.icon+'"></i>';
						ulHtml += '<cite>'+item.name+'</cite>';
						ulHtml += '<span class="layui-nav-more"></span>';
						ulHtml += '</a>';
						ulHtml += '<dl class="layui-nav-child">';
						$.each(item.children,function(index,child) {
							ulHtml += '<dd><a href="javascript:;" data-url="'+child.url+'">';
							if(child.icon != null) {
								ulHtml += '<i class="layui-icon '+child.icon+'" data-icon="'+child.icon+'"></i>';
							}
							ulHtml += '<cite>'+child.name+'</cite></a></dd>';
						});
						ulHtml += "</dl>"
					}else{
						ulHtml += '<a href="javascript:;" data-url="">';
						ulHtml += '<i class="layui-icon '+item.icon+'" data-icon="'+item.icon+'"></i>';
						ulHtml += '<cite>'+item.name+'</cite></a>';
					}
					ulHtml += '</li>'
				});
				ulHtml += '</ul>';
				$(".navBar").html(ulHtml);
				element.init();  //初始化页面元素
			}
		});
	}

	$('#lock').mouseover(function () {
		layer.tips('请按Alt+L快速锁屏！', '#lock', {tips: [1, '#3c8dbc'], time: 2000})
	});
	$(document).keydown(function (e) {
		if (e.altKey && e.which == 76) {
			lockPage();
		}
	});
	$(document).keyup(function(event) {
		if(event.keyCode ==13) {
			$("#unlock").click();
		}
	});

	//退出
	$('#logout').on('click', function () {
		var url = '${ctx}/logout';
		console.info("logout...");
		common.logOut("退出","退出",url);
	});


	// 修改头像
	$("#updateAvatar").on('click', function () {
		uploadInst.upload();
	});


	var uploadInst = upload.render({
		elem: '#updateAvatar',
		url: '${ctx}/user/updAvatar.do',
		before: function(obj) {
			// 预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				$('#avatarImg').attr('src', result);
			});
			element.progress('filter-demo', '0%');
			layer.msg('上传中', {icon: 16, time: 0});
		},
		done: function(res) {
			// 若上传失败
			if(res.code === '0000') {
				$('#avatarImg').attr('src', '${ctx}' + res.data);
			} else {
				return layer.msg('上传失败:'+res.msg);
			}
		},
		error: function() {
			return layer.msg('上传失败');
		},
		// 进度条
		progress: function(n, elem, e) {
			element.progress('filter-demo', n + '%'); // 可配合 layui 进度条元素使用
			if(n === 100) {
				layer.msg('上传完毕', {icon: 1});
			}
		}
	});

	//修改密码
	$("#upadtePassword").on('click', function () {
		var url = '${ctx}/user/updPwd';
		common.cmsLayOpen('修改密码',url,'450px','400px');
	});

	// 添加新窗口
	$("body").on("click",".layui-left-nav .layui-nav-item a",function() {
		if($(this).attr("data-url")) {
			//如果不存在子级
			if($(this).siblings().length == 0) {
				addTab($(this));
			}
		}
		$(this).parent("li").siblings().removeClass("layui-nav-itemed");
	});

	/**打开或隐藏选项卡*/
	$(".side-menu-switch").click(function () {
		$(".layui-layout-admin").toggleClass("showMenu");
		$(".layui-body,.layui-footer").css("left", ($(".layui-layout-admin").hasClass("showMenu")) ? "0" : "203px")
	});

	/**关闭当前*/
	$(".closeCurrent").on("click",function() {
		if($("#top_tabs li").length>1 && $("#top_tabs li.layui-this cite").text()!="后台首页") {
			var menu = JSON.parse(window.sessionStorage.getItem("menu"));
			$("#top_tabs li").each(function() {
				if($(this).attr("lay-id") != '' && $(this).hasClass("layui-this")) {
					element.tabDelete("bodyTab",$(this).attr("lay-id")).init();
					//此处将当前窗口重新获取放入session，避免一个个删除来回循环造成的不必要工作量
					for(var i=0;i<menu.length;i++) {
						if($("#top_tabs li.layui-this cite").text() == menu[i].title) {
							menu.splice(0,menu.length,menu[i]);
							window.sessionStorage.setItem("menu",JSON.stringify(menu));
						}
					}
				}
			});
		}else{
			top.layer.msg('首页不能关闭',{icon: 0});
		}
	});





});

//打开新窗口
function addTab(_this) {
	tab.tabAdd(_this);
}

</script>

</head>
<body class="main_body larryTheme-A">

<div class="layui-layout layui-layout-admin ">
    <!-- 顶部-->
    <div class="layui-header header header-menu ">
        <div class="layui-main ">
            <a href="#" class="logo" style="font-size:16px">SteamHelper</a>
            <!-- 左侧导航收缩开关 -->
            <div class="side-menu-switch">
                <ul class="layui-nav clearfix ">
                    <li style="" class="layui-nav-item">
                        <a class="onFullScreen"><i class="layui-icon layui-icon-shrink-right"></i></a>
                    </li>
                </ul>
            </div>
            <!-- 顶级菜单 -->
            <div class="larry-top-menu posb topMenu" id="topMenu"></div>
            <!-- 右侧常用菜单导航 -->
            <div class="larry-right-menu posb" >
                <ul class="layui-nav clearfix ">
                    <li class="layui-nav-item">
                        <a id="upadtePassword" style="height: 50px;"><i class="layui-icon layui-icon-password"></i><cite>修改密码</cite></a>
                    </li>
                    <li class="layui-nav-item exit">
                        <a id="logout" style="height: 50px;"><i class="layui-icon layui-icon-return"></i><cite>退出</cite></a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- 左侧导航-->
    <div class="layui-side layui-side-menu layui-bg-black" style="top:50px;">
        <div class="user-photo">
            <a class="img" title="我的头像" id="updateAvatar">
				<c:if test="${empty LOGIN_USER.avatarType}">
					<img id="avatarImg" src="${ctx}/static/img/stark.jpg">
				</c:if>
				<c:if test="${LOGIN_USER.avatarType == 1}">
					<img id="avatarImg" src="${ctx}/${LOGIN_USER.avatar}">
				</c:if>
				<c:if test="${LOGIN_USER.avatarType == 2}">
					<img id="avatarImg" src="${LOGIN_USER.avatar}">
				</c:if>
			</a>
            <p><i class="layui-icon layui-icon-username"></i> ${LOGIN_USER.userName}</p>
        </div>

        <!-- 左侧菜单-->
        <div class="navBar layui-side-scroll" id="navBarId">
        </div>
    </div>
    <!--中间内容 -->
    <div class="layui-body layui-form" id="larry-body">
        <div class="layui-tab marg0" id="larry-tab" lay-filter="bodyTab">
            <! -- 选项卡-->
            <ul class="layui-tab-title top_tab" id="top_tabs">
                <li class="layui-this" lay-id=""><i class="layui-icon layui-icon-home"></i></li>
            </ul>
            <div class="layui-tab-content clildFrame" style="height:793px;">
                <div class="layui-tab-item layui-show layui-anim layui-anim-upbit" >
                    <iframe src="${ctx}/home" data-id="0" name="ifr_0" id="ifr_0"></iframe>
                </div>
                <div style="height: 10px;"></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
