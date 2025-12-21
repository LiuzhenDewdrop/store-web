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
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">

    <script src="${ctx}/static/layui/layui.js"></script>
    <style type="text/css">
        .layui-form-item .layui-form-label{
            width: 150px;
        }
        .layui-form-item .layui-details{
            width: 190px;
        }
		.layui-form-item .layui-input-inline {
			width: 240px;
		}
        .layui-form-item {
            margin-bottom: 0px;
        }
        .layui-input{
            height: 38px;
        }
    </style>
</head>
<body class="childrenBody" style="font-size: 12px;margin: 10px 10px 0;">
<form class="layui-form layui-form-pane">
	<input id="pageFlag"  type="hidden" value="${pageFlag}">
	<input id="resId" name="id" type="hidden" value="${menu.id}">
	<input id="resParentCount"  type="hidden" value="${resParentCount}">

	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">菜单名称</label>
			<div class="layui-input-inline">
				<input type="text" id="resName" name="name" class="layui-input" maxlength="20" value="${menu.name}" lay-verify="required|name" placeholder="请输入菜单名称">
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">菜单图标</label>
			<div class="layui-input-inline">
				<input type="text" class="layui-input" id="resImage" name="icon" lay-verify="required" value="${menu.icon}" readonly>
			</div>
			<div class="layui-form-mid layui-word-aux">
				<a class="layui-btn layui-btn-xs select_img" data-id="" title="选择图标"><i class="layui-icon layui-icon-picture"></i></a>'
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">菜单类型</label>
			<div class="layui-input-inline">
				<input type="radio" name="menuType" lay-filter="menuTypeFilter" value="1" title="菜单">
				<input type="radio" name="menuType" lay-filter="menuTypeFilter" value="2" title="按钮">
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">菜单级别</label>
			<div class="layui-input-inline" id="resLevel">
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">父级菜单</label>
			<div class="layui-input-inline">
				<select id="resParentId" name="pId" lay-verify="resParentId">
					<option value="0" selected>请选择</option>
				</select>
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">菜单路径</label>
			<div class="layui-input-inline">
				<input type="text" id="resUrl" name="url" class="layui-input" maxlength="100" lay-verify="resUrl"  value="${menu.url}" placeholder="请输入菜单路径"/>
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">授权标识</label>
			<div class="layui-input-inline">
				<input type="text" id="resPermission" name="permission" class="layui-input" lay-verify="required|resPermission" maxlength="30" value="${menu.permission}" placeholder="如：user:config"/>
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">显示顺序</label>
			<div class="layui-input-inline">
				<input type="text" id="resDisplayOrder" name="displayOrder" class="layui-input" maxlength="10" lay-verify="resDisplayOrder"  value="${menu.displayOrder}" placeholder="请输入菜单顺序"/>
			</div>
		</div>
    </div>
	<div class="layui-form-item">
		<label class="layui-form-label">备注</label>
		<div class="layui-input-block" style="margin-left: 150px;">
			<textarea type="text" name="remark" class="layui-textarea" maxlength="100" style="resize:none;min-height:80px;width: 93%;" placeholder="请输入备注">${menu.remark}</textarea>
		</div>
	</div>
    <div class="layui-form-item" style="text-align: center;margin-top: 18px;">
        <button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="saveRes">保存</button>
        <button type="layui-btn" id="cancle" class="layui-btn layui-btn-primary">取消</button>
    </div>
</form>
<script type="text/javascript">
layui.config({
	base : "${ctx}/static/js/"
}).use(['form','layer','jquery','commCms'],function() {
	var $ = layui.$,
	form = layui.form,
	common = layui.commCms,
	layer = parent.layer === undefined ? layui.layer : parent.layer;

	/*初始化*/
	resInit();

	function resInit() {
		var pageFlag = $("#pageFlag").val();
		switch (pageFlag) {
			case "add":
				changeMenuType('1');
				break;
			case "upd":
				menuTypeVal = '${menu.menuType}';
				levelVal = '${menu.level}';
				pIdVal = '${menu.pId}';
				changeMenuType(menuTypeVal, levelVal);
				$("#resParentId option[value='" + pIdVal + "']").prop("selected","selected");
				break;
			default:
				break;
		}
	}

	function changeMenuType(menuType, level) {
		$('input[name="menuType"][value="' + menuType + '"]').prop('checked', true);
		$("#resLevel").empty()
		switch (menuType) {
			case '1':
				$("#resLevel").append('<input type="radio" name="level" lay-filter="levelFilter" value="1" title="一级菜单">');
				$("#resLevel").append('<input type="radio" name="level" lay-filter="levelFilter" value="2" title="二级菜单">');
				changeLevel(level ? level : '1');
				break;
			case '2':
				$("#resLevel").append('<input type="radio" name="level" lay-filter="levelFilter" value="3" title="三级菜单">');
				changeLevel('3');
				break;
			default:
				break;
		}
		form.render('radio');
	}

	function changeLevel(level) {
		$('input[name="level"][value="' + level + '"]').prop('checked', true);
		$('#resParentId option').not(":first").remove();
		switch (level) {
			case '1':
				$("#resUrl").val(null);
				$("#resUrl").attr("disabled","disabled");
				$("#resParentId option[value='0']").prop("selected","selected");
				break;
			case '2':
				$("#resUrl").removeAttr("disabled","disabled");
				$("#resDisplayOrder").removeAttr("disabled","disabled");
				break;
			case '3':
				$("#resUrl").val(null);
				$("#resUrl").attr("disabled","disabled");
				$("#resDisplayOrder").val(null);
				$("#resDisplayOrder").attr("disabled","disabled");
				break;
			default:
				break;
		}
		loadParentMenu(level);
		form.render();
	}

	/**监听菜单类型选择*/
	form.on('radio(menuTypeFilter)', function(data) {
		changeMenuType(data.value)
	});

	/**监听菜单级别选择*/
	form.on('radio(levelFilter)', function(data) {
		changeLevel(data.value);
	});

	/**加载父级菜单*/
	function loadParentMenu(level) {
		if (level === '1') {
			return ;
		}
		$.ajax({
			url : '${ctx}/menu/findParentMenu.do',
			type : 'post',
			async: false,
			data : {
				level: level
			},
			success : function(data) {
				if (data.code === "0000") {
					$(data.data).each(function(index, item) {
						$("#resParentId").append(
							'<option value="'+item.id+'">'+item.name+'</option>'
						);
					});
				}
			}
		});
	}

	/**选择图标*/
	$(".select_img").click(function() {
		var url = "${ctx}/menu/icon";
		common.cmsLayOpen('选择图标', url, '485px', '520px');
	});

	/**表单验证*/
	form.verify({
		resName: function(value, item) {
			// 验证菜单名称
			if (!new RegExp("^[0-9a-zA-Z\u4e00-\u9fa5\-_]+$").test(value)) {
				return '菜单名称只能为中文、数字、字母或-_';
			}
		},
		resParentId: function(value, item) {
			// 验证父级菜单
			var level = $("input[name='level']:checked").val();
			if (level === '1') {
				return ;
			}
			if (!value || value === '0') {
				return '父级菜单不能为空';
			}
		},
		resUrl: function(value, item) {
			// 验证菜单路径
			var level = $("input[name='level']:checked").val();
			if (level === '1' || level === '3') {
				return ;
			}
			if (!value) {
				return '菜单路径不能为空';
			}
			if (!new RegExp("^[a-zA-Z_/.-]+$").test(value)) {
				return '菜单路径只能为英文下划线左斜杠和点';
			}
		},
		resPermission: function(value, item) {
			if (!value) {
				return '授权标识不能为空';
			}
		},
		resDisplayOrder: function (value, item) {
			var level = $("input[name='level']:checked").val();
			if (level === '3') {
				return ;
			}
			if (!value) {
				return '顺序不能为空';
			}
			if (!new RegExp("^[0-9]+$").test(value)) {
				return '顺序只能为数字';
			}
		}
	});

	/**保存*/
	form.on("submit(saveRes)",function(data) {
		var pageFlag = $("#pageFlag").val();
		$.ajax({
			url : '${ctx}/menu/' + pageFlag + '.do',
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
	$("#cancle").click(function() {
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭
	});

});
</script>
</body>
</html>