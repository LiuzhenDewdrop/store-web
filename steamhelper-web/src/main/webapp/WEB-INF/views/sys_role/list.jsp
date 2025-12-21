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
            <blockquote class="layui-elem-quote mylog-info-tit">
                <div class="layui-inline">
                    <form class="layui-form" id="roleSearchForm">
                        <div class="layui-form-item" style="margin-bottom:3px;">
                            <label class="layui-form-label">角色名称:</label>
                            <div class="layui-input-inline" style="width:140px;">
                                <input type="text" name="name" value="" placeholder="请输入角色名称" class="layui-input search_input">
                            </div>
                            <label class="layui-form-label">角色编码:</label>
                            <div class="layui-input-inline" style="width:140px;">
                                <input type="text" name="roleCode" value="" placeholder="请输入角色编码" class="layui-input search_input">
                            </div>
                            <a class="layui-btn roleSearchList_btn" lay-submit lay-filter="roleSearchFilter"><i class="layui-icon  layui-icon-search"></i>查询</a>
                        </div>
                    </form>
                </div>
            </blockquote>
            <div class="larry-separate"></div>
            <!-- 角色列表 -->
            <div class="layui-tab-item  layui-show" style="padding: 10px 15px;">
                <div class="layui-inline" style="margin-bottom: 10px;">
                    <shiro:hasPermission name="sys:role:add">
                        <a class="layui-btn layui-btn-normal  roleAdd_btn"> <i class="layui-icon  layui-icon-add-circle"></i>新增角色</a>
                    </shiro:hasPermission>
                </div>
                <table id="roleTableList" lay-filter="resTableId"></table>
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
			elem: '#roleTableList',
			url: '${ctx}/role/list.do',
			id:'resTableId',
			method: 'post',
			loading:true,
			skin:'row',
			even:'true',
			size: 'sm',
			where: field,
			cols: [[
				{field:'level', title: '等级', width: '15%', align:'center'},
				{field:'roleCode', title: '角色编码', width: '20%', align:'center' },
				{field:'name', title: '角色名称', width: '20%', align:'center' },
				{field:'remark', title: '备注', width: '26%', align:'center'},
				{fixed:'right',title: '操作', width: '20%', align:'center',toolbar: '#roleBar'}
			]],
			page: true,
			limit: 20
		});
	}

	searchList();

	$(".roleSearchList_btn").click(function(){
		form.on('submit(roleSearchFilter)', function (data) {
			searchList(data);
		});
	});

	/**角色新增*/
	$(".roleAdd_btn").click(function(){
		var url = "${ctx}/role/add";
		common.cmsLayOpen('新增角色',url,'880px','410px');
	});

	/**监听工具条*/
	table.on('tool(resTableId)', function(obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var url;
		switch (layEvent) {
			case 'res_edit':
				url =  '${ctx}/role/upd?id=' + data.id;
				common.cmsLayOpen('编辑角色',url,'880px','410px');
				break;
			case 'res_del':
				url = "${ctx}/role/del.do";
				var param = {id: data.id};
				common.ajaxCmsConfirm('系统提示', '确定删除该角色?',url,param);
				break;
			case 'res_grant':
				url =  '${ctx}/role/grant?id=' + data.id;
				common.cmsLayOpen('角色授权',url,'880px','520px');
				break;
			default:
				break;
		}
	});
});
</script>

<!--工具条 -->
<script type="text/html" id="roleBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="sys:role:upd">
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_edit"><i class="layui-icon  layui-icon-edit"></i>编辑</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="sys:role:del">
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="sys:role:grant">
			<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="res_grant"><i class="layui-icon layui-icon-auz"></i>权限</a>
		</shiro:hasPermission>
	</div>
</script>


</body>
</html>