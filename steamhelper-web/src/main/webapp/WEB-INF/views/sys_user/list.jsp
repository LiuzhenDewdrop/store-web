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
<div class="larry-grid layui-anim layui-anim-upbit larryTheme-A" >
    <div class="larry-personal" >
        <div class="layui-tab" >
            <blockquote class="layui-elem-quote mylog-info-tit">
                    <div class="layui-inline">
                        <form class="layui-form" id="cusSearchForm">
                            <div class="layui-form-item" style="margin-bottom:3px;">
                                <label class="layui-form-label">登录名:</label>
                                <div class="layui-input-inline" style="width:140px;">
                                    <input type="text" name="loginName" value="" placeholder="请输入关键字" class="layui-input search_input">
                                </div>
                                <label class="layui-form-label">用户名:</label>
								<div class="layui-input-inline" style="width:140px;">
									<input type="text" name="userName" value="" placeholder="请输入关键字" class="layui-input search_input">
								</div>
                                <label class="layui-form-label">手机号:</label>
								<div class="layui-input-inline" style="width:140px;">
									<input type="text" name="phoneNo" value="" placeholder="请输入关键字" class="layui-input search_input">
								</div>
                                <label class="layui-form-label">e-mail:</label>
								<div class="layui-input-inline" style="width:140px;">
									<input type="text" name="email" value="" placeholder="请输入关键字" class="layui-input search_input">
								</div>
								<label class="layui-form-label">用户角色:</label>
								<div class="layui-input-inline" style="width:140px;">
									<select name="roleId" id="resRole">
										<option selected="selected"></option>
									</select>
								</div>
								<label class="layui-form-label" >可用状态:</label>
								<div class="layui-input-inline" style="width:140px;">
									<select name="status" >
										<option selected="selected"></option>
										<option value="ENABLE">可用</option>
										<option value="DISABLE">不可用</option>
									</select>
								</div>
								<shiro:hasPermission name="sys:user:list">
								<a class="layui-btn layui-btn-normal searchList_btn" lay-submit lay-filter="searchFilter"><i class="layui-icon  layui-icon-search"></i>查询</a>
								</shiro:hasPermission>
							</div>
						</form>
					</div>
			</blockquote>
			<div class="larry-separate"></div>
			<!-- 角色列表 -->
			<div class="layui-tab-item layui-show" style="padding: 10px 15px;">
				<shiro:hasPermission name="sys:user:add">
					<div class="layui-inline" style="margin-bottom: 10px;">
						<a class="layui-btn layui-btn-normal  resAdd_btn"> <i class="layui-icon  layui-icon-add-circle"></i>新增用户</a>
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

		var roleList;
		function searchRoleList() {
			$.ajax({
				url : '${ctx}/role/list.do',
				type : 'post',
				async: false,
				data : {
				},
				success : function(data) {
					if(data.code === "0000"){
						$('#resRole option').not(":first").remove();
						roleList = data.data;
						roleList.forEach(item => {
							$("#resRole").append(
								'<option value="'+item.id+'">'+item.name+'</option>'
							);
						})
					}
				}
			});
			form.render($('#resRole'));
		}
		searchRoleList();



		function searchList(data) {
			var field = data && data.field
			table.render({
				elem: '#resTableList',
				url: '${ctx}/user/list.do',
				id:'resTableId',
				method: 'post',
				loading:true,
				skin:'row',
				even:'true',
				size: 'sm',
				where: field,
				cols: [[
					{field:'id', title: '序号', width: '11%', align:'center'},
					{field:'roleId', title: '用户角色', width: '11%', align:'center',templet: function (d) {
						var roleName = '';
						roleList.forEach(item => {
							if (item.id === d.roleId) {
								roleName = item.name;
							}
						})
						return roleName;
					}},
					{field:'loginName', title: '登录账号', width: '11%', align:'center'},
					{field:'userName', title: '用户昵称', width: '11%', align:'center'},
					{field:'phoneNo', title: '电话号码', width: '11%', align:'center'},
					{field:'email', title: '电子邮箱', width: '11%', align:'center'},
					{field:'status', title: '可用状态', width: '11%', align:'center',templet: '#userStatusTpl'},
					{field:'createTime', title: '创建时间', width: '12%', align:'center', templet: '#timeTpl'},
					{fixed:'right', title: '操作', width: '11%', align:'center',toolbar: '#resBar'}
				]],
				page: true,
				limit: 20
			});
		}

		<shiro:hasPermission name="sys:user:list">
		searchList();
		</shiro:hasPermission>


		/**查询*/
		$(".searchList_btn").click(function(){
			form.on('submit(searchFilter)', function (data) {
				searchList(data);
			});
		});

		/**新增角色*/
		$(".resAdd_btn").click(function(){
			var url = "${ctx}/user/add";
			common.cmsLayOpen('新增角色',url,'880px','500px');
		});

		/**监听工具条*/
		table.on('tool(resTableId)', function(obj) {
			var data = obj.data;
			var layEvent = obj.event;
			var url;
			switch (layEvent) {
				case 'res_edit':
					url =  '${ctx}/user/upd?id=' + data.id;
					common.cmsLayOpen('编辑角色',url,'880px','500px');
					break;
				case 'res_del':
					url = "${ctx}/user/del.do";
					var param = {id: data.id};
					common.ajaxCmsConfirm('系统提示', '确定删除该角色?',url,param);
					break;
				default:
					break;
			}
		});
	});

</script>
<!-- 角色级别tpl-->
<script type="text/html" id="userStatusTpl">
    {{# if(d.status == 'ENABLE'){ }}
    <span>可用</span>
    {{# } else if(d.status == 'DISABLE'){ }}
    <span>不可用</span>
    {{# } }}
</script>

<!-- 时间tpl-->
<script type="text/html" id="timeTpl">
	{{layui.util.toDateString(d.createTime, 'yyyy-MM-dd HH:mm:ss')}}
</script>

<!--工具条 -->
<script type="text/html" id="resBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="sys:user:upd">
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_edit"><i class="layui-icon  layui-icon-edit"></i>编辑</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="sys:user:del">
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
		</shiro:hasPermission>
	</div>
</script>

</body>
</html>