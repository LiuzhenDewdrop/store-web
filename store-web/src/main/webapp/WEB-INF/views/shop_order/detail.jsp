<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/comm/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<title>订单详情</title>
	<link rel="shortcut icon" href="${ctx}/static/img/5r7_logo.png">
	<link rel="stylesheet" href="${ctx}/static/layui/css/layui.css">
	<link rel="stylesheet" href="${ctx}/static/css/global.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" media="all">
	<link rel="stylesheet" type="text/css" href="${ctx}/static/css/fontcss.css">
	<script src="${ctx}/static/layui/layui.js"></script>
	<style>
		.layui-card { margin-bottom: 10px; }
		.layui-table td, .layui-table th { font-size: 13px; }
		.label-cell { background-color: #fafafa; width: 12%; }
		.empty-tip { color: #999; padding: 10px 0; }
		.goods-img { width: 60px; height: 60px; object-fit: cover; }

		html {
			height: 100%;
			overflow: hidden;
		}
		body.childrenBody {
			height: 100% !important;
			max-height: 100% !important;
			overflow-y: auto !important;
			overflow-x: hidden !important;
			margin: 0;
			padding: 10px;
			box-sizing: border-box;
		}
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

	<!-- ==================== 订单基本信息 ==================== -->
	<div class="layui-card">
		<div class="layui-card-header">订单基本信息</div>
		<div class="layui-card-body">
			<table class="layui-table">
				<tr>
					<td class="label-cell">订单号</td><td width="38%">${order.orderNo}</td>
					<td class="label-cell">订单状态</td><td width="38%" id="orderStatusTd">-</td>
				</tr>
				<tr>
					<td class="label-cell">支付单号</td><td>${order.outTradeNo}</td>
					<td class="label-cell">支付状态</td><td id="payStatusTd">-</td>
				</tr>
				<tr>
					<td class="label-cell">微信交易号</td><td>${order.transactionId}</td>
					<td class="label-cell">售后状态</td><td id="afterSaleStatusTd">-</td>
				</tr>
				<tr>
					<td class="label-cell">店铺</td><td>${order.shopName}</td>
					<td class="label-cell">支付方式</td><td id="payTypeTd">-</td>
				</tr>
				<tr>
					<td class="label-cell">下单时间</td>
					<td id="createTimeTd">
					<c:if test="${not empty order.createTime}">
						<fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</c:if>
					<c:if test="${empty order.createTime}">-</c:if>
				</td>
					<td class="label-cell">支付时间</td>
					<td id="payTimeTd">
						<c:choose>
							<c:when test="${not empty order.payTime}"><fmt:formatDate value="${order.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="label-cell">发货时间</td>
					<td id="shipTimeTd">
						<c:choose>
							<c:when test="${not empty order.shipTime}"><fmt:formatDate value="${order.shipTime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td class="label-cell">收货时间</td>
					<td id="receiveTimeTd">
						<c:choose>
							<c:when test="${not empty order.receiveTime}"><fmt:formatDate value="${order.receiveTime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="label-cell">完成时间</td>
					<td id="finishTimeTd">
						<c:choose>
							<c:when test="${not empty order.finishTime}"><fmt:formatDate value="${order.finishTime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td class="label-cell">取消/关闭时间</td>
					<td id="cancelOrCloseTimeTd">
						<c:choose>
							<c:when test="${not empty order.cancelTime}"><fmt:formatDate value="${order.cancelTime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
							<c:when test="${not empty order.closeTime}"><fmt:formatDate value="${order.closeTime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<c:if test="${order.orderStatus == 32 || order.orderStatus == 33 || order.orderStatus == 34 || order.orderStatus == 35}">
					<tr>
						<td class="label-cell">取消/关闭原因</td>
						<td colspan="3">${order.cancelReason}</td>
					</tr>
				</c:if>
				<tr>
					<td class="label-cell">买家留言</td><td>${order.buyerRemark}</td>
					<td class="label-cell">商家备注</td><td>${order.sellerRemark}</td>
				</tr>
			</table>
		</div>
	</div>

	<!-- ==================== 金额信息 ==================== -->
	<div class="layui-card">
		<div class="layui-card-header">金额信息</div>
		<div class="layui-card-body">
			<table class="layui-table">
				<tr>
					<td class="label-cell">商品金额</td><td id="goodsAmtTd">-</td>
					<td class="label-cell">运费</td><td id="freightAmtTd">-</td>
				</tr>
				<tr>
					<td class="label-cell">应付金额</td><td id="payAmtTd">-</td>
					<td class="label-cell">实付金额</td><td id="paidAmtTd">-</td>
				</tr>
				<tr>
					<td class="label-cell">已退款</td><td id="refundAmtTd">-</td>
					<td class="label-cell"></td><td></td>
				</tr>
			</table>
		</div>
	</div>

	<!-- ==================== 收货信息 ==================== -->
	<div class="layui-card">
		<div class="layui-card-header">收货信息</div>
		<div class="layui-card-body">
			<table class="layui-table">
				<tr>
					<td class="label-cell">收货人</td><td width="38%">${order.receiverName}</td>
					<td class="label-cell">手机号</td><td width="38%">${order.receiverMobile}</td>
				</tr>
				<tr>
					<td class="label-cell">收货地址</td><td colspan="3">${order.receiverAddress}</td>
				</tr>
			</table>
		</div>
	</div>

	<!-- ==================== 快递信息 ==================== -->
	<c:if test="${not empty order.expressNo}">
		<div class="layui-card">
			<div class="layui-card-header">快递信息</div>
			<div class="layui-card-body">
				<table class="layui-table">
					<tr>
						<td class="label-cell">物流公司</td><td width="38%">${order.expressCompanyName}</td>
						<td class="label-cell">运单号</td><td width="38%">${order.expressNo}</td>
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
							<th>图片</th>
							<th>商品名称</th>
							<th>规格</th>
							<th>单价</th>
							<th>数量</th>
							<th>行金额</th>
							<th>已退金额</th>
							<th>退款状态</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${itemList}" var="item">
							<tr>
								<td>
									<c:if test="${not empty item.specImg}">
										<img class="goods-img" src="${item.specImg}">
									</c:if>
								</td>
								<td>${item.itemName}</td>
								<td>${item.specName}</td>
								<td>￥<fmt:formatNumber value="${item.salePrice / 100}" pattern="0.00"/></td>
								<td>${item.quantity}</td>
								<td>￥<fmt:formatNumber value="${item.totalAmt / 100}" pattern="0.00"/></td>
								<td>￥<fmt:formatNumber value="${item.refundAmt / 100}" pattern="0.00"/></td>
								<td>
									<c:choose>
										<c:when test="${item.refundStatus == 0}">无</c:when>
										<c:when test="${item.refundStatus == 1}">申请中</c:when>
										<c:when test="${item.refundStatus == 2}">退款中</c:when>
										<c:when test="${item.refundStatus == 3}">已退款</c:when>
										<c:when test="${item.refundStatus == 4}">售后关闭</c:when>
										<c:otherwise>-</c:otherwise>
									</c:choose>
								</td>
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
<!-- 底部操作区 -->
<div style="text-align: center; padding: 30px 0 50px 0;">
	<button type="button" class="layui-btn layui-btn-primary layui-btn-lg" id="closeTabBtn">
		<i class="layui-icon layui-icon-return"></i> 关闭并返回列表
	</button>
</div>
<script type="text/javascript">
	layui.config({ base: "${ctx}/static/js/" }).use(['form', 'layer', 'jquery'], function () {
		var $ = layui.$;

		var order = {
			orderStatus: '${order.orderStatus}',
			payStatus: '${order.payStatus}',
			afterSaleStatus: '${order.afterSaleStatus}',
			payType: '${order.payType}',
			goodsAmt: ${order.goodsAmt == null ? 0 : order.goodsAmt},
			freightAmt: ${order.freightAmt == null ? 0 : order.freightAmt},
			payAmt: ${order.payAmt == null ? 0 : order.payAmt},
			paidAmt: ${order.paidAmt == null ? 0 : order.paidAmt},
			refundAmt: ${order.refundAmt == null ? 0 : order.refundAmt},
			<%--createTime: '${order.createTime}',--%>
			<%--payTime: '${order.payTime}',--%>
			<%--shipTime: '${order.shipTime}',--%>
			<%--receiveTime: '${order.receiveTime}',--%>
			<%--finishTime: '${order.finishTime}',--%>
			<%--cancelTime: '${order.cancelTime}',--%>
			<%--closeTime: '${order.closeTime}'--%>
		};

		$('#orderStatusTd').text(orderStatusText(order.orderStatus));
		$('#payStatusTd').text(payStatusText(order.payStatus));
		$('#afterSaleStatusTd').text(afterSaleStatusText(order.afterSaleStatus));
		$('#payTypeTd').text(order.payType == '1' ? '微信支付' : order.payType == '2' ? '余额' : '-');

		$('#goodsAmtTd').text('￥' + (order.goodsAmt / 100).toFixed(2));
		$('#freightAmtTd').text('￥' + (order.freightAmt / 100).toFixed(2));
		$('#payAmtTd').text('￥' + (order.payAmt / 100).toFixed(2));
		$('#paidAmtTd').text('￥' + (order.paidAmt / 100).toFixed(2));
		$('#refundAmtTd').text('￥' + (order.refundAmt / 100).toFixed(2));

		function orderStatusText(s) {
			return s == '0' ? '待付款' : s == '10' ? '待发货' : s == '20' ? '待收货' : s == '31' ? '正常完成'
				: s == '32' ? '用户取消' : s == '33' ? '超时关闭' : s == '34' ? '全额退款关闭' : s == '35' ? '平台关闭' : '-';
		}
		function payStatusText(s) {
			return s == '0' ? '未支付' : s == '1' ? '已支付' : s == '2' ? '部分退款' : s == '3' ? '全额退款' : '-';
		}
		function afterSaleStatusText(s) {
			return s == '0' ? '无' : s == '1' ? '申请中' : s == '2' ? '退款中' : s == '3' ? '退款完成' : s == '4' ? '售后关闭' : '-';
		}
		// 底部关闭按钮：关闭当前 tab
		$('#closeTabBtn').on('click', function () {
			// 获取当前 iframe 所在 tab 的 lay-id
			var layId = top.layui.$('.layui-tab-title .layui-this').attr('lay-id');
			if (layId) {
				// 调用父页面的 tabDelete 方法删除当前 tab
				parent.layui.element.tabDelete('bodyTab', layId).init();
			} else {
				// 如果拿不到 tab id，尝试用历史返回
				parent.history.back();
			}
		});
	});
</script>
</body>
</html>