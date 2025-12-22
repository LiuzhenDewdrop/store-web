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
<body  style="font-size: 12px;margin: 10px 10px 0;">
<div style="text-align: center;">
    <form class="layui-form layui-form-pane">
        <input id="userId" name="id" type="hidden" value="${user.id}">
        <input type="hidden" class="layui-input search_input" name="userName" value="${user.userName}">

        <div class="layui-form-item">
            <label class="layui-form-label">原始密码</label>
            <div class="layui-input-block">
                <input type="password" class="layui-input search_input" name="password" style="width: 250px;height: 38px;" lay-verify="required|password" maxlength="20"  placeholder="请输入原始密码">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">新密码</label>
            <div class="layui-input-block">
                <input type="password" class="layui-input search_input" name="newPassword" style="width: 250px;height: 38px;" lay-verify="required|newPassword" maxlength="20"  placeholder="请输入新密码">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">确认新密码</label>
            <div class="layui-input-block">
                <input type="password" class="layui-input search_input" name="newPasswordConfirm" style="width: 250px;height: 38px;" lay-verify="required|newPasswordConfirm" maxlength="20" placeholder="请确认密码">
            </div>
        </div>
        <div class="layui-form-item" style="text-align: center;">
            <button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="saveUser">保存</button>
            <button class="layui-btn layui-btn-primary" type="layui-btn" id="cancle">取消</button>

        </div>
    </form>
</div>
<script type="text/javascript">
layui.config({
	base : "${ctx}/static/js/"
}).use(['form','layer','jquery','commCms'],function(){
	var $ = layui.$,
			form = layui.form,
			common = layui.commCms,
			layer = layui.layer;

	/**表单验证*/
	form.verify({
		password: function(value, item){
			//验证登录账号
			if(value ==null ||value==""){
				return '输入原始密码';
			}
		},
		newPassword: function(value, item){
			if(!CheckPassWord(value)){
				return '密码只能为英文和数字组合，长度至少6位';
			}
		},
		newPasswordConfirm: function(value, item){
			//验证用户名
			if(!CheckPassWord(value)){
				return '密码只能为英文和数字组合，长度至少6位';
			}
			var newPassword = $("input[ name='newPassword']").val();
			if(value!=newPassword){
				return '需要输入两次相同的密码';
			}

		}
	});

	/**保存*/
	form.on("submit(saveUser)",function(data){
		common.ajaxCmsConfirm("确认修改密码","确认修改",'${ctx}/user/updPwd.do', data.field, parent.location);
		return false;
	});


});
function CheckPassWord(password) {
	var str = password;
	if (str == null || str.length < 6 || str.length> 20) {
		return false;
	}
	var reg1 = new RegExp(/^[0-9A-Za-z]+$/);
	if (!reg1.test(str)) {
		return false;
	}
	var reg = new RegExp(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/);
	if (reg.test(str)) {
		return true;
	} else {
		return false;
	}
}
</script>
</body>
</html>