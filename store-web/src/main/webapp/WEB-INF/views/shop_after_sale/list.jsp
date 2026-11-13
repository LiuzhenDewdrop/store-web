<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>乐活益家运营平台</title>
	<link rel="shortcut icon" href="${ctx}/static/img/5r7_logo.png">
	<link rel="stylesheet" href="${ctx}/static/layui/css/layui.css">
	<link rel="stylesheet" href="${ctx}/static/css/global.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" media="all">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/personal.css" media="all">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">
	<script src="${ctx}/static/layui/layui.js"></script>
</head>
<body>
<div class="larry-grid layui-anim layui-anim-upbit larryTheme-A">
	<div class="larry-personal">
		<div class="layui-tab">
			<blockquote class="layui-elem-quote mylog-info-tit">
				<div class="layui-inline">
					<form class="layui-form" id="cusSearchForm">
						<div class="layui-form-item" style="margin-bottom:3px;">
							<label class="layui-form-label">售后单号</label>
							<div class="layui-input-inline" style="width:160px;">
								<input type="text" name="afterSaleNo" placeholder="请输入售后单号" class="layui-input">
							</div>
							<label class="layui-form-label">订单号</label>
							<div class="layui-input-inline" style="width:160px;">
								<input type="text" name="orderNo" placeholder="请输入订单号" class="layui-input">
							</div>
							<label class="layui-form-label">售后状态</label>
							<div class="layui-input-inline" style="width:140px;">
								<select name="status">
									<option selected="selected" value=""></option>
									<option value="1">申请中</option>
									<option value="2">退款中</option>
									<option value="3">退款完成</option>
									<option value="4">已关闭</option>
								</select>
							</div>
							<shiro:hasPermission name="shop:afterSale:list">
								<a class="layui-btn layui-btn-normal searchList_btn" lay-submit lay-filter="searchFilter"><i class="layui-icon layui-icon-search"></i>查询</a>
							</shiro:hasPermission>
						</div>
					</form>
				</div>
			</blockquote>
			<div class="larry-separate"></div>
			<div class="layui-tab-item layui-show" style="padding: 10px 15px;">
				<table class="resTableList" id="resTableList" lay-filter="resTableId"></table>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	layui.config({ base: "${ctx}/static/js/" })
		.use(['form', 'table', 'layer', 'commCms', 'bodyTab'], function () {
			var $ = layui.$, form = layui.form, table = layui.table;

			function searchList(data) {
				table.render({
					elem: '#resTableList',
					url: '${ctx}/afterSale/list.do',
					id: 'resTableId',
					method: 'post',
					loading: true,
					skin: 'row',
					even: 'true',
					size: 'lg',
					where: data && data.field,
					cols: [[
						{ field: 'afterSaleNo', title: '售后单号', width: '13%', align: 'center' },
						{ field: 'orderNo', title: '订单号', width: '13%', align: 'center' },
						{ field: 'customerNickname', title: '申请人', width: '10%', align: 'center' },
						{ field: 'afterSaleType', title: '类型', width: '7%', align: 'center', templet: '#typeTpl' },
						{ field: 'applyAmt', title: '申请金额', width: '9%', align: 'center', templet: '#amtTpl' },
						{ field: 'refundAmt', title: '实退金额', width: '9%', align: 'center', templet: '#refundAmtTpl' },
						{ field: 'applyReason', title: '申请原因', width: '13%', align: 'center' },
						{ field: 'status', title: '售后状态', width: '9%', align: 'center', templet: '#statusTpl' },
						{ field: 'createTime', title: '申请时间', width: '12%', align: 'center', templet: '#timeTpl' },
						{ fixed: 'right', title: '操作', width: '8%', align: 'center', toolbar: '#resBar' }
					]],
					page: true,
					limit: 20
				});
			}

			<shiro:hasPermission name="shop:afterSale:list">
			searchList();
			</shiro:hasPermission>

			$(".searchList_btn").click(function () {
				form.on('submit(searchFilter)', function (data) { searchList(data); });
			});

			table.on('tool(resTableId)', function (obj) {
				var data = obj.data;
				if (obj.event === 'res_detail') {
					top.addTab($(this).attr('data-url', '/store-web/afterSale/detail?id=' + data.id));
				}
			});
		});
</script>

<script type="text/html" id="typeTpl">
	{{# if(d.afterSaleType == 1){ }}<span class="label label-info">仅退款</span>
	{{# } else if(d.afterSaleType == 2){ }}<span class="label label-warning">退货退款</span>{{# } }}
</script>
<script type="text/html" id="amtTpl">
	￥{{= ((d.applyAmt || 0) / 100).toFixed(2) }}
</script>
<script type="text/html" id="refundAmtTpl">
	￥{{= ((d.refundAmt || 0) / 100).toFixed(2) }}
</script>
<script type="text/html" id="statusTpl">
	{{# if(d.status == 1){ }}<span class="label label-warning">申请中</span>
	{{# } else if(d.status == 2){ }}<span class="label label-info">退款中</span>
	{{# } else if(d.status == 3){ }}<span class="label label-success">退款完成</span>
	{{# } else if(d.status == 4){ }}<span class="label label-default">已关闭</span>{{# } }}
</script>
<script type="text/html" id="timeTpl">
	{{layui.util.toDateString(d.createTime, 'yyyy-MM-dd HH:mm:ss')}}
</script>
<script type="text/html" id="resBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="shop:afterSale:view">
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_detail"
			   href="javascript:;" data-url="/store-web/afterSale/detail?id={{= d.id }}">
				<i class="layui-icon layui-icon-form"></i><cite>详情</cite>
			</a>
		</shiro:hasPermission>
	</div>
</script>
</body>
</html>