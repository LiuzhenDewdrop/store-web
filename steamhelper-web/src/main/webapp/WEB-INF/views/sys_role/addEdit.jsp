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

    <script src="${ctx}/static/layui/layui.js"></script>

</head>
<body class="childrenBody" style="font-size: 12px;margin: 10px 10px 0;">
<form class="layui-form layui-form-pane">
	<input id="pageFlag"  type="hidden" value="${pageFlag}">
	<input id="roleId" name="id" type="hidden" value="${role.id}">
	<div class="layui-form-item">
		<label class="layui-form-label">角色编码</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input"  name="roleCode" value="${role.roleCode}" lay-verify="required|resRoleCode" maxlength="10"  style="height: 38px;" placeholder="请输入角色编码">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">角色名称</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input" name="name" value="${role.name}" lay-verify="required|resName" maxlength="10" style="height: 38px;" placeholder="请输入角色名称">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">等级</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input" name="level" value="${role.level}" lay-verify="resLevel" maxlength="10" style="height: 38px;" placeholder="请输入等级">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">备注</label>
		<div class="layui-input-block" style="margin-left: 110px;">
			<textarea class="layui-textarea" name="remark" maxlength="100" style="resize:none;min-height:80px;width: 80%;" placeholder="请输入内容">${role.remark}</textarea>
		</div>
	</div>
	<div class="layui-form-item" style="text-align: center;">
		<button class="layui-btn" lay-submit="" lay-filter="saveRes">保存</button>
		<button type="layui-btn" id="cancle" class="layui-btn layui-btn-primary">取消</button>
	</div>
</form>
<script type="text/javascript">
layui.config({
	base : "${ctx}/static/js/"
}).use(['form','layer','jquery','commCms'],function(){
	var $ = layui.$,
	form = layui.form,
	common = layui.commCms,
	layer = parent.layer === undefined ? layui.layer : parent.layer;

	/**表单验证*/
	form.verify({

		resRoleCode: function(value, item) {
			if (!new RegExp("^[0-9a-zA-Z-_]+$").test(value)) {
				return '角色编码只能为数字、字母或-_';
			}
		},
		resName: function(value, item) {
			if (!new RegExp("^[0-9a-zA-Z\u4e00-\u9fa5\-_]+$").test(value)) {
				return '角色名称只能为中文、数字、字母或-_';
			}
		},
		resLevel: function (value, item) {
			if (!value) {
				return '等级不能为空';
			}
			if (!new RegExp("^[0-9]+$").test(value)) {
				return '等级只能为自然数';
			}
		}
	});

	/**保存*/
	form.on("submit(saveRes)",function(data) {
		var pageFlag = $("#pageFlag").val();
		$.ajax({
			url : '${ctx}/role/' + pageFlag + '.do',
			type : 'post',
			async: false,
			data : data.field,
			success : function(data) {
				if (JSON.parse(data).code === "0000") {
					location.reload();
					common.cmsLaySucMsg("保存成功");
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭
					parent.location.reload();
				} else {
					common.cmsLayErrorMsg(JSON.parse(data).msg);
				}
			}, error:function(data) {
				layer.close(index);
			}
		});
		return false;
	});

	/**取消*/
	$("#cancle").click(function(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭
	});

});

</script>
</body>
</html>