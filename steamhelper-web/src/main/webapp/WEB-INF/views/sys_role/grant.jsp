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

    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">


    <script src="${ctx}/static/layui/layui.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/jquery-1.8.3.js"></script>


</head>
<body style="font-size:12px;">
<fieldset class="layui-elem-field">
    <legend  style="font-size: 12px;color:#FF5722;">请选择菜单信息</legend>
	<div id="menuTree"></div>
</fieldset>
<input id="roleId" type="hidden"  value="${roleId}" >
<div class="layui-form-item" style="text-align: center;">
    <button class="layui-btn" id="saveRoleGrant">保存</button>
    <button id="cancle" class="layui-btn layui-btn-primary">取消</button>
</div>




<script type="text/javascript">
var resourceTree;
layui.config({
	base : "${ctx}/static/js/"
}).use(['form','layer','jquery','commCms', 'tree'], function() {
	var $ = layui.$,
	form = layui.form,
	common = layui.commCms,
	tree = layui.tree,
	layer = parent.layer === undefined ? layui.layer : parent.layer;

	tree.render({
		elem: '#menuTree',
		id: 'resTree',
		showCheckbox: true,
		data: ${tree},
		customName: {
			id: 'id',
			title: 'name',
			children: 'children'
		},
	});

	/**角色菜单信息保存*/
	$("#saveRoleGrant").click(function() {
		//角色Id
		var roleId = $("#roleId").val();
		var checkData = tree.getChecked('resTree');
		var menuIds = [];
		checkData.forEach(node1 => {
			menuIds.push(node1.id);
			if (node1.children) {
				node1.children.forEach(node2 => {
					menuIds.push(node2.id);
					if (node2.children) {
						node2.children.forEach(node3 => {
							menuIds.push(node3.id);
						})
					}
				})
			}
		});

		$.ajax({
			url : '${ctx}/role/grant.do',
			type : 'post',
			async: false,
			data : {
				roleId: roleId,
				menuIds: menuIds
			},
			success : function(data) {
				if(JSON.parse(data).code === "0000") {
					common.cmsLaySucMsg("角色授权信息保存成功")
					var index = parent.layer.getFrameIndex(window.name);
					parent.layer.close(index);
					parent.location.reload();
				}else{
					common.cmsLayErrorMsg(JSON.parse(data).msg);
				}
			}
		});

	});
	//取消
	$("#cancle").click(function() {
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭
	});
});
</script>
</body>
</html>