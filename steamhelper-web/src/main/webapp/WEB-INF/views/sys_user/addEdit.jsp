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
	<input id="userId" name="id" type="hidden" value="${user.id}">
	<input id="pageFlag"  type="hidden" value="${pageFlag}">
	<input id="initRoleId"  type="hidden" value="${user.roleId}">

	<div class="layui-form-item">
		<div class="layui-input-wrap">
			<div class="layui-input-prefix">
				<i class="layui-icon layui-icon-username"></i>
			</div>
		</div>
		<div class="layui-input-block">
			<input type="text" class="layui-input" name="loginName" style="height: 38px;" lay-verify="required|loginName" maxlength="20"  value="${user.loginName}" placeholder="请输入登录账号" >
			<span style="color:red">*</span>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-input-wrap">
			<div class="layui-input-prefix">
				<i class="layui-icon layui-icon-username"></i>
			</div>
		</div>
		<div class="layui-input-block">
			<input type="text" class="layui-input" name="userName" style="height: 38px;" lay-verify="required|userName" maxlength="20"  value="${user.userName}" placeholder="请输入用户昵称" ><span style="color:red">*</span>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-input-prefix">
			<i class="layui-icon layui-icon-cellphone"></i>
		</div>
		<div class="layui-input-block">
			<input type="text" class="layui-input" name="phoneNo" style="height: 38px;" lay-verify="number|phoneNo" maxlength="20"  value="${user.phoneNo}" placeholder="请输入电话号码" >
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-input-prefix">
			<i class="layui-icon layui-icon-email"></i>
		</div>
		<div class="layui-input-block">
			<input type="text" class="layui-input" name="email" style="height: 38px;" maxlength="20"  value="${user.email}" placeholder="请输入电子邮箱" >
		</div>
	</div>
	<c:if test="${pageFlag == 'upd'}">
		<div class="layui-form-item">
			<label class="layui-form-label">用户角色</label>
			<div class="layui-input-inline">
				<select name="roleId" id="roleId">
					<c:forEach items="${roles}" var="item">
					<option value="${item.id}">${item.name}</option>
<%--					<option value="${item.id}" <c:if test="${user.roleId} eq ${item.id}">selected</c:if>>${item.name}</option>--%>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">用户状态</label>
			<div class="layui-input-block">
				<input type="radio" name="status" value="ENABLE" title="有效"   <c:if test="${user.status == 'ENABLE' }">checked</c:if>/>
				<input type="radio" name="status" value="DISABLE" title="失效"   <c:if test="${user.status == 'DISABLE' }">checked</c:if>/>
			</div>
		</div>
	</c:if>
    <div class="layui-form-item" style="text-align: center;">
        <button class="layui-btn" lay-submit="" lay-filter="saveBtn">保存</button>
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

	function initUpd() {
		var pageFlag = $("#pageFlag").val()
		if (pageFlag === 'upd') {
			$('#roleId').val($('#initRoleId').val());
			form.render($('#roleId'));
		}
	}
	initUpd();


	/**表单验证*/
	form.verify({
		loginName: function(value, item){
			if (!new RegExp("^([0-9a-zA-Z]){2,20}$").test(value)) {
				return '用户姓名只能为数字或字母，长度2-20位';
			}
		},
		userName: function(value, item) {
			if (!new RegExp("^([0-9a-zA-Z\u4e00-\u9fa5\-_]){2,20}$").test(value)) {
				return '用户昵称只能为中文、数字、字母或-_';
			}
		},
		phoneNo: function (value, item) {
			if (value && value.length !== 11) {
				return '请输入正确的电话号码';
			}
		}
	});

	/**保存*/
	form.on("submit(saveBtn)",function(data){
		var pageFlag = $("#pageFlag").val();
		$.ajax({
			url : '${ctx}/user/' + pageFlag + '.do',
			type : 'post',
			async: false,
			data : data.field,
			success : function(data) {
				if(JSON.parse(data).code === "0000"){
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