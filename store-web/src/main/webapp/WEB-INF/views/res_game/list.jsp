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
                    <form class="layui-form" id="searchForm">
                        <div class="layui-form-item" style="margin-bottom:3px;">
							<label class="layui-form-label">所属平台</label>
							<div class="layui-input-inline">
								<select name="platformId" id="platformId">
									<c:forEach items="${platforms}" var="item">
										<option value="${item.dictValue}">${item.dictKey} <c:if test="item.index==0">checked</c:if></option>
									</c:forEach>
								</select>
							</div>
							<label class="layui-form-label">英文名称</label>
							<div class="layui-input-inline" style="width:190px;">
								<input type="text" name="engName" value="" placeholder="请输入" class="layui-input search_input">
							</div>
							<label class="layui-form-label">中文名称</label>
							<div class="layui-input-inline" style="width:190px;">
								<input type="text" name="engName" value="" placeholder="请输入" class="layui-input search_input">
							</div>
						</div>
						<div class="layui-form-item" style="margin-bottom:3px;">
							<label class="layui-form-label">系列</label>
							<div class="layui-input-inline">
								<select name="series" id="series" lay-filter="seriesFilter">
									<option value="">请选择</option>
								</select>
							</div>
							<label class="layui-form-label">子系列</label>
							<div class="layui-input-inline">
								<select name="subSeries" id="subSeries">
									<option value="">请选择</option>
								</select>
							</div>
							<label class="layui-form-label">成就状态</label>
							<div class="layui-input-inline">
								<select name="achieveStatus" id="achieveStatus">
									<option value="">请选择</option>
									<c:forEach items="${achv}" var="item">
										<option value="${item.dictValue}">${item.dictKey}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<shiro:hasPermission name="res:game:list">
						<div class="layui-form-item" style="margin-bottom:3px;">
                            <a class="layui-btn layui-btn-normal search_btn" lay-submit lay-filter="searchFilter"><i class="layui-icon  layui-icon-search"></i>查询</a>
                        </div>
						</shiro:hasPermission>
                    </form>
                </div>
            </blockquote>
            <div class="larry-separate"></div>
            <div class="layui-tab-item  layui-show" style="padding: 10px 15px;">
                <div class="layui-inline" style="margin-bottom: 10px;">
                    <shiro:hasPermission name="res:game:auto">
                        <a class="layui-btn layui-btn-normal  auto_btn"> <i class="layui-icon  layui-icon-add-circle"></i>自动更新</a>
                    </shiro:hasPermission>
                </div>
                <div class="layui-inline" style="margin-bottom: 10px;">
                    <shiro:hasPermission name="res:game:import">
                        <a class="layui-btn layui-btn-normal  import_btn"> <i class="layui-icon  layui-icon-add-circle"></i>导入游戏</a>
                    </shiro:hasPermission>
                </div>
                <div class="layui-inline" style="margin-bottom: 10px;">
                    <shiro:hasPermission name="res:game:add">
                        <a class="layui-btn layui-btn-normal  add_btn"> <i class="layui-icon  layui-icon-add-circle"></i>新增游戏</a>
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
	var seriesMap = JSON.parse('${seriesMap}');
	var achvList = JSON.parse('${achv}');
	var iconPrefix = '${iconPrefix}';

	function initSel() {
		Object.keys(seriesMap).forEach(key => {
			$("#series").append(
				'<option value="'+key+'">'+key+'</option>'
			);
		});
		form.render($('#series'));
	}

	<shiro:hasPermission name="res:clf:list">
	initSel();
	</shiro:hasPermission>

	function searchList(data) {
		var field = data && data.field
		table.render({
			elem: '#accTableList',
			url: '${ctx}/game/list.do',
			id:'resTableId',
			method: 'post',
			loading:true,
			skin:'row',
			even:'true',
			size: 'sm',
			where: field,
			cols: [[
				{field:'id', title: '序号', width: '6%', align:'center'},
				{field:'iconSuffix', title: 'icon', width: '6%', align:'center',templet: function (d) {
						return '<img src="' + iconPrefix + d.appId + d.iconSuffix + '" alt="' + d.engName + '">';
					}},
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
				{field:'appId', title: 'APPID', width: '9%', align:'center'},
				{field:'engName', title: '英文名称', width: '9%', align:'center'},
				{field:'chnName', title: '中文名称', width: '9%', align:'center'},
				{field:'series', title: '系列', width: '9%', align:'center'},
				{field:'subSeries', title: '子系列', width: '9%', align:'center',templet: function (d) {
						var name = '';
						seriesMap[d.series].forEach(item => {
							if (item.id == d.clf_id) {
								name = item.subSeries;
								return ;
							}
						})
						return name;
					}},
				{field:'achieveStatus', title: '成就类型', width: '9%', align:'center',templet: function (d) {
						var name = '';
						achvList.forEach(item => {
							if (item.dictValue == d.achieveStatus) {
								name = item.dictKey;
								return ;
							}
						})
						return name;
					}},
				{field:'dlcNum', title: '拥有dlc数量', width: '9%', align:'center'},
				{field:'achieveNum', title: '拥有成就数量', width: '9%', align:'center'},
				{field:'releaseDate', title: '发行日期', width: '6%', align:'center'},
				{fixed:'right',title: '操作', width: '15%', align:'center',toolbar: '#resBar'}
			]],
			page: true,
			limit: 20
		});
	}
	<shiro:hasPermission name="res:game:list">
		searchList();
	</shiro:hasPermission>

	form.on('select(seriesFilter)', function(data) {
		$('#subSeries option').not(":first").remove();
		var subList = seriesMap[data.value];
		subList.forEach( item => {
			$("#subSeries").append(
				'<option value="'+item.id+'">'+item.subSeries+'</option>'
			);
		});
		form.render($("#subSeries"));
	});

	$(".search_btn").click(function(){
		form.on('submit(searchFilter)', function (data) {
			searchList(data);
		});
	});

	/**自动全量更新*/
	$(".auto_btn").click(function(){
		var url = "${ctx}/game/auto.do";
		var platformId = $("#platformId").val()
		var param = {platformId: platformId};
		common.ajaxMask(url, param);
	});

	/**导入*/
	$(".import_btn").click(function(){
		var platformId = $("#platformId").val()
		var url = "${ctx}/game/import?platformId=" + platformId;
		common.cmsLayOpen('导入游戏',url,'880px','410px');
	});

	/**新增*/
	$(".add_btn").click(function(){
		var url = "${ctx}/game/add";
		common.cmsLayOpen('新增账号',url,'880px','410px');
	});

	/**监听工具条*/
	table.on('tool(resTableId)', function(obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var url;
		switch (layEvent) {
			case 'res_edit':
				url =  '${ctx}/game/upd?id=' + data.id;
				common.cmsLayOpen('编辑游戏',url,'880px','410px');
				break;
			case 'res_del':
				url = "${ctx}/game/del.do";
				var param = {id: data.id};
				common.ajaxCmsConfirm('系统提示', '确定删除该游戏?',url,param);
				break;
			case 'res_force':
				var platformId = $("#platformId").val()
				url = "${ctx}/game/res_force.do";
				var param = {platformId: platformId};
				common.ajaxCmsConfirm('系统提示', '确定强制更新该游戏的信息?',url,param);
				break;
			default:
				break;
		}
	});
});
</script>

<!--工具条 -->
<script type="text/html" id="resBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="res:game:upd">
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_edit"><i class="layui-icon  layui-icon-edit"></i>编辑</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="res:game:force">
			<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="res_force"><i class="layui-icon  layui-icon-delete"></i>强制更新</a>
		</shiro:hasPermission>
	</div>
</script>


</body>
</html>