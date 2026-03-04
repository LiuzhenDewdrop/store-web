<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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

    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" media="all">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/personal.css" media="all">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">
    <script src="${ctx}/static/layui/layui.js"></script>


<body>
<div class="larry-grid layui-anim layui-anim-upbit larryTheme-A">
    <div class="larry-personal">
        <div class="layui-tab">
            <div class="larry-separate"></div>
            <!-- 游戏分类列表 -->
            <div class="layui-tab-item  layui-show" style="padding: 10px 15px;margin-bottom: 80px;">
                <div class="layui-inline" style="margin-bottom: 10px;">
                    <shiro:hasPermission name="res:clf:add">
                        <a class="layui-btn layui-btn-normal  add_btn"> <i class="layui-icon  layui-icon-add-circle"></i>新增游戏分类</a>
                    </shiro:hasPermission>
                </div>
                <table id="clfTableList" lay-filter="resTableId"></table>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript">
layui.config({
	base : "${ctx}/static/js/"
}).use(['form', 'table', 'layer','commCms'], function () {
	var $ =  layui.$,
			form = layui.form,
			table = layui.table,
			layer = layui.layer,
			common = layui.commCms;

	function searchList(data) {
		var field = data && data.field
		table.render({
			elem: '#clfTableList',
			url: '${ctx}/clf/list.do',
			id:'resTableId',
			method: 'post',
			loading:true,
			skin:'row',
			even:'true',
			size: 'sm',
			where: field,
			cols: [[
				// {field:'id', title: '序号', width: '5%', align:'center'},
				{field:'series', title: '系列', width: '10%', align:'center' },
				{field:'subSeries', title: '子系列', width: '10%', align:'center' },
				{field:'resume', title: '简介', width: '25%', align:'center'},
				{field:'standard',title: '分类标准', width: '25%', align:'center'},
				{field:'sample',title: '经典样例', width: '20%', align:'center'},
				{field:'sort',title: '排序', width: '5%', align:'center'},
				{fixed:'right',title: '操作', width: '10%', align:'center',toolbar: '#clfBar'}
			]],
			// page: true,
			// limit: 20
		});
	}

	<shiro:hasPermission name="res:clf:list">
	searchList();
	</shiro:hasPermission>

	// $(".clfSearchList_btn").click(function(){
	// 	form.on('submit(searchFilter)', function (data) {
	// 		searchList(data);
	// 	});
	// });

	/**游戏分类新增*/
	$(".add_btn").click(function(){
		var url = "${ctx}/clf/add";
		common.cmsLayOpen('新增游戏分类',url,'500px','600px');
	});

	/**监听工具条*/
	table.on('tool(resTableId)', function(obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var url;
		switch (layEvent) {
			case 'res_edit':
				url =  '${ctx}/clf/upd?id=' + data.id;
				common.cmsLayOpen('编辑游戏分类',url,'500px','600px');
				break;
			case 'res_del':
				url = "${ctx}/clf/del.do";
				var param = {id: data.id};
				common.ajaxCmsConfirm('系统提示', '确定删除该游戏分类?',url,param);
				break;
			default:
				break;
		}
	});
});
</script>

<!--工具条 -->
<script type="text/html" id="clfBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="res:clf:upd">
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_edit"><i class="layui-icon  layui-icon-edit"></i>编辑</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="res:clf:del">
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
		</shiro:hasPermission>
	</div>
</script>


</body>
</html>