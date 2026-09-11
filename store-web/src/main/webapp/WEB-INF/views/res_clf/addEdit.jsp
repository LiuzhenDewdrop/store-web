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

    <script src="${ctx}/static/layui/layui.js"></script>

</head>
<body class="childrenBody" style="font-size: 12px;margin: 10px 10px 0;">
<form class="layui-form layui-form-pane">
	<input id="pageFlag"  type="hidden" value="${pageFlag}">
	<input id="clfId" name="id" type="hidden" value="${clf.id}">
	<div class="layui-form-item">
		<label class="layui-form-label">系列</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input"  name="series" value="${clf.series}" lay-verify="required" maxlength="8"  style="height: 38px;" placeholder="请输入系列">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">子系列</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input" name="subSeries" value="${clf.subSeries}" lay-verify="required" maxlength="20" style="height: 38px;" placeholder="请输入子系列">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">简介</label>
		<div class="layui-input-block" style="margin-left: 110px;">
			<textarea class="layui-textarea" name="resume" maxlength="100" style="resize:none;min-height:80px;width: 80%;" placeholder="请输入内容">${clf.resume}</textarea>
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">分类标准</label>
		<div class="layui-input-block" style="margin-left: 110px;">
			<textarea class="layui-textarea" name="standard" maxlength="100" style="resize:none;min-height:80px;width: 80%;" placeholder="请输入分类标准">${clf.standard}</textarea>
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">经典样例</label>
		<div class="layui-input-block" style="margin-left: 110px;">
			<textarea class="layui-textarea" name="sample" maxlength="100" style="resize:none;min-height:80px;width: 80%;" placeholder="请输入经典样例">${clf.sample}</textarea>
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">排序</label>
		<div class="layui-input-inline">
			<input type="text" class="layui-input" name="sort" value="${clf.sort}" lay-verify="required|number" maxlength="20" style="height: 38px;" placeholder="请输入排序">
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

	/**保存*/
	form.on("submit(saveRes)",function(data) {
		var pageFlag = $("#pageFlag").val();
		$.ajax({
			url : '${ctx}/clf/' + pageFlag + '.do',
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