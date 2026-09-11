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
                    <form class="layui-form" id="accSearchForm">
                        <div class="layui-form-item" style="margin-bottom:3px;">
                            <label class="layui-form-label">用户昵称</label>
                            <div class="layui-input-inline" style="width:140px;">
                                <input type="text" name="userName" value="" placeholder="请输入" class="layui-input search_input">
                            </div>
							<label class="layui-form-label">所属平台</label>
							<div class="layui-input-inline">
								<select name="platformId" id="platformId">
									<option value="">请选择</option>
									<c:forEach items="${platforms}" var="item">
										<option value="${item.dictValue}">${item.dictKey}</option>
									</c:forEach>
								</select>
							</div>
							<shiro:hasPermission name="res:acc:list">
                            <a class="layui-btn layui-btn-normal accSearchList_btn" lay-submit lay-filter="searchFilter"><i class="layui-icon  layui-icon-search"></i>查询</a>
							</shiro:hasPermission>
                        </div>
                    </form>
                </div>
            </blockquote>
            <div class="larry-separate"></div>
            <!-- 账号列表 -->
            <div class="layui-tab-item  layui-show" style="padding: 10px 15px;">
                <div class="layui-inline" style="margin-bottom: 10px;">
                    <shiro:hasPermission name="res:acc:add">
                        <a class="layui-btn layui-btn-normal  accAdd_btn"> <i class="layui-icon  layui-icon-add-circle"></i>新增账号</a>
                    </shiro:hasPermission>
                </div>
                <table id="accTableList" lay-filter="resTableId"></table>
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
	var platforms = JSON.parse('${platforms}');

	function searchList(data) {
		var field = data && data.field
		table.render({
			elem: '#accTableList',
			url: '${ctx}/acc/list.do',
			id:'resTableId',
			method: 'post',
			loading:true,
			skin:'row',
			even:'true',
			size: 'sm',
			where: field,
			cols: [[
				{field:'id', title: '序号', width: '6%', align:'center'},
				{field:'userId', title: '用户id', width: '9%', align:'center' },
				{field:'platformId', title: '所属平台', width: '9%', align:'center',templet: function (d) {
						var name = '';
						platforms.forEach(item => {
							if (item.dictValue == d.platformId) {
								name = item.dictKey;
								return ;
							}
						})
						return name;
					}},
				{field:'loginName', title: '登录账号', width: '9%', align:'center'},
				{field:'userName', title: '用户昵称', width: '9%', align:'center'},
				{field:'platformUserId', title: '平台账号id', width: '9%', align:'center'},
				{field:'platformUserNo', title: '平台账号No', width: '9%', align:'center'},
				{field:'numType', title: '使用id还是no', width: '6%', align:'center', templet: '#numTypeTpl'},
				{field:'accountToken', title: '账号token', width: '9%', align:'center'},
				{field:'mainAccount', title: '主账号', width: '6%', align:'center'},
				{field:'accountSort',title: '排序', width: '6%', align:'center'},
				{fixed:'right',title: '操作', width: '15%', align:'center',toolbar: '#accBar'}
			]],
			page: true,
			limit: 20
		});
	}
	<shiro:hasPermission name="sys:menu:list">
	searchList();
	</shiro:hasPermission>
	$(".accSearchList_btn").click(function(){
		form.on('submit(searchFilter)', function (data) {
			searchList(data);
		});
	});

	/**账号新增*/
	$(".accAdd_btn").click(function(){
		var url = "${ctx}/acc/add";
		common.cmsLayOpen('新增账号',url,'880px','410px');
	});

	/**监听工具条*/
	table.on('tool(resTableId)', function(obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var url;
		switch (layEvent) {
			case 'res_edit':
				url =  '${ctx}/acc/upd?id=' + data.id;
				common.cmsLayOpen('编辑账号',url,'880px','410px');
				break;
			case 'res_del':
				url = "${ctx}/acc/del.do";
				var param = {id: data.id};
				common.ajaxCmsConfirm('系统提示', '确定删除该账号?',url,param);
				break;
			default:
				break;
		}
	});
});
</script>

<script type="text/html" id="numTypeTpl">
	{{# if(d.numType == 1){ }}
	<span>id</span>
	{{# } else if(d.numType == 2){ }}
	<span>no</span>
	{{# } }}
</script>

<!--工具条 -->
<script type="text/html" id="accBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="res:acc:upd">
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_edit"><i class="layui-icon  layui-icon-edit"></i>编辑</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="res:acc:del">
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
		</shiro:hasPermission>
	</div>
</script>


</body>
</html>