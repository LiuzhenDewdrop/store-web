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
			<label class="layui-form-label">商品类别</label>
			<div class="layui-input-inline">
				<c:if test="${pageFlag == 'add' }">
					<input type="radio" name="itemType" value="1" title="实体" checked="checked">
					<input type="radio" name="itemType" value="2" title="虚拟">
				</c:if>
				<c:if test="${pageFlag == 'upd' }">
					<input type="radio" name="itemType" value="1" title="实体" <c:if test="${detail.item.itemType == 1 }">checked</c:if>/>
					<input type="radio" name="itemType" value="2" title="虚拟" <c:if test="${detail.item.itemType == 2 }">checked</c:if>/>
				</c:if>
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">商品名称</label>
			<div class="layui-input-inline">
				<input type="text" id="resName" name="itemName" class="layui-input" maxlength="20" value="${detail.item.itemName}" lay-verify="required|itemName" placeholder="请输入商品名称">
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">商品封面图</label>
			<div class="layui-input-inline" style="width:150px;">
				<a class="img" title="商品封面图">
					<img id="itemImage" src="${ctx}/${detail.item.itemImage}" style="width: 120px; height: 120px;">
				</a>
			</div>
			<div class="layui-form-mid layui-word-aux">
				<a class="layui-btn layui-btn-xs layui-btn-normal select_picture" id="uploadImageBtn" data-id="" title="选择图片"><i class="layui-icon layui-icon-picture"></i>选择图片</a>'
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">商品轮播图</label>

			<div class="layui-form-mid layui-word-aux">
				<a class="layui-btn layui-btn-xs layui-btn-normal select_picture" id="uploadPicBtn" data-id="" title="选择图片"><i class="layui-icon layui-icon-picture"></i>选择图片</a>'
			</div>
		</div>
	</div>
	<table id="picTable"></table>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">商品编码</label>
			<div class="layui-input-inline">
				<input type="text" id="itemCode" name="itemCode" class="layui-input" maxlength="20" value="${detail.item.itemCode}" lay-verify="required|name" placeholder="请输入商品编码">
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">商品分类</label>
			<div class="layui-input-inline" style="width:140px;">
				<select name="itemCategory1" id="itemCategory1" lay-filter="categoryFilter">
					<option selected="selected" value="">请选择1级分类</option>
				</select>
			</div>
			<div class="layui-input-inline" style="width:140px;">
				<select name="itemCategory2" id="itemCategory2" lay-filter="categoryFilter">
					<option selected="selected" value="">请选择2级分类</option>
				</select>
			</div>
			<div class="layui-input-inline" style="width:140px;">
				<select name="itemCategory3" id="itemCategory3">
					<option selected="selected" value="">请选择3级分类</option>
				</select>
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">品牌名称</label>
			<div class="layui-input-inline">
				<input type="text" id="brandName" name="brandName" class="layui-input" maxlength="20" value="${detail.item.brandName}" lay-verify="required|brandName" placeholder="请输入品牌名称">
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">型号类别</label>
			<div class="layui-input-inline" style="width: 500px;">
				<c:if test="${pageFlag == 'add' }">
					<input type="radio" name="modelType" lay-filter="modelTypeFilter" value="1" title="单一型号规格" checked="checked">
					<input type="radio" name="modelType" lay-filter="modelTypeFilter" value="2" title="多种型号规格">
				</c:if>
				<c:if test="${pageFlag == 'upd' }">
					<input type="radio" name="modelType" lay-filter="modelTypeFilter" value="1" title="单一型号规格" <c:if test="${detail.item.modelType == 1 }">checked</c:if>/>
					<input type="radio" name="modelType" lay-filter="modelTypeFilter" value="2" title="多种型号规格" <c:if test="${detail.item.modelType == 2 }">checked</c:if>/>
				</c:if>
			</div>
		</div>
	</div>

	<div id="resModelType1">
		<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">规格名称</label>
			<div class="layui-input-inline">
				<input type="text" id="specName" class="layui-input" maxlength="20" value="${detail.specs[0].specName}" lay-verify="specName" placeholder="请输入规格名称">
			</div>
		</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">供货价格（元）</label>
				<div class="layui-input-inline">
					<input id="supplyPrice" type="text" lay-filter="supplyPrice" lay-affix="number" min="0" lay-precision="2" class="layui-input" lay-verify="supplyPrice" value="${detail.specs[0].supplyPrice}" placeholder="请输入供货价格">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">销售价格（元）</label>
				<div class="layui-input-inline">
					<input id="salePrice" type="text" lay-filter="salePrice" lay-affix="number" min="0" lay-precision="2" class="layui-input" lay-verify="salePrice" value="${detail.specs[0].salePrice}" placeholder="请输入销售价格">
				</div>
			</div>
		</div>
	</div>
	<div id="resModelType2" style="display: none;">
		<table class="layui-hide" id="specTable"></table>
		<div class="layui-inline" style="margin-bottom: 10px;">
			<a class="layui-btn layui-btn-normal" id="resAddSpec_btn"> <i class="layui-icon  layui-icon-add-circle"></i>新增规格</a>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">商品详情</label>
			<div class="layui-input-block" style="margin-left: 150px;">
				<textarea type="text" id="itemDetail" name="itemDetail" class="layui-textarea" maxlength="100" style="resize:none;min-height:80px;width: 93%;" placeholder="请输入详细介绍"></textarea>
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">快递费</label>
			<div class="layui-input-inline">
				<input id="deliveryFee" name="deliveryFee" type="text" lay-affix="number" min="0" lay-precision="2" class="layui-input" lay-verify="required|number" value="${detail.item.deliveryFee}" placeholder="请输入快递费">
			</div>
			<label class="layui-form-label">快递费门槛</label>
			<div class="layui-input-inline">
				<input id="deliveryThreshold" name="deliveryThreshold" type="text" lay-affix="number" min="0" lay-precision="2" class="layui-input" lay-verify="required|number" value="${detail.item.deliveryThreshold}" placeholder="请输入快递费门槛">
			</div>
		</div>
		<div class="layui-inline">
			<span>单一订单中，某种商品价格累计达到快递费门槛，即免运费</span>
		</div>
	</div>
    <div class="layui-form-item" style="text-align: center;margin-top: 18px;height:138px;">
		<div class="layui-inline">
			<c:if test="${pageFlag == 'add' || pageFlag == 'upd'}">
				<button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="saveRes">保存为草稿</button>
			</c:if>
			<c:if test="${pageFlag == 'detail'}">
				<button class="layui-btn layui-btn-disabled" lay-submit="" lay-filter="saveRes">保存为草稿</button>
			</c:if>
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
	tinymce = layui.tinymce,
	layer = parent.layer === undefined ? layui.layer : parent.layer;

	function resInit() {
		var pageFlag = $("#pageFlag").val();
		switch (pageFlag) {
			case "add":
				queryCategory(1, 0);
				break;
			case "upd":
			case "detail":
				var itemCategory1 = '${detail.item.itemCategory1}',
					itemCategory2 = '${detail.item.itemCategory2}',
					itemCategory3 = '${detail.item.itemCategory3}';
				queryCategory(1, 0);
				$("#itemCategory1 option[value='" + itemCategory1 + "']").prop("selected","selected");
				queryCategory(2, itemCategory1);
				$("#itemCategory2 option[value='" + itemCategory2 + "']").prop("selected","selected");
				$("#itemCategory3 option[value='" + itemCategory3 + "']").prop("selected","selected");
				break;
			default:
				break;
		}

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

		var specs = '${detail.item.specs}';
		if (specs) {
			specs = JSON.parse('${detail.item.specs}');
		} else {
			specs = [];
		}
		table.render({
			elem: '#specTable',
			data: specs,
			cols: [[
				{field: 'specName', title: '规格名称', width: '15%', align:'center'},
				{field: 'picUrl', title: '规格图片', width: '20%', align:'center',templet: '#resImageTpl'},
				{field: 'supplyPrice', title: '供货价格', width: '15%', align:'center'},
				{field: 'salePrice', title: '销售价格', width: '15%', align:'center'},
				{fixed:'right', title: '操作', width: '10%', align:'center',toolbar: '#resBar'}
			]],
		});

		tinymce.render({
			height: 500,
			elem: '#itemDetail', // 绑定指定的textarea
			setup: function(editor) {
				editor.on('init', function() {
					this.setContent('${detail.item.itemDetail}');
				});
			},
			// 图片
			images_upload_url: '${ctx}/item/upload.do',
			form:{
				name: 'file',
				data: {'path': '${ctx}'}
			}
		});
	}

	/*初始化*/
	resInit();

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
				$('#itemImage').attr('src', '${ctx}' + res.data);
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
					uploadStep: 1,
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
				pic.uploadStep = 2;
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
								"src": '${ctx}' + obj.data.picUrl,
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

	// 查询分类
	function queryCategory(level, pid) {
		$.ajax({
			type: "POST",
			url: "${ctx}/category/list.do",
			dataType: "json",
			data: {pId: pid},
			success: function (data) {
				var id = "#itemCategory"+level;
				$(id).empty();
				$(id).append('<option selected="selected" value="">请选择'+level+'级分类</option>');
				switch (level) {
					case 1:
						$('#itemCategory2 option').not(":first").remove();
						$('#itemCategory3 option').not(":first").remove();
						break;
					case 2:
						$('#itemCategory3 option').not(":first").remove();
						break;
					case 3:
						break;
					default:
						break;
				}
				$(data.data).each(function(index, item) {
					$(id).append(
						'<option value="'+item.id+'">'+item.categoryName+'</option>'
					);
				});
				form.render('select');
			}
		});
	}
	// 改变1级分组时，刷新2/3级分组
	// 改变2级分组时，刷新3级分组
	form.on('select(categoryFilter)', function(data){
		if (!data.value) {
			return ;
		}
		switch (data.elem.id) {
			case "itemCategory1":
				queryCategory(2, data.value);
				break;
			case "itemCategory2":
				queryCategory(3, data.value);
				break;
			default:
				break;
		}
	});

	// 改变型号规格类型时
	form.on('radio(modelTypeFilter)', function(data) {
		changeModelType(data.value)
	});

	table.on('tool(specTable)', function(obj) {
		var data = obj.data;
		var layEvent = obj.event;
		switch (layEvent) {
			case 'res_del':
				obj.del();
				break;
			case 'res_up':
				obj.specStatus = 1;
				break;
			case 'res_down':
				obj.specStatus = 2;
				break;
			default:
				break;
		}
	});
	function changeModelType(modelType) {
		$('input[name="modelType"][value="' + modelType + '"]').prop('checked', true);
		switch (modelType) {
			case '1':
				$("#resModelType2").hide();
				$("#resModelType1").show();
				break;
			case '2':
				$("#resModelType1").hide();
				$("#resModelType2").show();
				break;
			default:
				break;
		}
		form.render('radio');
	}
	$("#resAddSpec_btn").click(function() {
		var url = "${ctx}/item/spec";
		layui.layer.open({
			title : '<i class="layui-icon layui-icon-layer"></i>'+"新增规格",
			type : 2,
			skin : 'layui-layer-molv',
			offset: '50px',
			content : url,
			area: ['400px','500px'],
			resize:true,
			anim:1,
			btn: ['确定', '取消'],
			btnAlign: 'c',
			yes: function(index, layero) {
				var sub =  window[layero.find('iframe')[0]['name']].layui;
				var addSpecSpecName = sub.$('#addSpecSpecName').val();
				// 验证规格名称
				if (!new RegExp("^[0-9a-zA-Z\u4e00-\u9fa5\-_]+$").test(addSpecSpecName)) {
					common.cmsLayErrorMsg('规格名称只能为中文、数字、字母或-_');
					return ;
				}
				var addSpecSalePrice = sub.$('#addSpecSalePrice').val();
				var addSpecSupplyPrice = sub.$('#addSpecSupplyPrice').val();
				var checkResult = checkPrice(addSpecSalePrice, addSpecSupplyPrice);
				if (checkResult) {
					common.cmsLayErrorMsg(checkResult);
					return ;
				}
				var addSpecPicUrl = sub.$('#itemImage').attr('data-url');
				var specCache = table.cache['specTable'];
				specCache.push({
					specName: addSpecSpecName,
					salePrice: addSpecSalePrice,
					supplyPrice: addSpecSupplyPrice,
					picUrl: addSpecPicUrl,
					specStatus: 1,
				});
				table.renderData('specTable');
				layui.layer.close(index); // 关闭弹层
			}
		});
	});

	function checkPrice(salePrice, supplyPrice) {
		var costRatio = '${LOGIN_USER.costRatio}' || 1.25;	// 默认1.25
		var minPrice = Math.round(supplyPrice * costRatio * 100)/100;
		if (minPrice > salePrice) {
			return '销售价格过低，至少达到'+minPrice+'元，或者调低供货价格';
		}
	}

	$('#supplyPrice').on('input', function(e) {
		return form.validate('#supplyPrice');
	});
	$('#salePrice').on('input', function(e) {
		return form.validate('#salePrice');
	});
	form.on('input-affix(supplyPrice)', function(data){
		return form.validate('#supplyPrice');
	});
	form.on('input-affix(salePrice)', function(data){
		return form.validate('#salePrice');
	});

	/**表单验证*/
	form.verify({
		itemName: function(value, item) {
			// 验证商品名称
			if (!new RegExp("^[0-9a-zA-Z\u4e00-\u9fa5\-_]+$").test(value)) {
				return '商品名称只能为中文、数字、字母或-_';
			}
		},
		itemCode: function(value, item) {
			// 验证商品编码
			if (!new RegExp("^[0-9a-zA-Z\u4e00-\u9fa5\-_]+$").test(value)) {
				return '商品编码只能为中文、数字、字母或-_';
			}
		},
		brandName: function(value, item) {
			// 验证品牌名称
			if (!new RegExp("^[0-9a-zA-Z\u4e00-\u9fa5\-_]+$").test(value)) {
				return '品牌名称只能为中文、数字、字母或-_';
			}
		},
		specName: function(value, item) {
			// 验证规格名称
			if ($('input[name="modelType"]:checked').val() === '1') {
				if (!new RegExp("^[0-9a-zA-Z\u4e00-\u9fa5\-_]+$").test(value)) {
					return '规格名称只能为中文、数字、字母或-_';
				}
			}
		},
		salePrice: function(value, item) {
			// 验证价格
			if ($('input[name="modelType"]:checked').val() === '1') {
				return checkPrice(value, $('#supplyPrice').val());
			}
		},
		supplyPrice: function(value, item) {
			// 验证价格
			if ($('input[name="modelType"]:checked').val() === '1') {
				return checkPrice($('#salePrice').val(), value);
			}
		},

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
			specs: specs,
			pics: table.cache['picTable'],
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
			{{# if(d.uploadStep == 1){ }}
			<img src="{{= d.picUrl}}"  style="width: 120px; height: 120px;">
			{{# } else { }}
			<img src="${ctx}{{= d.picUrl}}"  style="width: 120px; height: 120px;">
			{{# } }}
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