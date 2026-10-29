<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>乐活益家运营平台</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="乐活益家运营平台">
    <meta name="description" content="五人齐商铺 一个买卖东西的地方">
    <link rel="shortcut icon" href="${ctx}/static/img/5r7_logo.png">

    <link rel="stylesheet" href="${ctx}/static/layui/css/layui.css">
    <link rel="stylesheet" href="${ctx}/static/css/global.css">

    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" media="all">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">

    <script src="${ctx}/static/layui/layui.js"></script>
<%--    <script src="${ctx}/static/layui_exts/tinymce/tinymce.js"></script>--%>
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
	<input id="resId" name="id" type="hidden" value="${detail.item.id}">

	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">商品轮播图</label>

			<div class="layui-form-mid layui-word-aux">
				<a class="layui-btn layui-btn-xs layui-btn-normal select_picture" id="uploadPicBtn" data-id="" title="选择图片"><i class="layui-icon layui-icon-picture"></i>选择图片</a>'
			</div>
		</div>
	</div>
	<table id="picTable"></table>

    <div class="layui-form-item" style="text-align: center;margin-top: 18px;height:138px;">
		<div class="layui-inline">
			<button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="saveRes">保存</button>
			<button id="cancle" class="layui-btn layui-btn-primary">取消</button>
		</div>
    </div>
</form>
<script type="text/javascript">
layui.config({
	base : "${ctx}/static/js/",
}).extend({
	tinymce: 'layui_exts/tinymce/tinymce'
}).use(['form','layer','jquery','commCms','upload', 'table', 'tinymce'],function() {
	var $ = layui.$,
	form = layui.form,
	common = layui.commCms,
	upload = layui.upload,
	element = layui.element,
	table = layui.table,
	layer = parent.layer === undefined ? layui.layer : parent.layer;

	function resInit() {
		var itemPictures = '${detail.pics}';
		if (itemPictures) {
			itemPictures = JSON.parse('${detail.pics}');
		} else {
			itemPictures = [];
		}

		table.render({
			elem: '#picTable',
			data: itemPictures,
			initSort: {
				field: 'sortNo',
				type: 'asc'
			},
			skin:'row',
			even:'true',
			size: 'lg',
			height: '120',
			maxHeight: '500',
			cols: [[
				// {field:'id', title: '序号', width: '6%', align:'center'},
				{field:'sortNo', title: '排序(升序)', width: '10%', align:'center', sort:true},
				{field:'picUrl', title: '商品图片', width: '20%', minWidth: '120', align:'center',templet: '#resImageTpl'},
				// {field:'progressPercent', title: '上传进度', width: '10%', align:'center',templet: '#resProgressTpl'},
				{title: '上传进度', width: '20%', align:'center',templet: '#resProgressTpl'},
				{fixed:'right', title: '操作', width: '20%', align:'center',toolbar: '#resPicBar'}
			]],
		});
	}

	/*初始化*/
	resInit();

	// 上传轮播图片
	var uploadInst1 = upload.render({
		elem: '#uploadPicBtn',
		url: '${ctx}/item/upload.do',
		before: function(obj) {
			// 预读本地文件示例，不支持ie8
			obj.preview(function(index, file, result) {
				var picCache = table.cache['picTable'];
				let maxSortNo = 0;
				if (picCache) {
					for (let i = 0; i < picCache.length; i++) {
						let item = picCache[i].sortNo;
						if (item && item > maxSortNo) {
							maxSortNo = item;
						}
					}
				}
				picCache.push({
					sortNo: maxSortNo + 1,
					picUrl: result,
					index: index,
				});
				table.renderData('picTable');
				element.render('progress'); // 渲染新加的进度条组件
			});
		},
		done: function(res, index) {
			// 上传结果
			if(res.code === '0000') {
				var result = res.data;
				var picCache = table.cache['picTable'];
				const pic = picCache.find(pic => pic.index === index);
				pic.picUrl = result;
				table.renderData('picTable');
				layer.msg('上传完毕', {icon: 1});
			} else {
				return layer.msg('上传失败:'+res.msg);
			}
		},
		error: function(index) {
			var picCache = table.cache['picTable'];
			const picIndex = picCache.findIndex(pic => pic.index === index);
			if (picIndex !== -1) {
				picCache.splice(picIndex, 1);
			}
			return layer.msg('上传失败');
		},
		progress: function(n, elem, e, index){
			element.progress('progress-'+ index, n + '%');
		}
	});

	table.on('tool(picTable)', function(obj) {
		var data = obj.data;
		var layEvent = obj.event;
		switch (layEvent) {
			case 'delPic':
				obj.del();
				break;
			case 'changeSort':
				layer.prompt({title: '请输入排序数字0靠前，9靠后', formType: 0, type: 'number'}, function(text, index){
					layer.close(index);
					if (text == null || text === '') {
						layer.msg("请输入内容");
						return ;
					}
					if (isNaN(text)) {
						layer.msg("不是数字，请重新输入");
						return ;
					}
					var num = parseInt(text);
					if (num < 1) {
						layer.msg("请输入正整数");
						return ;
					}
					obj.dataCache.sortNo = num;
					table.renderData('picTable');
					table.sort();
				});
				break;
			case 'detail':
				layer.photos({
					photos: {
						"title": "图片详情",
						"start": 0,
						"data": [
							{
								"alt": "图片详情",
								"src": obj.data.picUrl,
							}
						]
					},
					// footer: false // 是否显示底部栏 --- 2.8.16+
				});
				break;
			default:
				break;
		}
	});

	/**保存*/
	form.on("submit(saveRes)",function(data) {
		var pageFlag = $("#pageFlag").val();
		var itemImageUrl = $('#itemImage').attr('data-url')
		var specs = [];
		switch ($('input[name="modelType"]:checked').val()) {
			case '1':
				specs.push({
					specName: $('#specName').val(),
					picUrl: itemImageUrl,
					supplyPrice: $('#supplyPrice').val(),
					salePrice: $('#salePrice').val(),
					specStatus: 1,
				});
				break;
			case '2':
				specs = table.cache['specTable'];
				break;
			default:
				break;
		}
		var postData = {
			item: data.field,
			specList: specs,
			picList: table.cache['picTable'],
			desc: table.cache['descTable'],
		};
		postData.item.itemImage = itemImageUrl;
		postData.item.itemDetail = tinymce.get('#itemDetail').getContent();
		$.ajax({
			url : '${ctx}/item/' + pageFlag + '.do',
			type : 'post',
			async: false,
			contentType: 'application/json;charset=utf-8',
			data : JSON.stringify(postData),
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
		// var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		// parent.layer.close(index); //再执行关闭
		var layId = top.layui.$('.layui-tab-title .layui-this').attr('lay-id')
		parent.layui.element.tabDelete('bodyTab', layId).init();
	});

});
</script>

<!-- 商品图片tpl-->
<script type="text/html" id="resImageTpl">
	<div class="item-img">
		<a class="img" title="商品图片">
			<img src="{{= d.picUrl}}"  style="width: 120px; height: 120px;">
		</a>
	</div>
</script>

<!-- 进度条tpl-->
<script type="text/html" id="resProgressTpl">
	{{# if(d.index){ }}
		<div class="layui-progress" lay-filter="progress-{{= d.index}}"><div class="layui-progress-bar" lay-percent=""></div></div>
	{{# } else if(d.index == null) { }}
		<div class="layui-progress"><div class="layui-progress-bar" lay-percent=""></div></div>
	{{# } }}
</script>

<!--工具条 -->
<script type="text/html" id="resPicBar">
	<div class="layui-btn-group">
		<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="detail"><i class="layui-icon  layui-icon-edit"></i>查看</a>
		<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="changeSort"><i class="layui-icon  layui-icon-edit"></i>修改排序</a>
		<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="delPic"><i class="layui-icon  layui-icon-delete"></i>删除</a>
	</div>
</script>

<!--工具条 -->
<script type="text/html" id="resBar">
	<div class="layui-btn-group">
		{{# if(d.specStatus && d.specStatus == 1){ }}
		<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_down"><i class="layui-icon  layui-icon-down"></i>下架</a>
		{{# } else if(d.specStatus && d.specStatus == 2){ }}
		<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_up"><i class="layui-icon  layui-icon-up"></i>上架</a>
		{{# } }}
		<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_del"><i class="layui-icon  layui-icon-delete"></i>删除</a>
	</div>
</script>
</body>
</html>