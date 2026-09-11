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
<style type="text/css">
.layui-form-pane .layui-form-label {
	width: 150px;
}
</style>
</head>
<body class="childrenBody" style="font-size: 12px;margin: 10px 10px 0;">
<form class="layui-form layui-form-pane">
	<input id="accId" name="id" type="hidden" value="${acc.id}">
	<input id="userId" name="userId" type="hidden" value="${acc.userId}">
	<input id="pageFlag"  type="hidden" value="${pageFlag}">
	<input id="initPlatformId"  type="hidden" value="${acc.platformId}">

	<div class="layui-form-item">
		<label class="layui-form-label">游戏平台</label>
		<div class="layui-input-inline">
			<select name="platformId" id="platformId">
				<c:forEach items="${platforms}" var="item">
					<option value="${item.dictValue}">${item.dictKey}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">登录账号</label>
		<div class="layui-inline">
			<input type="text" class="layui-input" name="loginName" style="height: 38px;" lay-verify="required" maxlength="20"  value="${acc.loginName}" placeholder="请输入登录账号" >
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">用户昵称</label>
		<div class="layui-inline">
			<input type="text" class="layui-input" name="userName" style="height: 38px;" lay-verify="required" maxlength="20"  value="${acc.userName}" placeholder="请输入用户昵称" >
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">在平台的用户id</label>
		<div class="layui-inline">
			<input type="text" class="layui-input" name="platformUserId" style="height: 38px;" maxlength="20" lay-verify="number" value="${acc.platformUserId}" placeholder="请输入" >
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">在平台的用户no</label>
		<div class="layui-inline">
			<input type="text" class="layui-input" name="platformUserNo" style="height: 38px;" maxlength="20"  value="${acc.platformUserNo}" placeholder="请输入" >
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">在平台的用户id或no</label>
		<div class="layui-inline">
			<input type="radio" name="numType" value="1" title="user_id" checked>
			<input type="radio" name="numType" value="2" title="user_no">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">token</label>
		<div class="layui-inline">
			<input type="text" class="layui-input" name="accountToken" style="height: 38px;" maxlength="100"  value="${acc.accountToken}" placeholder="请输入" >
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">账号地位</label>
		<div class="layui-inline">
			<c:if test="${pageFlag == 'add'}">
				<input type="radio" name="mainAccount" value="1" title="主账号">
				<input type="radio" name="mainAccount" value="0" title="非主账号" checked>
			</c:if>
			<c:if test="${pageFlag == 'upd'}">
				<input type="radio" name="mainAccount" value="1" title="主账号"   <c:if test="${acc.mainAccount == 1 }">checked</c:if>>
				<input type="radio" name="mainAccount" value="0" title="非主账号" <c:if test="${acc.mainAccount == 0 }">checked</c:if>>
			</c:if>
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">账号排序</label>
		<div class="layui-inline">
			<input type="text" class="layui-input" name="accountSort" style="height: 38px;" maxlength="20" lay-verify="number" value="${acc.accountSort}" placeholder="请输入" >
		</div>
	</div>
    <div class="layui-form-item" style="text-align: center;">
        <button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="saveBtn">保存</button>
        <button type="layui-btn layui-btn-primary" id="cancle" class="layui-btn layui-btn-primary">取消</button>

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

	function initPlatform() {
		var pageFlag = $("#pageFlag").val()
		if (pageFlag === 'upd') {
			$('#platformId').val($('#initPlatformId').val());
			form.render($('#platformId'));
		}
	}
	initPlatform();

	/**保存*/
	form.on("submit(saveBtn)",function(data){
		var pageFlag = $("#pageFlag").val();
		$.ajax({
			url : '${ctx}/acc/' + pageFlag + '.do',
			type : 'post',
			async: false,
			data : data.field,
			success : function(data) {
				if(data.code === "0000"){
					if(pageFlag === 'addPage'){
						common.cmsLaySucMsg("保存成功,默认密码123456,请及时修改")
					}else {
						common.cmsLaySucMsg("保存成功")
					}
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭                        //刷新父页面
					parent.location.reload();
				}else{
					common.cmsLayErrorMsg(data.msg);
				}
			},error:function(data){
				top.layer.close(index);
			}
		});
		return false;
	});
	//取消
	$("#cancle").click(function(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭
	});
});
</script>
</body>
</html>