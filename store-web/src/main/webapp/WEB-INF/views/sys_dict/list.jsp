<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>5r7 store</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="5r7 store">
    <meta name="description" content="五人齐商铺 一个买卖东西的地方">
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
                    <form class="layui-form" id="dictSearchForm">
                        <div class="layui-form-item" style="margin-bottom:3px;">
                            <label class="layui-form-label">字典-组:</label>
                            <div class="layui-input-inline" style="width:140px;">
                                <input type="text" name="dictGroup" value="" placeholder="请输入" class="layui-input search_input">
                            </div>
                            <label class="layui-form-label">字典-key:</label>
                            <div class="layui-input-inline" style="width:140px;">
                                <input type="text" name="dictKey" value="" placeholder="请输入" class="layui-input search_input">
                            </div>
							<shiro:hasPermission name="sys:dict:list">
                            <a class="layui-btn dictSearchList_btn" lay-submit lay-filter="searchFilter"><i class="layui-icon  layui-icon-search"></i>查询</a>
							</shiro:hasPermission>
                        </div>
                    </form>
                </div>
            </blockquote>
            <div class="larry-separate"></div>
            <!-- 字典列表 -->
            <div class="layui-tab-item  layui-show" style="padding: 10px 15px;">
                <div class="layui-inline" style="margin-bottom: 10px;">
                    <shiro:hasPermission name="sys:dict:add">
                        <a class="layui-btn layui-btn-normal  dictAdd_btn"> <i class="layui-icon  layui-icon-add-circle"></i>新增字典</a>
                    </shiro:hasPermission>
                </div>
                <table id="dictTableList" lay-filter="resTableId"></table>
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
			elem: '#dictTableList',
			url: '${ctx}/dict/list.do',
			id:'resTableId',
			method: 'post',
			loading:true,
			skin:'row',
			even:'true',
			size: 'sm',
			where: field,
			cols: [[
				{field:'id', title: '序号', width: '15%', align:'center'},
				{field:'dictGroup', title: '组', width: '20%', align:'center' },
				{field:'dictKey', title: 'key', width: '20%', align:'center' },
				{field:'dictValue', title: 'value', width: '26%', align:'center'},
				{field:'dictSort',title: '排序', width: '20%', align:'center'},
				{fixed:'right',title: '操作', width: '20%', align:'center',toolbar: '#dictBar'}
			]],
			page: true,
			limit: 20
		});
	}

	<shiro:hasPermission name="sys:dict:list">
	searchList();
	</shiro:hasPermission>

	$(".dictSearchList_btn").click(function(){
		form.on('submit(searchFilter)', function (data) {
			searchList(data);
		});
	});

	/**字典新增*/
	$(".dictAdd_btn").click(function(){
		var url = "${ctx}/dict/add";
		common.cmsLayOpen('新增字典',url,'880px','410px');
	});

	/**监听工具条*/
	table.on('tool(resTableId)', function(obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var url;
		switch (layEvent) {
			case 'res_edit':
				url =  '${ctx}/dict/upd?id=' + data.id;
				common.cmsLayOpen('编辑字典',url,'880px','410px');
				break;
			case 'res_del':
				url = "${ctx}/dict/del.do";
				var param = {id: data.id};
				common.ajaxCmsConfirm('系统提示', '确定删除该字典?',url,param);
				break;
			default:
				break;
		}
	});
});
</script>

<!--工具条 -->
<script type="text/html" id="dictBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="sys:dict:upd">
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_edit"><i class="layui-icon  layui-icon-edit"></i>编辑</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="sys:dict:del">
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
		</shiro:hasPermission>
	</div>
</script>


</body>
</html>