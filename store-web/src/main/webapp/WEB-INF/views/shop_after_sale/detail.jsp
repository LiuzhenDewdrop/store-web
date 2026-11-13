<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>售后详情</title>
	<link rel="shortcut icon" href="${ctx}/static/img/5r7_logo.png">
	<link rel="stylesheet" href="${ctx}/static/layui/css/layui.css">
	<link rel="stylesheet" href="${ctx}/static/css/global.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" media="all">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">
	<script src="${ctx}/static/layui/layui.js"></script>
	<style>
		.layui-card { margin-bottom: 10px; }
		.layui-table td, .layui-table th { font-size: 13px; }
	</style>
</head>
<c:if test="${not empty errorMsg}">
	<div class="layui-card">
		<div class="layui-card-body" style="color:#FF5722;font-size:14px;">
				${errorMsg}
		</div>
	</div>
	<script type="text/javascript">
		// 有错误就不渲染下面区块
		document.addEventListener('DOMContentLoaded', function () {
			var cards = document.querySelectorAll('.layui-card');
			for (var i = 1; i < cards.length; i++) {
				cards[i].style.display = 'none';
			}
		});
	</script>
</c:if>
<body class="childrenBody" style="font-size: 13px; margin: 10px;">

<!-- 售后单信息 -->
<div class="layui-card">
	<div class="layui-card-header">售后单信息</div>
	<div class="layui-card-body">
		<table class="layui-table">
			<tr>
				<td width="12%">售后单号</td><td width="38%">${afterSale.afterSaleNo}</td>
				<td width="12%">订单号</td><td width="38%">${afterSale.orderNo}</td>
			</tr>
			<tr>
				<td>类型</td><td id="typeTd"></td>
				<td>售后状态</td><td id="statusTd"></td>
			</tr>
			<tr>
				<td>申请金额</td><td id="applyAmtTd"></td>
				<td>实退金额</td><td id="refundAmtTd"></td>
			</tr>
			<tr>
				<td>申请原因</td><td colspan="3">${afterSale.applyReason}</td>
			</tr>
			<tr>
				<td>凭证图片</td><td colspan="3" id="imgTd"></td>
			</tr>
			<tr>
				<td>申请时间</td><td id="createTimeTd"></td>
				<td>退款时间</td><td id="refundTimeTd"></td>
			</tr>
		</table>
	</div>
</div>

<!-- 订单信息 -->
<div class="layui-card">
	<div class="layui-card-header">订单信息</div>
	<div class="layui-card-body">
		<table class="layui-table">
			<tr>
				<td width="12%">订单号</td><td width="38%">${order.orderNo}</td>
				<td width="12%">店铺</td><td width="38%">${order.shopName}</td>
			</tr>
			<tr>
				<td>收货人</td><td>${order.receiverName} / ${order.receiverMobile}</td>
				<td>收货地址</td><td>${order.receiverAddress}</td>
			</tr>
			<tr>
				<td>实付金额</td><td id="paidAmtTd"></td>
				<td>已退金额</td><td id="orderRefundAmtTd"></td>
			</tr>
			<tr>
				<td>订单状态</td><td id="orderStatusTd"></td>
				<td>支付状态</td><td id="payStatusTd"></td>
			</tr>
		</table>
	</div>
</div>

<!-- 商品明细 -->
<div class="layui-card">
	<div class="layui-card-header">商品明细</div>
	<div class="layui-card-body">
		<table class="layui-table" id="itemTable">
			<thead>
			<tr>
				<th>商品</th><th>规格</th><th>单价</th><th>数量</th><th>行金额</th><th>已退</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${itemList}" var="item">
				<tr>
					<td>${item.itemName}</td>
					<td>${item.specName}</td>
					<td>￥<fmt:formatNumber value="${item.salePrice / 100}" pattern="0.00"/></td>
					<td>${item.quantity}</td>
					<td>￥<fmt:formatNumber value="${item.totalAmt / 100}" pattern="0.00"/></td>
					<td>￥<fmt:formatNumber value="${item.refundAmt / 100}" pattern="0.00"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<!-- 状态流转日志 -->
<div class="layui-card">
	<div class="layui-card-header">状态流转日志</div>
	<div class="layui-card-body">
		<table class="layui-table">
			<thead>
			<tr><th>动作</th><th>状态变更</th><th>操作人</th><th>来源</th><th>备注</th><th>时间</th></tr>
			</thead>
			<tbody>
			<c:forEach items="${logList}" var="log">
				<tr>
					<td>${log.action}</td>
					<td>
						<c:if test="${not empty log.oldOrderStatus}">${log.oldOrderStatus}→</c:if>${log.newOrderStatus}
						<c:if test="${not empty log.newAfterSaleStatus}">（售后：${log.newAfterSaleStatus}）</c:if>
					</td>
					<td>${log.operatorType}</td>
					<td>${log.source}</td>
					<td>${log.remark}</td>
					<td><fmt:formatDate value="${log.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<script type="text/javascript">
	layui.config({ base: "${ctx}/static/js/" }).use(['form', 'layer', 'jquery'], function () {
		var $ = layui.$, layer = layui.layer;

		var afterSale = {
			afterSaleType: '${afterSale.afterSaleType}',
			applyAmt: ${afterSale.applyAmt == null ? 0 : afterSale.applyAmt},
			refundAmt: ${afterSale.refundAmt == null ? 0 : afterSale.refundAmt},
			status: '${afterSale.status}',
			createTime: '${afterSale.createTime}',
			refundTime: '${afterSale.refundTime}',
			applyImages: '${afterSale.applyImages}'
		};
		var order = {
			paidAmt: ${order.paidAmt == null ? 0 : order.paidAmt},
			refundAmt: ${order.refundAmt == null ? 0 : order.refundAmt},
			orderStatus: '${order.orderStatus}',
			payStatus: '${order.payStatus}'
		};

		$('#typeTd').text(afterSale.afterSaleType == '1' ? '仅退款' : '退货退款');
		$('#statusTd').text(afterSale.status == '1' ? '申请中' : afterSale.status == '2' ? '退款中' : afterSale.status == '3' ? '退款完成' : '已关闭');
		$('#applyAmtTd').text('￥' + (afterSale.applyAmt / 100).toFixed(2));
		$('#refundAmtTd').text('￥' + (afterSale.refundAmt / 100).toFixed(2));
		$('#createTimeTd').text(afterSale.createTime || '');
		$('#refundTimeTd').text(afterSale.refundTime || '-');

		$('#paidAmtTd').text('￥' + (order.paidAmt / 100).toFixed(2));
		$('#orderRefundAmtTd').text('￥' + (order.refundAmt / 100).toFixed(2));
		$('#orderStatusTd').text(orderStatusText(order.orderStatus));
		$('#payStatusTd').text(payStatusText(order.payStatus));

		if (afterSale.applyImages) {
			try {
				var imgs = JSON.parse(afterSale.applyImages);
				var html = '';
				for (var i = 0; i < imgs.length; i++) {
					html += '<img src="' + imgs[i] + '" style="width:80px;height:80px;margin-right:8px;">';
				}
				$('#imgTd').html(html);
			} catch (e) {}
		}

		function orderStatusText(s) {
			return s == '0' ? '待付款' : s == '10' ? '待发货' : s == '20' ? '待收货' : s == '31' ? '正常完成'
				: s == '32' ? '用户取消' : s == '33' ? '超时关闭' : s == '34' ? '全额退款关闭' : s;
		}
		function payStatusText(s) {
			return s == '0' ? '未支付' : s == '1' ? '已支付' : s == '2' ? '部分退款' : s == '3' ? '全额退款' : s;
		}
	});
</script>
</body>
</html>