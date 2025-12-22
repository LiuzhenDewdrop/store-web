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
	<input id="dictId" name="id" type="hidden" value="${dict.id}">
	<div class="layui-form-item">
		<label class="layui-form-label">group</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input"  name="dictGroup" value="${dict.dictGroup}" lay-verify="required|resDictGroup" maxlength="50"  style="height: 38px;" placeholder="请输入">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">key</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input" name="dictKey" value="${dict.dictKey}" lay-verify="required|resDictKey" maxlength="50" style="height: 38px;" placeholder="请输入">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">value</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input" name="dictValue" value="${dict.dictValue}" lay-verify="required" maxlength="250" style="height: 38px;" placeholder="请输入">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">sort</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input" name="dictSort" value="${dict.dictSort}" lay-verify="resDictSort" maxlength="10" style="height: 38px;" placeholder="请输入等级">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">备注</label>
		<div class="layui-input-block" style="margin-left: 110px;">
			<textarea class="layui-textarea" name="remark" maxlength="100" style="resize:none;min-height:80px;width: 80%;" placeholder="请输入内容">${dict.remark}</textarea>
		</div>
	</div>
	<div class="layui-form-item" style="text-align: center;">
		<button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="saveRes">保存</button>
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
		resDictGroup: function(value, item) {
			if (!new RegExp("^[0-9a-zA-Z-_]+$").test(value)) {
				return 'group只能为数字、字母或-_';
			}
		},
		resDictKey: function(value, item) {
			if (!new RegExp("^[0-9a-zA-Z-_]+$").test(value)) {
				return 'key只能为数字、字母或-_';
			}
		},
		resDictSort: function (value, item) {
			if (!value) {
				return 'sort不能为空';
			}
			return Number.isInteger(value);
		}
	});

	/**保存*/
	form.on("submit(saveRes)",function(data) {
		var pageFlag = $("#pageFlag").val();
		$.ajax({
			url : '${ctx}/dict/' + pageFlag + '.do',
			type : 'post',
			async: false,
			data : data.field,
			success : function(data) {
				if (data.code === "0000") {
					location.reload();
					common.cmsLaySucMsg("保存成功");
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭
					parent.location.reload();
				} else {
					common.cmsLayErrorMsg(data.msg);
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