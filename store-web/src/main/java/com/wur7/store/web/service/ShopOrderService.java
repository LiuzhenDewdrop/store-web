package com.wur7.store.web.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.ShopOrder;
import com.wur7.store.entity.ShopOrderItem;
import com.wur7.store.entity.ShopOrderStatusLog;
import com.wur7.store.entity.SysUser;
import com.wur7.store.util.util.CollectionUtil;
import com.wur7.store.util.util.StringUtil;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.bean.request.AppQuery;
import com.wur7.store.web.bean.request.OrderCancelReq;
import com.wur7.store.web.bean.request.OrderRefundReq;
import com.wur7.store.web.bean.request.OrderShipReq;
import com.wur7.store.web.bean.response.ShopOrderDetail;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class ShopOrderService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	// ==================== 后台端 ====================
	
	public PageInfo<ShopOrder> allOfPage(ShopOrder query, LayPage layPage, SysUser loginUser) {
		if (loginUser.getRoleId() == CommonConstant.ROLE_ID_SHOP) {
			query.setShopId(loginUser.getId());
		}
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<ShopOrder> list = mapperSupport.shopOrderMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	public IReturnBean<ShopOrderDetail> detail(Integer id) {
		if (id == null) return IReturnBean.fail("查不到此订单");
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(id);
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("查不到此订单");
		}
		List<ShopOrderItem> itemList = mapperSupport.shopOrderItemMapper.selectByOrderId(id);
		List<ShopOrderStatusLog> logList = mapperSupport.shopOrderStatusLogMapper.selectByOrderId(id);
		return IReturnBean.success(new ShopOrderDetail(order, itemList, logList));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> ship(OrderShipReq req, SysUser operator) {
		if (req == null || req.getOrderId() == null) return IReturnBean.fail("订单id不能为空");
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(req.getOrderId());
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("查不到此订单");
		}
		if (operator.getRoleId() == CommonConstant.ROLE_ID_SHOP
				&& !order.getShopId().equals(operator.getId())) {
			return IReturnBean.fail("只能操作自己店铺的订单");
		}
		if (order.getOrderStatus() != CommonConstant.ORDER_STATUS_UNSHIPPED) {
			return IReturnBean.fail("仅待发货订单可发货");
		}
		int rows = mapperSupport.shopOrderMapper.updateShipped(order.getId(),
				CommonConstant.ORDER_STATUS_UNSHIPPED, order.getVersion(),
				req.getExpressCompanyName(), req.getExpressNo(), new Date());
		if (rows == 0) return IReturnBean.fail("订单状态已变更，请刷新后重试");
		writeLog(order, CommonConstant.ORDER_STATUS_UNSHIPPED, CommonConstant.ORDER_STATUS_UNRECEIVED,
				null, null, null, null,
				CommonConstant.ORDER_ACTION_SHIP, CommonConstant.OPERATOR_TYPE_SHOP, operator.getId(),
				CommonConstant.SOURCE_ADMIN_API,
				req.getExpressCompanyName() + " " + req.getExpressNo(), null);
		return IReturnBean.success();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> refund(OrderRefundReq req, SysUser operator) {
		if (req == null || req.getOrderId() == null || req.getRefundAmt() == null) {
			return IReturnBean.fail("参数不完整");
		}
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(req.getOrderId());
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("查不到此订单");
		}
		if (CommonConstant.isOrderFinalStatus(order.getOrderStatus())) {
			return IReturnBean.fail("订单已终结，不可退款");
		}
		if (order.getRefundAmt() + req.getRefundAmt() > order.getPaidAmt()) {
			return IReturnBean.fail("退款金额超过实付金额");
		}
		
		boolean fullRefund = req.getRefundAmt() >= (order.getPaidAmt() - order.getRefundAmt());
		int rows = fullRefund
				? mapperSupport.shopOrderMapper.updateFullRefundClose(order.getId(),
				order.getOrderStatus(), order.getVersion(), new Date())
				: mapperSupport.shopOrderMapper.updatePartialRefund(order.getId(),
				req.getRefundAmt(), order.getVersion());
		if (rows == 0) return IReturnBean.fail("订单状态已变更，请刷新后重试");
		
		if (req.getItemId() != null) {
			mapperSupport.shopOrderItemMapper.updateRefundByItemId(order.getId(), req.getItemId(), req.getRefundAmt());
		}
		writeLog(order,
				fullRefund ? order.getOrderStatus() : null,
				fullRefund ? CommonConstant.ORDER_STATUS_FULL_REFUND : null,
				order.getPayStatus(),
				fullRefund ? CommonConstant.PAY_STATUS_FULL_REFUND : CommonConstant.PAY_STATUS_PART_REFUND,
				order.getAfterSaleStatus(), CommonConstant.AFTER_SALE_STATUS_REFUNDED,
				fullRefund ? CommonConstant.ORDER_ACTION_FULL_REFUND : CommonConstant.ORDER_ACTION_PART_REFUND,
				operator.getRoleId() == CommonConstant.ROLE_ID_SHOP
						? CommonConstant.OPERATOR_TYPE_SHOP : CommonConstant.OPERATOR_TYPE_PLATFORM,
				operator.getId(), CommonConstant.SOURCE_ADMIN_API,
				req.getReason(), "{\"refundAmt\":" + req.getRefundAmt() + "}");
		// TODO 三方支付：调用退款接口
		// 例：paymentChannelService.refund(order.getOrderNo(), req.getRefundAmt(), req.getReason());
		// 退款结果以异步回调为准，回调里再更新 pay_status / refund_amt
		
		// TODO 三方分润：若该订单已分润，调用分账回退，冲销分润记录
		// profitShareService.revokeShareOnRefund(order.getId(), req.getRefundAmt());
		return IReturnBean.success();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> close(Integer id, SysUser operator) {
		if (id == null) return IReturnBean.fail("订单id不能为空");
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(id);
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("查不到此订单");
		}
		if (CommonConstant.isOrderFinalStatus(order.getOrderStatus())) {
			return IReturnBean.fail("订单已终结");
		}
		int rows = mapperSupport.shopOrderMapper.updateStatusWithVersion(order.getId(),
				order.getOrderStatus(), CommonConstant.ORDER_STATUS_FULL_REFUND,
				order.getVersion(), new Date());
		if (rows == 0) return IReturnBean.fail("订单状态已变更，请刷新后重试");
		writeLog(order, order.getOrderStatus(), CommonConstant.ORDER_STATUS_FULL_REFUND,
				null, null, null, null,
				CommonConstant.ORDER_ACTION_CLOSE, CommonConstant.OPERATOR_TYPE_PLATFORM, operator.getId(),
				CommonConstant.SOURCE_PLATFORM, "平台关闭订单", null);
		return IReturnBean.success();
	}
	
	// ==================== 小程序端 ====================
	
	public IReturnBean<List<ShopOrder>> listForApp(AppQuery query, Integer customerId) {
		int offset = (query.getPageNum() - 1) * query.getPageSize();
		List<ShopOrder> list = mapperSupport.shopOrderMapper.selectByCustomer(
				customerId, query.getOrderStatus(), offset, query.getPageSize());
		long total = mapperSupport.shopOrderMapper.countByCustomer(customerId, query.getOrderStatus());
		return IReturnBean.success(total, list);
	}
	
	public IReturnBean<ShopOrderDetail> detailForApp(Integer id, Integer customerId) {
		if (id == null) return IReturnBean.fail("查不到此订单");
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(id);
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL
				|| !order.getCustomerId().equals(customerId)) {
			return IReturnBean.fail("查不到此订单");
		}
		List<ShopOrderItem> itemList = mapperSupport.shopOrderItemMapper.selectByOrderId(id);
		return IReturnBean.success(new ShopOrderDetail(order, itemList, null));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> cancelForApp(OrderCancelReq req, Integer customerId) {
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(req.getOrderId());
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL
				|| !order.getCustomerId().equals(customerId)) {
			return IReturnBean.fail("查不到此订单");
		}
		if (order.getOrderStatus() != CommonConstant.ORDER_STATUS_UNPAID) {
			return IReturnBean.fail("仅待付款订单可取消");
		}
		int rows = mapperSupport.shopOrderMapper.updateStatusWithVersion(order.getId(),
				CommonConstant.ORDER_STATUS_UNPAID, CommonConstant.ORDER_STATUS_CANCELED,
				order.getVersion(), new Date());
		if (rows == 0) return IReturnBean.fail("订单状态已变更，请刷新后重试");
		writeLog(order, CommonConstant.ORDER_STATUS_UNPAID, CommonConstant.ORDER_STATUS_CANCELED,
				null, null, null, null,
				CommonConstant.ORDER_ACTION_CANCEL, CommonConstant.OPERATOR_TYPE_USER, customerId,
				CommonConstant.SOURCE_MINI_APP, req.getReason(), null);
		return IReturnBean.success();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> receiveForApp(Integer orderId, Integer customerId) {
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(orderId);
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL
				|| !order.getCustomerId().equals(customerId)) {
			return IReturnBean.fail("查不到此订单");
		}
		if (order.getOrderStatus() != CommonConstant.ORDER_STATUS_UNRECEIVED) {
			return IReturnBean.fail("仅待收货订单可确认收货");
		}
		Date now = new Date();
		int rows = mapperSupport.shopOrderMapper.updateReceived(order.getId(),
				CommonConstant.ORDER_STATUS_UNRECEIVED, order.getVersion(), now, now);
		if (rows == 0) return IReturnBean.fail("订单状态已变更，请刷新后重试");
		writeLog(order, CommonConstant.ORDER_STATUS_UNRECEIVED, CommonConstant.ORDER_STATUS_FINISHED,
				null, null, null, null,
				CommonConstant.ORDER_ACTION_RECEIVE, CommonConstant.OPERATOR_TYPE_USER, customerId,
				CommonConstant.SOURCE_MINI_APP, "确认收货", null);
		// TODO 三方分润：订单完成，触发分润计算并调三方分账接口
		// profitShareService.shareOnOrderFinish(orderId);
		return IReturnBean.success();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> refundForApp(OrderRefundReq req, Integer customerId) {
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(req.getOrderId());
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL
				|| !order.getCustomerId().equals(customerId)) {
			return IReturnBean.fail("查不到此订单");
		}
		if (CommonConstant.isOrderFinalStatus(order.getOrderStatus())) {
			return IReturnBean.fail("订单已终结，不可退款");
		}
		if (order.getPayStatus() == CommonConstant.PAY_STATUS_UNPAID) {
			return IReturnBean.fail("订单未支付，无需退款");
		}
		if (order.getRefundAmt() + req.getRefundAmt() > order.getPaidAmt()) {
			return IReturnBean.fail("退款金额超过实付金额");
		}
		// 小程序端只标记售后申请中，实际退款由后台/回调处理
		int rows = mapperSupport.shopOrderMapper.updateAfterSaleStatus(order.getId(),
				CommonConstant.AFTER_SALE_STATUS_APPLYING, order.getVersion());
		if (rows == 0) return IReturnBean.fail("订单状态已变更，请刷新后重试");
		writeLog(order, null, null, null, null,
				order.getAfterSaleStatus(), CommonConstant.AFTER_SALE_STATUS_APPLYING,
				CommonConstant.ORDER_ACTION_PART_REFUND, CommonConstant.OPERATOR_TYPE_USER, customerId,
				CommonConstant.SOURCE_MINI_APP, req.getReason(),
				"{\"refundAmt\":" + req.getRefundAmt() + ",\"itemId\":" + req.getItemId() + "}");
		return IReturnBean.success();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> delForApp(Integer orderId, Integer customerId) {
		if (orderId == null) return IReturnBean.fail("订单id不能为空");
		int rows = mapperSupport.shopOrderMapper.softDelete(orderId, customerId);
		if (rows == 0) return IReturnBean.fail("查不到此订单或无权删除");
		return IReturnBean.success("订单已删除");
	}
	
	// ==================== 支付回调 ====================
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> handlePayCallback(String outTradeNo, String transactionId,
											Integer paidAmt, Integer payType) {
		if (StringUtil.isBlank(outTradeNo)) {
			return IReturnBean.fail("商户支付单号不能为空");
		}
		ShopOrder order = mapperSupport.shopOrderMapper.selectByOutTradeNo(outTradeNo);
		if (order == null) {
			return IReturnBean.fail("订单不存在");
		}
		// 幂等：已支付直接返回成功
		if (order.getPayStatus() == CommonConstant.PAY_STATUS_PAID) {
			return IReturnBean.success();
		}
		if (order.getOrderStatus() != CommonConstant.ORDER_STATUS_UNPAID) {
			return IReturnBean.fail("订单状态不允许支付");
		}
		int rows = mapperSupport.shopOrderMapper.updatePaid(order.getId(),
				CommonConstant.ORDER_STATUS_UNPAID, order.getVersion(),
				transactionId, payType == null ? CommonConstant.PAY_TYPE_WECHAT : payType,
				paidAmt, new Date());
		if (rows == 0) {
			return IReturnBean.fail("订单状态已变更");
		}
		writeLog(order,
				CommonConstant.ORDER_STATUS_UNPAID, CommonConstant.ORDER_STATUS_UNSHIPPED,
				CommonConstant.PAY_STATUS_UNPAID, CommonConstant.PAY_STATUS_PAID,
				null, null,
				CommonConstant.ORDER_ACTION_PAY,
				CommonConstant.OPERATOR_TYPE_SYSTEM, null,
				CommonConstant.SOURCE_WECHAT_CALLBACK,
				"微信支付成功",
				"{\"transactionId\":\"" + transactionId + "\",\"paidAmt\":" + paidAmt + "}");
		// TODO 三方支付：支付成功后的确认收款/分账准备（按支付机构 API 决定是否需要）
		// 若该机构支付成功即可分账，此处可不调，分润延后到订单完成
		return IReturnBean.success();
	}
	
	// ==================== 定时任务用 ====================
	
	/** 超时未支付订单自动关闭（定时任务调用） */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void closeExpiredUnpaid(Integer limit) {
		List<ShopOrder> list = mapperSupport.shopOrderMapper.selectExpiredUnpaid(new Date(), limit);
		if (CollectionUtil.isEmpty(list)) {
			return;
		}
		for (ShopOrder order : list) {
			int rows = mapperSupport.shopOrderMapper.updateStatusWithVersion(order.getId(),
					CommonConstant.ORDER_STATUS_UNPAID, CommonConstant.ORDER_STATUS_TIMEOUT,
					order.getVersion(), new Date());
			if (rows == 0) {
				continue;
			}
			writeLog(order,
					CommonConstant.ORDER_STATUS_UNPAID, CommonConstant.ORDER_STATUS_TIMEOUT,
					null, null, null, null,
					CommonConstant.ORDER_ACTION_TIMEOUT,
					CommonConstant.OPERATOR_TYPE_SYSTEM, null,
					CommonConstant.SOURCE_TIMER,
					"待付款超时自动关闭", null);
		}
	}
	
	/** 已发货超时未收货自动确认（定时任务调用） */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void autoReceive(Integer limit) {
		Date deadline = new Date(System.currentTimeMillis() - 7L * 24 * 3600 * 1000);
		List<ShopOrder> list = mapperSupport.shopOrderMapper.selectShippedTimeout(deadline, limit);
		if (CollectionUtil.isEmpty(list)) {
			return;
		}
		for (ShopOrder order : list) {
			Date now = new Date();
			int rows = mapperSupport.shopOrderMapper.updateReceived(order.getId(),
					CommonConstant.ORDER_STATUS_UNRECEIVED, order.getVersion(), now, now);
			if (rows == 0) {
				continue;
			}
			writeLog(order,
					CommonConstant.ORDER_STATUS_UNRECEIVED, CommonConstant.ORDER_STATUS_FINISHED,
					null, null, null, null,
					CommonConstant.ORDER_ACTION_RECEIVE,
					CommonConstant.OPERATOR_TYPE_SYSTEM, null,
					CommonConstant.SOURCE_TIMER,
					"发货超时自动确认收货", null);
			// TODO 三方分润：订单完成，触发分润
			// profitShareService.shareOnOrderFinish(order.getId());
		}
	}
	
	// ==================== 私有工具 ====================
	
	private void writeLog(ShopOrder order,
						  Integer oldOrderStatus, Integer newOrderStatus,
						  Integer oldPayStatus, Integer newPayStatus,
						  Integer oldAfterSaleStatus, Integer newAfterSaleStatus,
						  int action, int operatorType, Integer operatorId,
						  int source, String remark, String extra) {
		ShopOrderStatusLog log = new ShopOrderStatusLog();
		log.setOrderId(order.getId());
		log.setOrderNo(order.getOrderNo());
		log.setOldOrderStatus(oldOrderStatus);
		// new_order_status 为 NOT NULL，null 时用当前订单状态兜底
		log.setNewOrderStatus(newOrderStatus == null ? order.getOrderStatus() : newOrderStatus);
		log.setOldPayStatus(oldPayStatus);
		log.setNewPayStatus(newPayStatus);
		log.setOldAfterSaleStatus(oldAfterSaleStatus);
		log.setNewAfterSaleStatus(newAfterSaleStatus);
		log.setAction(action);
		log.setOperatorType(operatorType);
		log.setOperatorId(operatorId);
		log.setSource(source);
		log.setRemark(remark);
		log.setExtra(extra);
		mapperSupport.shopOrderStatusLogMapper.insertSelective(log);
	}
}