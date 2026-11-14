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
		.layui-table .label-cell { padding: 9px 15px; background-color: #fafafa; width: 12%; }
		.imgs-wrap img { width: 80px; height: 80px; margin-right: 8px; border: 1px solid #eee; }
		.empty-tip { color: #999; padding: 10px 0; }
	</style>
</head>
<body class="childrenBody" style="font-size: 13px; margin: 10px;">

<!-- 错误提示 -->
<c:if test="${not empty errorMsg}">
	<div class="layui-card">
		<div class="layui-card-body" style="color:#FF5722;font-size:14px;">
				${errorMsg}
		</div>
	</div>
</c:if>

<c:if test="${empty errorMsg}">

	<!-- ==================== 售后单信息 ==================== -->
	<div class="layui-card">
		<div class="layui-card-header">售后单信息</div>
		<div class="layui-card-body">
			<table class="layui-table">
				<tr>
					<td class="label-cell">售后单号</td><td width="38%">${afterSale.afterSaleNo}</td>
					<td class="label-cell">订单号</td><td width="38%">${afterSale.orderNo}</td>
				</tr>
				<tr>
					<td class="label-cell">售后类型</td>
					<td id="typeTd">-</td>
					<td class="label-cell">售后状态</td>
					<td id="statusTd">-</td>
				</tr>
				<tr>
					<td class="label-cell">申请金额</td>
					<td id="applyAmtTd">-</td>
					<td class="label-cell">实退金额</td>
					<td id="refundAmtTd">-</td>
				</tr>
				<tr>
					<td class="label-cell">申请原因</td>
					<td colspan="3">${afterSale.applyReason}</td>
				</tr>
				<tr>
					<td class="label-cell">凭证图片</td>
					<td colspan="3" id="imgTd"><span class="empty-tip">无</span></td>
				</tr>
				<tr>
					<td class="label-cell">申请时间</td>
					<td id="createTimeTd">-</td>
					<td class="label-cell">退款时间</td>
					<td id="refundTimeTd">-</td>
				</tr>
			</table>
		</div>
	</div>

	<!-- ==================== 订单信息 ==================== -->
	<c:if test="${not empty order}">
		<div class="layui-card">
			<div class="layui-card-header">订单信息</div>
			<div class="layui-card-body">
				<table class="layui-table">
					<tr>
						<td class="label-cell">订单号</td><td width="38%">${order.orderNo}</td>
						<td class="label-cell">店铺</td><td width="38%">${order.shopName}</td>
					</tr>
					<tr>
						<td class="label-cell">收货人</td>
						<td>${order.receiverName} / ${order.receiverMobile}</td>
						<td class="label-cell">收货地址</td>
						<td>${order.receiverAddress}</td>
					</tr>
					<tr>
						<td class="label-cell">实付金额</td>
						<td id="paidAmtTd">-</td>
						<td class="label-cell">已退金额</td>
						<td id="orderRefundAmtTd">-</td>
					</tr>
					<tr>
						<td class="label-cell">订单状态</td>
						<td id="orderStatusTd">-</td>
						<td class="label-cell">支付状态</td>
						<td id="payStatusTd">-</td>
					</tr>
				</table>
			</div>
		</div>
	</c:if>

	<!-- ==================== 商品明细 ==================== -->
	<div class="layui-card">
		<div class="layui-card-header">商品明细</div>
		<div class="layui-card-body">
			<c:choose>
				<c:when test="${not empty itemList}">
					<table class="layui-table">
						<thead>
						<tr>
							<th>商品名称</th>
							<th>规格</th>
							<th>单价</th>
							<th>数量</th>
							<th>行金额</th>
							<th>已退金额</th>
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
				</c:when>
				<c:otherwise>
					<div class="empty-tip">暂无商品明细</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<!-- ==================== 状态流转日志 ==================== -->
	<div class="layui-card">
		<div class="layui-card-header">状态流转日志</div>
		<div class="layui-card-body">
			<c:choose>
				<c:when test="${not empty logList}">
					<table class="layui-table">
						<thead>
						<tr>
							<th width="12%">动作</th>
							<th width="20%">状态变更</th>
							<th width="10%">操作人</th>
							<th width="12%">来源</th>
							<th width="28%">备注</th>
							<th width="18%">时间</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${logList}" var="log">
							<tr>
								<td>
									<c:choose>
										<c:when test="${log.action == 1}">创建</c:when>
										<c:when test="${log.action == 2}">支付</c:when>
										<c:when test="${log.action == 3}">发货</c:when>
										<c:when test="${log.action == 4}">收货</c:when>
										<c:when test="${log.action == 5}">取消</c:when>
										<c:when test="${log.action == 6}">超时关闭</c:when>
										<c:when test="${log.action == 7}">部分退款</c:when>
										<c:when test="${log.action == 8}">全额退款</c:when>
										<c:when test="${log.action == 9}">关闭</c:when>
										<c:otherwise>${log.action}</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:if test="${not empty log.oldOrderStatus}">${log.oldOrderStatus} → </c:if>${log.newOrderStatus}
									<c:if test="${not empty log.newAfterSaleStatus}">
										<span style="color:#999;">（售后：${log.newAfterSaleStatus}）</span>
									</c:if>
								</td>
								<td>
									<c:choose>
										<c:when test="${log.operatorType == 1}">用户</c:when>
										<c:when test="${log.operatorType == 2}">商家</c:when>
										<c:when test="${log.operatorType == 3}">系统</c:when>
										<c:when test="${log.operatorType == 4}">平台</c:when>
										<c:otherwise>-</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${log.source == 0}">未知</c:when>
										<c:when test="${log.source == 1}">微信回调</c:when>
										<c:when test="${log.source == 2}">后台接口</c:when>
										<c:when test="${log.source == 3}">定时任务</c:when>
										<c:when test="${log.source == 4}">退款接口</c:when>
										<c:when test="${log.source == 5}">小程序端</c:when>
										<c:when test="${log.source == 6}">平台端</c:when>
										<c:otherwise>-</c:otherwise>
									</c:choose>
								</td>
								<td>${log.remark}</td>
								<td><fmt:formatDate value="${log.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="empty-tip">暂无日志</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

</c:if>

<script type="text/javascript">
	layui.config({ base: "${ctx}/static/js/" }).use(['form', 'layer', 'jquery'], function () {
		var $ = layui.$, layer = layui.layer;

		// 售后单数据
		var afterSale = {
			afterSaleType: '${afterSale.afterSaleType}',
			applyAmt: ${afterSale.applyAmt == null ? 0 : afterSale.applyAmt},
			refundAmt: ${afterSale.refundAmt == null ? 0 : afterSale.refundAmt},
			status: '${afterSale.status}',
			createTime: '${afterSale.createTime}',
			refundTime: '${afterSale.refundTime}',
			applyImages: '${afterSale.applyImages}'
		};

		$('#typeTd').text(afterSale.afterSaleType == '1' ? '仅退款' : afterSale.afterSaleType == '2' ? '退货退款' : '-');
		$('#statusTd').text(afterSaleStatusText(afterSale.status));
		$('#applyAmtTd').text('￥' + (afterSale.applyAmt / 100).toFixed(2));
		$('#refundAmtTd').text('￥' + (afterSale.refundAmt / 100).toFixed(2));
		$('#createTimeTd').text(afterSale.createTime || '-');
		$('#refundTimeTd').text(afterSale.refundTime || '-');

		// 凭证图片
		if (afterSale.applyImages && afterSale.applyImages !== 'null' && afterSale.applyImages !== '') {
			try {
				var imgs = JSON.parse(afterSale.applyImages);
				if (imgs && imgs.length > 0) {
					var html = '<div class="imgs-wrap">';
					for (var i = 0; i < imgs.length; i++) {
						html += '<img src="' + imgs[i] + '">';
					}
					html += '</div>';
					$('#imgTd').html(html);
				}
			} catch (e) {}
		}

		// 订单数据
		<c:if test="${not empty order}">
		var order = {
			paidAmt: ${order.paidAmt == null ? 0 : order.paidAmt},
			refundAmt: ${order.refundAmt == null ? 0 : order.refundAmt},
			orderStatus: '${order.orderStatus}',
			payStatus: '${order.payStatus}'
		};
		$('#paidAmtTd').text('￥' + (order.paidAmt / 100).toFixed(2));
		$('#orderRefundAmtTd').text('￥' + (order.refundAmt / 100).toFixed(2));
		$('#orderStatusTd').text(orderStatusText(order.orderStatus));
		$('#payStatusTd').text(payStatusText(order.payStatus));
		</c:if>

		function afterSaleStatusText(s) {
			return s == '1' ? '申请中' : s == '2' ? '退款中' : s == '3' ? '退款完成' : s == '4' ? '已关闭' : '-';
		}
		function orderStatusText(s) {
			return s == '0' ? '待付款' : s == '10' ? '待发货' : s == '20' ? '待收货' : s == '31' ? '正常完成'
				: s == '32' ? '用户取消' : s == '33' ? '超时关闭' : s == '34' ? '全额退款关闭' : s == '35' ? '平台关闭' : s;
		}
		function payStatusText(s) {
			return s == '0' ? '未支付' : s == '1' ? '已支付' : s == '2' ? '部分退款' : s == '3' ? '全额退款' : s;
		}
	});
</script>
</body>
</html>