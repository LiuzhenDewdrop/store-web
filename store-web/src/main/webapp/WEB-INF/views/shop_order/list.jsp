<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/personal.css" media="all">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">

	<script src="${ctx}/static/layui/layui.js"></script>
	<style type="text/css">
		.layui-table tr { height: 60px; }
		.order-goods { font-size: 12px; line-height: 18px; text-align: left; }
		.order-goods .goods-name { color: #333; }
		.order-goods .goods-count { color: #999; }
	</style>
</head>
<body>
<div class="larry-grid layui-anim layui-anim-upbit larryTheme-A">
	<div class="larry-personal">
		<div class="layui-tab">
			<blockquote class="layui-elem-quote mylog-info-tit">
				<div class="layui-inline">
					<form class="layui-form" id="cusSearchForm">
						<div class="layui-form-item" style="margin-bottom:3px;">
							<label class="layui-form-label">订单号</label>
							<div class="layui-input-inline" style="width:140px;">
								<input type="text" name="orderNo" value="" placeholder="请输入完整订单号" class="layui-input search_input">
							</div>
							<label class="layui-form-label">订单状态</label>
							<div class="layui-input-inline" style="width:140px;">
								<select name="orderStatus">
									<option selected="selected" value=""></option>
									<option value="0">待付款</option>
									<option value="10">待发货</option>
									<option value="20">待收货</option>
									<option value="31">正常完成</option>
									<option value="32">用户取消</option>
									<option value="33">超时关闭</option>
									<option value="34">全额退款关闭</option>
									<option value="35">平台关闭</option>
								</select>
							</div>
							<label class="layui-form-label">支付状态</label>
							<div class="layui-input-inline" style="width:140px;">
								<select name="payStatus">
									<option selected="selected" value=""></option>
									<option value="0">未支付</option>
									<option value="1">已支付</option>
									<option value="2">部分退款</option>
									<option value="3">全额退款</option>
								</select>
							</div>
						</div>
						<div class="layui-form-item" style="margin-bottom:3px;">
							<label class="layui-form-label">下单人</label>
							<div class="layui-input-inline" style="width:140px;">
								<input type="text" name="customerNickname" value="" placeholder="请输入下单人昵称" class="layui-input search_input">
							</div>
							<label class="layui-form-label">收货人</label>
							<div class="layui-input-inline" style="width:140px;">
								<input type="text" name="receiverName" value="" placeholder="姓名/手机号" class="layui-input search_input">
							</div>
							<label class="layui-form-label">下单时间</label>
							<div class="layui-input-inline" style="width:140px;">
								<input type="text" name="createTimeStart" id="createTimeStart" placeholder="开始时间" class="layui-input">
							</div>
							<div class="layui-input-inline" style="width:140px;">
								<input type="text" name="createTimeEnd" id="createTimeEnd" placeholder="结束时间" class="layui-input">
							</div>
							<shiro:hasPermission name="shop:order:list">
								<a class="layui-btn layui-btn-normal searchList_btn" lay-submit lay-filter="searchFilter"><i class="layui-icon layui-icon-search"></i>查询</a>
								<button type="reset" class="layui-btn layui-btn-primary" id="resetBtn"><i class="layui-icon layui-icon-refresh"></i>重置</button>
							</shiro:hasPermission>
						</div>
					</form>
				</div>
			</blockquote>
			<div class="larry-separate"></div>
			<!-- 订单列表 -->
			<div class="layui-tab-item layui-show" style="padding: 10px 15px;">
				<table class="resTableList" id="resTableList" lay-filter="resTableId"></table>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	layui.config({
		base: "${ctx}/static/js/"
	}).use(['form', 'table', 'layer', 'laydate', 'commCms', 'bodyTab'], function () {
		var $ = layui.$,
			form = layui.form,
			table = layui.table,
			layer = layui.layer,
			laydate = layui.laydate,
			common = layui.commCms;

		// 日期范围
		laydate.render({ elem: '#createTimeStart', type: 'date' });
		laydate.render({ elem: '#createTimeEnd', type: 'date' });

		function searchList(data) {
			var field = data && data.field;
			table.render({
				elem: '#resTableList',
				url: '${ctx}/order/list.do',
				id: 'resTableId',
				method: 'post',
				loading: true,
				skin: 'row',
				even: 'true',
				size: 'lg',
				where: field,
				cols: [[
					{ field: 'orderNo', title: '订单号', width: '14%', align: 'center' },
					{ field: 'customerNickname', title: '下单人', width: '10%', align: 'center', templet: '#customerTpl' },
					{ field: 'goods', title: '商品', width: '22%', align: 'center', templet: '#goodsTpl' },
					{ field: 'receiverName', title: '收货人', width: '10%', align: 'center', templet: '#receiverTpl' },
					{ field: 'paidAmt', title: '实付金额', width: '8%', align: 'center', templet: '#amtTpl' },
					{ field: 'orderStatus', title: '订单状态', width: '9%', align: 'center', templet: '#orderStatusTpl' },
					{ field: 'payStatus', title: '支付状态', width: '8%', align: 'center', templet: '#payStatusTpl' },
					{ field: 'afterSaleStatus', title: '售后状态', width: '8%', align: 'center', templet: '#afterSaleStatusTpl' },
					{ field: 'createTime', title: '下单时间', width: 170, align: 'center', templet: '#createTimeTpl' },
					{ fixed: 'right', title: '操作', width: '14%', align: 'center', toolbar: '#resBar' }
				]],
				page: true,
				limit: 20
			});
		}

		<shiro:hasPermission name="shop:order:list">
		searchList();
		</shiro:hasPermission>

		/**查询*/
		$(".searchList_btn").click(function () {
			form.on('submit(searchFilter)', function (data) {
				searchList(data);
			});
		});

		/**清空查询条件*/
		$("#resetBtn").click(function () {
			// 1. 清空表单数据
			$('#cusSearchForm')[0].reset();

			// 2. 清空 layui 的下拉框和日期选择器（必须重新渲染）
			form.render('select');
			form.render('date'); // 如果用了 laydate，直接把 input 的 value 置空也可以

			// 3. 把时间控件也清空（laydate 绑定的可能有些残留）
			$('#createTimeStart').val('');
			$('#createTimeEnd').val('');

			// 4. 重新查询列表（不带任何条件）
			searchList();
		});

		/**监听工具条*/
		table.on('tool(resTableId)', function (obj) {
			var data = obj.data;
			var layEvent = obj.event;
			var url;
			switch (layEvent) {
				case 'res_detail':
					top.addTab($(this));
					break;
				case 'res_ship':
					openShip(data);
					break;
				case 'res_refund':
					openRefund(data);
					break;
				case 'res_close':
					url = "${ctx}/order/close.do";
					common.ajaxCmsConfirm('系统提示', '确定关闭该订单?', url, { id: data.id }, {
						reload: function () {
							table.reload('resTableId', { page: { curr: 1 } });
						}
					});
					break;
				default:
					break;
			}
		});

		/**发货弹窗*/
		function openShip(data) {
			layer.open({
				type: 1,
				title: '订单发货',
				area: ['480px', '320px'],
				content: $('#shipDialog').html(),
				btn: ['确定', '取消'],
				yes: function (index, layero) {
					var expressCompanyName = layero.find('input[name="expressCompanyName"]').val();
					var expressNo = layero.find('input[name="expressNo"]').val();
					if (!expressCompanyName) { layer.msg('请输入物流公司'); return; }
					if (!expressNo) { layer.msg('请输入运单号'); return; }
					$.post('${ctx}/order/ship.do', {
						orderId: data.id,
						expressCompanyName: expressCompanyName,
						expressNo: expressNo
					}, function (res) {
						if (res.code === '0000') {
							layer.msg('发货成功', { icon: 1 });
							layer.close(index);
							table.reload('resTableId', { page: { curr: 1 } });
						} else {
							layer.msg(res.msg || '发货失败');
						}
					});
				}
			});
		}

		/**退款弹窗*/
		function openRefund(data) {
			layer.open({
				type: 1,
				title: '订单退款',
				area: ['480px', '340px'],
				content: $('#refundDialog').html(),
				success: function (layero) {
					layero.find('input[name="refundAmt"]').val(((data.paidAmt - (data.refundAmt || 0)) / 100).toFixed(2));
				},
				btn: ['确定', '取消'],
				yes: function (index, layero) {
					var refundAmt = layero.find('input[name="refundAmt"]').val();
					var reason = layero.find('textarea[name="reason"]').val();
					if (!refundAmt || isNaN(refundAmt) || Number(refundAmt) <= 0) {
						layer.msg('请输入正确的退款金额'); return;
					}
					$.post('${ctx}/order/refund.do', {
						orderId: data.id,
						refundAmt: Math.round(Number(refundAmt) * 100),
						reason: reason
					}, function (res) {
						if (res.code === '0000') {
							layer.msg('退款已提交', { icon: 1 });
							layer.close(index);
							table.reload('resTableId', { page: { curr: 1 } });
						} else {
							layer.msg(res.msg || '退款失败');
						}
					});
				}
			});
		}
	});
</script>

<!-- 下单人 tpl -->
<!-- 下单人 tpl -->
<script type="text/html" id="customerTpl">
	<div style="line-height: 20px;">
		<span style="font-weight: bold; color: #333;">{{= d.customerNickname || '未设置昵称' }}</span>
		<br>
		<span style="font-size: 12px; color: #999;">ID: {{= d.customerId }}</span>
	</div>
</script>

<!-- 商品 tpl -->
<script type="text/html" id="goodsTpl">
	<div class="order-goods" style="text-align: left;">
		{{# if(d.itemList && d.itemList.length > 0){ }}
		{{# layui.each(d.itemList.slice(0, 2), function(i, item){ }}
		<div class="goods-name" style="font-weight: bold;">{{= item.itemName }}</div>
		<div class="goods-count" style="font-size: 11px; color: #999; line-height: 14px;">
			{{= item.specName || '默认规格' }} | ￥{{= ((item.salePrice || 0) / 100).toFixed(2) }} × {{= item.quantity }}
		</div>
		{{# }); }}

		{{# if(d.itemList.length > 2){ }}
		<div class="goods-count" style="margin-top: 2px; color: #666;">
			... 等 {{= d.itemList.length }} 件商品
		</div>
		{{# } }}
		{{# } else { }}
		-
		{{# } }}
	</div>
</script>

<!-- 收货人 tpl -->
<script type="text/html" id="receiverTpl">
	<div>{{= d.receiverName || '' }}</div>
	<div style="color:#999;">{{= d.receiverMobile || '' }}</div>
</script>

<!-- 金额 tpl（分转元） -->
<script type="text/html" id="amtTpl">
	￥{{= ((d.paidAmt || 0) / 100).toFixed(2) }}
</script>

<!-- 订单状态 tpl -->
<script type="text/html" id="orderStatusTpl">
	{{# if(d.orderStatus == 0){ }}
	<span class="label label-warning">待付款</span>
	{{# } else if(d.orderStatus == 10){ }}
	<span class="label label-info">待发货</span>
	{{# } else if(d.orderStatus == 20){ }}
	<span class="label label-primary">待收货</span>
	{{# } else if(d.orderStatus == 31){ }}
	<span class="label label-success">正常完成</span>
	{{# } else if(d.orderStatus == 32){ }}
	<span class="label label-default">用户取消</span>
	{{# } else if(d.orderStatus == 33){ }}
	<span class="label label-default">超时关闭</span>
	{{# } else if(d.orderStatus == 34){ }}
	<span class="label label-danger">全额退款关闭</span>
	{{# } else if(d.orderStatus == 35){ }}
	<span class="label label-default">平台关闭</span>
	{{# } }}
</script>

<!-- 支付状态 tpl -->
<script type="text/html" id="payStatusTpl">
	{{# if(d.payStatus == 0){ }}
	<span class="label label-default">未支付</span>
	{{# } else if(d.payStatus == 1){ }}
	<span class="label label-success">已支付</span>
	{{# } else if(d.payStatus == 2){ }}
	<span class="label label-warning">部分退款</span>
	{{# } else if(d.payStatus == 3){ }}
	<span class="label label-danger">全额退款</span>
	{{# } }}
</script>

<!-- 售后状态 tpl -->
<script type="text/html" id="afterSaleStatusTpl">
	{{# if(d.afterSaleStatus == 0){ }}
	<span class="label label-default">无</span>
	{{# } else if(d.afterSaleStatus == 1){ }}
	<span class="label label-warning">申请中</span>
	{{# } else if(d.afterSaleStatus == 2){ }}
	<span class="label label-info">退款中</span>
	{{# } else if(d.afterSaleStatus == 3){ }}
	<span class="label label-success">退款完成</span>
	{{# } else if(d.afterSaleStatus == 4){ }}
	<span class="label label-default">售后关闭</span>
	{{# } }}
</script>

<!-- 时间 tpl -->
<script type="text/html" id="createTimeTpl">
	{{layui.util.toDateString(d.createTime, 'yyyy-MM-dd HH:mm:ss')}}
</script>

<!-- 工具条 -->
<script type="text/html" id="resBar">
	<div class="layui-btn-group">
		<shiro:hasPermission name="shop:order:view">
			<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="res_detail" href="javascript:;" data-url="/store-web/order/detail?id={{= d.id }}"><i class="layui-icon layui-icon-form" data-icon="layui-icon-form"></i><cite>详情</cite></a>
		</shiro:hasPermission>
		<shiro:hasPermission name="shop:order:ship">
			{{# if(d.orderStatus == 10){ }}
			<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="res_ship"><i class="layui-icon layui-icon-release"></i>发货</a>
			{{# } }}
		</shiro:hasPermission>
		<shiro:hasPermission name="shop:order:refund">
			{{# if((d.afterSaleStatus == 1 || d.afterSaleStatus == 2) && (d.payStatus == 1 || d.payStatus == 2)){ }}
			<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="res_refund">
				<i class="layui-icon layui-icon-rmb"></i>处理售后
			</a>
			{{# } }}
		</shiro:hasPermission>
		<shiro:hasPermission name="shop:order:close">
			{{# if(d.orderStatus == 10){ }}
			<a class="layui-btn layui-btn-xs layui-btn-primary" lay-event="res_close"><i class="layui-icon layui-icon-close"></i>关闭</a>
			{{# } }}
		</shiro:hasPermission>
	</div>
</script>

<!-- 发货弹窗模板 -->
<script type="text/html" id="shipDialog">
	<form class="layui-form" style="padding: 20px;">
		<div class="layui-form-item">
			<label class="layui-form-label">物流公司</label>
			<div class="layui-input-block">
				<input type="text" name="expressCompanyName" class="layui-input" placeholder="如：顺丰速运">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">运单号</label>
			<div class="layui-input-block">
				<input type="text" name="expressNo" class="layui-input" placeholder="请输入运单号">
			</div>
		</div>
	</form>
</script>

<!-- 退款弹窗模板 -->
<script type="text/html" id="refundDialog">
	<form class="layui-form" style="padding: 20px;">
		<div class="layui-form-item">
			<label class="layui-form-label">退款金额(元)</label>
			<div class="layui-input-block">
				<input type="text" name="refundAmt" class="layui-input" placeholder="请输入退款金额">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">退款原因</label>
			<div class="layui-input-block">
				<textarea name="reason" class="layui-textarea" placeholder="请输入退款原因"></textarea>
			</div>
		</div>
	</form>
</script>

</body>
</html>