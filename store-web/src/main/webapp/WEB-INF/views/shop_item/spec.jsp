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

	<div id="addSpec">
		<div class="layui-inline">
			<label class="layui-form-label">规格名称</label>
			<div class="layui-input-inline">
				<input id="addSpecSpecName" type="text" name="specName" class="layui-input" maxlength="20" lay-verify="required" value="" placeholder="请输入规格名称">
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">规格图片</label>
			<div class="layui-input-inline" style="width:150px;">
				<a class="img" title="规格图片">
					<img id="itemImage" src="" style="width: 120px; height: 120px;">
				</a>
			</div>
			<div class="layui-form-mid layui-word-aux">
				<a class="layui-btn layui-btn-xs layui-btn-normal select_picture" id="uploadImageBtn" data-id="" title="选择图片"><i class="layui-icon layui-icon-picture"></i>选择图片</a>'
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">供货价格（元）</label>
			<div class="layui-input-inline">
				<input id="addSpecSupplyPrice" type="text" lay-affix="number" min="0" lay-precision="2" class="layui-input" lay-verify="required" value="" placeholder="请输入供货价格">
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">销售价格（元）</label>
			<div class="layui-input-inline">
				<input id="addSpecSalePrice" type="text" lay-affix="number" min="0" lay-precision="2" class="layui-input" lay-verify="required" value="" placeholder="请输入销售价格">
			</div>
		</div>
	</div>
</form>
<script type="text/javascript">
layui.config({
	base : "${ctx}/static/js/"
}).use(['form','layer','jquery', 'upload'],function() {
	var $ = layui.$,
	form = layui.form,
	element = layui.element,
	upload = layui.upload,
	layer = parent.layer === undefined ? layui.layer : parent.layer;

	// 上传封面图片
	var uploadInst = upload.render({
		elem: '#uploadImageBtn',
		url: '${ctx}/item/upload.do',
		before: function(obj) {
			// 预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				$('#itemImage').attr('src', result);
			});
		},
		done: function(res) {
			// 上传结果
			if(res.code === '0000') {
				$('#itemImage').attr('src', res.data);
				$('#itemImage').attr('data-url', res.data);
				layer.msg('上传完毕', {icon: 1});
			} else {
				return layer.msg('上传失败:'+res.msg);
			}
		},
		error: function() {
			return layer.msg('上传失败');
		},
	});
});
</script>

</body>
</html>