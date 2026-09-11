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
<div class="larry-grid layui-anim layui-anim-upbit larryTheme-A" >
    <div class="larry-personal" >
        <div class="layui-tab" >
            <blockquote class="layui-elem-quote mylog-info-tit">
                    <div class="layui-inline">
                        <form class="layui-form" id="cusSearchForm">
                            <div class="layui-form-item" style="margin-bottom:3px;">
                                <label class="layui-form-label">菜单名称:</label>
                                <div class="layui-input-inline" style="width:140px;">
                                    <input type="text" name="name" value="" placeholder="请输入关键字" class="layui-input search_input">
                                </div>
                                <label class="layui-form-label">菜单类型:</label>
                                <div class="layui-input-inline" style="width:140px;">
                                	<select name="menuType" >
                                		<option selected="selected"></option>
                                		<option value="1">菜单</option>
                                		<option value="2">按钮</option>
                                	</select>
                                </div>
                                <label class="layui-form-label" >菜单级别:</label>
                                <div class="layui-input-inline" style="width:140px;">
                                    <select name="level" >
                                		<option selected="selected"></option>
                                		<option value="1">1级菜单</option>
                                		<option value="2">2级菜单</option>
                                		<option value="2">3级菜单</option>
                                	</select>
                                </div>
								<shiro:hasPermission name="sys:menu:list">
                                <a class="layui-btn layui-btn-normal searchList_btn" lay-submit lay-filter="searchFilter"><i class="layui-icon  layui-icon-search"></i>查询</a>
								</shiro:hasPermission>
                            </div>
                        </form>
                    </div>
            </blockquote>
            <div class="larry-separate"></div>
            <!-- 菜单列表 -->
            <div class="layui-tab-item layui-show" style="padding: 10px 15px;">
                <shiro:hasPermission name="sys:menu:add">
                    <div class="layui-inline" style="margin-bottom: 10px;">
                        <a class="layui-btn layui-btn-normal  resAdd_btn"> <i class="layui-icon  layui-icon-add-circle"></i>新增菜单</a>
                    </div>
                </shiro:hasPermission>
                <table id="resTableList" lay-filter="resTableId"></table>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    layui.config({
        base : "${ctx}/static/js/"
    }).use(['form', 'table', 'layer','commCms'], function () {
        var $ = layui.$,
                form = layui.form,
                table = layui.table,
                layer = layui.layer,
                common = layui.commCms;

		function searchList(data) {
			var field = data && data.field
			table.render({
				elem: '#resTableList',
				url: '${ctx}/menu/list.do',
				id:'resTableId',
				method: 'post',
				loading:true,
				skin:'row',
				even:'true',
				size: 'sm',
				where: field,
				cols: [[
					{field:'id', title: '序号', width: '6%', align:'center'},
					{field:'name', title: '菜单名称', width: '10%', align:'center'},
					{field:'level', title: '菜单级别', width: '8%', align:'center',templet: '#resLevelTpl'},
					{field:'menuType', title: '菜单类型', width: '8%', align:'center',templet: '#menuTypeTpl'},
					{field:'pId', title: '上级菜单', width: '6%', align:'center'},
					{field:'url', title: '菜单路径', width: '16%', align:'center'},
					{field:'permission', title: '许可标识', width: '10%', align:'center'},
					{field:'displayOrder', title: '显示顺序', width: '7%', align:'center'},
					{field:'remark', title: '备注', width: '8%', align:'center'},
					{field:'createTime', title: '创建时间', width: '10%', align:'center', templet: '#timeTpl'},
					{fixed:'right', title: '操作', width: '10%', align:'center',toolbar: '#resBar'}
				]],
				page: true,
				limit: 20
			});
		}

		<shiro:hasPermission name="sys:menu:list">
		searchList();
		</shiro:hasPermission>

		/**查询*/
		$(".searchList_btn").click(function(){
			form.on('submit(searchFilter)', function (data) {
				searchList(data);
			});
		});

		/**新增菜单*/
		$(".resAdd_btn").click(function(){
			var url = "${ctx}/menu/add";
			common.cmsLayOpen('新增菜单',url,'880px','600px');
		});

		/**监听工具条*/
		table.on('tool(resTableId)', function(obj) {
			var data = obj.data;
			var layEvent = obj.event;
			var url;
			switch (layEvent) {
				case 'res_edit':
					url =  '${ctx}/menu/upd?id=' + data.id;
					common.cmsLayOpen('编辑菜单',url,'880px','600px');
					break;
				case 'res_del':
					url = "${ctx}/menu/del.do";
					var param = {id: data.id};
					common.ajaxCmsConfirm('系统提示', '确定删除该菜单?',url,param);
					break;
				default:
					break;
			}
		});
	});

</script>
<!-- 菜单类型tpl-->
<script type="text/html" id="menuTypeTpl">
    {{# if(d.menuType == 1){ }}
    <span class="label label-info ">菜单</span>
    {{# } else if(d.menuType == 2){ }}
    <span class="label label-warning ">按钮</span>
    {{# } }}
</script>

<!-- 菜单级别tpl-->
<script type="text/html" id="resLevelTpl">
    {{# if(d.level == 1){ }}
    <span>1级菜单</span>
    {{# } else if(d.level == 2){ }}
    <span>2级菜单</span>
    {{# } else if(d.level == 3){ }}
    <span>3级菜单</span>
    {{# } }}
</script>

<!-- 时间tpl-->
<script type="text/html" id="timeTpl">
	{{layui.util.toDateString(d.createTime, 'yyyy-MM-dd HH:mm:ss')}}
</script>

<!--工具条 -->
<script type="text/html" id="resBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="sys:menu:upd">
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_edit"><i class="layui-icon  layui-icon-edit"></i>编辑</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="sys:menu:del">
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
		</shiro:hasPermission>
	</div>
</script>

</body>
</html>