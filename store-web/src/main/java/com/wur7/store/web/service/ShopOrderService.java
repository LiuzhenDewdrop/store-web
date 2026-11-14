package com.wur7.store.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.bean.vo.ShopOrderQuery;
import com.wur7.store.bean.vo.ShopOrderResp;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.ShopAfterSale;
import com.wur7.store.entity.ShopCustomer;
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
	
	public IReturnBean<List<ShopOrderResp>> allOfPage(ShopOrderQuery query, LayPage layPage, SysUser loginUser) {
		if (loginUser.getRoleId() == CommonConstant.ROLE_ID_SHOP) {
			query.setShopId(loginUser.getId());
		}
		List<Integer> customerIds = null;
		// 1. 如果传了买家昵称，先去用户表模糊查人
		if (StringUtil.isNotBlank(query.getCustomerNickname())) {
			ShopCustomer searchCus = new ShopCustomer();
			searchCus.setNickname(query.getCustomerNickname());
			List<ShopCustomer> customers = mapperSupport.shopCustomerMapper.findAll(searchCus);
			if (CollectionUtil.isEmpty(customers)) {
				// 没搜到人，直接返回空，不查订单表了
				return IReturnBean.success(0L, new ArrayList<>());
			}
			// 提取出 ID 列表
			customerIds = customers.stream().map(ShopCustomer::getId).collect(Collectors.toList());
		}
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		// 2. 走带 IN 查询的方法
		List<ShopOrderResp> list = mapperSupport.shopOrderMapper.findPageByCustomerIds(query, customerIds);
		long total = new PageInfo<>(list).getTotal();
		// 3. 补全商品明细
		for (ShopOrderResp resp : list) {
			resp.setItemList(mapperSupport.shopOrderItemMapper.selectByOrderId(resp.getId()));
		}
		return IReturnBean.success(total, list);
	}
	
	public IReturnBean<ShopOrderDetail> detail(Integer id, SysUser loginUser) {
		if (id == null) return IReturnBean.fail("操作失败，查无此订单");
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(id);
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		// 越权拦截：统一伪装成查无此单
		if (loginUser.getRoleId() == CommonConstant.ROLE_ID_SHOP
				&& !order.getShopId().equals(loginUser.getId())) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		
		List<ShopOrderItem> itemList = mapperSupport.shopOrderItemMapper.selectByOrderId(id);
		List<ShopOrderStatusLog> logList = mapperSupport.shopOrderStatusLogMapper.selectByOrderId(id);
		return IReturnBean.success(new ShopOrderDetail(order, itemList, logList));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> ship(OrderShipReq req, SysUser operator) {
		if (operator == null) return IReturnBean.unlogin();
		if (req == null || req.getOrderId() == null) return IReturnBean.fail("订单id不能为空");
		if (StringUtil.isBlank(req.getExpressCompanyName()) || StringUtil.isBlank(req.getExpressNo())) {
			return IReturnBean.fail("物流公司和运单号不能为空");
		}
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(req.getOrderId());
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		if (operator.getRoleId() == CommonConstant.ROLE_ID_SHOP
				&& !order.getShopId().equals(operator.getId())) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		if (order.getOrderStatus() != CommonConstant.ORDER_STATUS_UNSHIPPED) {
			return IReturnBean.fail("仅待发货订单可发货");
		}
		Date now = new Date();
		int rows = mapperSupport.shopOrderMapper.updateShipped(order.getId(),
				CommonConstant.ORDER_STATUS_UNSHIPPED, order.getVersion(),
				req.getExpressCompanyName(), req.getExpressNo(), now, now);
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
		if (operator == null) return IReturnBean.unlogin();
		if (req == null || req.getOrderId() == null || req.getRefundAmt() == null) {
			return IReturnBean.fail("参数不完整");
		}
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(req.getOrderId());
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		// 越权拦截
		if (operator.getRoleId() == CommonConstant.ROLE_ID_SHOP
				&& !order.getShopId().equals(operator.getId())) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		if (CommonConstant.isOrderFinalStatus(order.getOrderStatus())) {
			return IReturnBean.fail("订单已终结，不可退款");
		}
		if (order.getRefundAmt() + req.getRefundAmt() > order.getPaidAmt()) {
			return IReturnBean.fail("退款金额超过实付金额");
		}
		Date now = new Date();
		boolean fullRefund = req.getRefundAmt() >= (order.getPaidAmt() - order.getRefundAmt());
		int rows = fullRefund
				? mapperSupport.shopOrderMapper.updateFullRefundClose(order.getId(),
				order.getOrderStatus(), order.getVersion(), now, now)
				: mapperSupport.shopOrderMapper.updatePartialRefund(order.getId(),
				req.getRefundAmt(), order.getVersion(), now);
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
		return IReturnBean.success();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> close(Integer id, SysUser operator) {
		if (operator == null) return IReturnBean.unlogin();
		if (id == null) return IReturnBean.fail("操作失败，查无此订单");
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(id);
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		if (operator.getRoleId() == CommonConstant.ROLE_ID_SHOP
				&& !order.getShopId().equals(operator.getId())) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		if (CommonConstant.isOrderFinalStatus(order.getOrderStatus())) {
			return IReturnBean.fail("订单已终结");
		}
		String reason = "平台关闭订单";
		Date now = new Date();
		int rows = mapperSupport.shopOrderMapper.updateCancel(order.getId(),
				order.getOrderStatus(), CommonConstant.ORDER_STATUS_PLATFORM_CLOSE,
				order.getVersion(), null, now, reason, now);
		if (rows == 0) return IReturnBean.fail("订单状态已变更，请刷新后重试");
		
		writeLog(order, order.getOrderStatus(), CommonConstant.ORDER_STATUS_PLATFORM_CLOSE,
				null, null, null, null,
				CommonConstant.ORDER_ACTION_CLOSE, CommonConstant.OPERATOR_TYPE_PLATFORM, operator.getId(),
				CommonConstant.SOURCE_PLATFORM, reason, null);
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
		if (id == null) return IReturnBean.fail("操作失败，查无此订单");
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(id);
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL
				|| !order.getCustomerId().equals(customerId)) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		List<ShopOrderItem> itemList = mapperSupport.shopOrderItemMapper.selectByOrderId(id);
		return IReturnBean.success(new ShopOrderDetail(order, itemList, null));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> cancelForApp(OrderCancelReq req, Integer customerId) {
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(req.getOrderId());
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL
				|| !order.getCustomerId().equals(customerId)) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		if (order.getOrderStatus() != CommonConstant.ORDER_STATUS_UNPAID) {
			return IReturnBean.fail("仅待付款订单可取消");
		}
		String reason = StringUtil.isBlank(req.getReason()) ? "用户主动取消" : req.getReason();
		Date now = new Date();
		int rows = mapperSupport.shopOrderMapper.updateCancel(order.getId(),
				CommonConstant.ORDER_STATUS_UNPAID, CommonConstant.ORDER_STATUS_CANCELED,
				order.getVersion(), now, null, reason, now);
		if (rows == 0) return IReturnBean.fail("订单状态已变更，请刷新后重试");
		writeLog(order, CommonConstant.ORDER_STATUS_UNPAID, CommonConstant.ORDER_STATUS_CANCELED,
				null, null, null, null,
				CommonConstant.ORDER_ACTION_CANCEL, CommonConstant.OPERATOR_TYPE_USER, customerId,
				CommonConstant.SOURCE_MINI_APP, reason, null);
		return IReturnBean.success();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> receiveForApp(Integer orderId, Integer customerId) {
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(orderId);
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL
				|| !order.getCustomerId().equals(customerId)) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		if (order.getOrderStatus() != CommonConstant.ORDER_STATUS_UNRECEIVED) {
			return IReturnBean.fail("仅待收货订单可确认收货");
		}
		Date now = new Date();
		int rows = mapperSupport.shopOrderMapper.updateReceived(order.getId(),
				CommonConstant.ORDER_STATUS_UNRECEIVED, order.getVersion(), now, now, now);
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
		if (req == null || req.getOrderId() == null || req.getRefundAmt() == null) {
			return IReturnBean.fail("参数不完整");
		}
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(req.getOrderId());
		if (order == null || order.getDelStatus() == CommonConstant.DEL_STATUS_DEL
				|| !order.getCustomerId().equals(customerId)) {
			return IReturnBean.fail("操作失败，查无此订单");
		}
		if (CommonConstant.isOrderFinalStatus(order.getOrderStatus())) {
			return IReturnBean.fail("订单已终结，不可申请售后");
		}
		if (order.getPayStatus() == CommonConstant.PAY_STATUS_UNPAID) {
			return IReturnBean.fail("订单未支付，无需售后");
		}
		if (order.getRefundAmt() + req.getRefundAmt() > order.getPaidAmt()) {
			return IReturnBean.fail("申请金额超过可退金额");
		}
		
		// 1. 生成售后单
		ShopAfterSale afterSale = new ShopAfterSale();
		afterSale.setAfterSaleNo(genAfterSaleNo());
		afterSale.setOrderId(order.getId());
		afterSale.setOrderNo(order.getOrderNo());
		afterSale.setOrderItemId(req.getItemId());
		afterSale.setCustomerId(customerId);
		afterSale.setShopId(order.getShopId());
		afterSale.setAfterSaleType(CommonConstant.AFTER_SALE_TYPE_REFUND_ONLY);
		afterSale.setApplyAmt(req.getRefundAmt());
		afterSale.setApplyReason(req.getReason());
		afterSale.setApplyImages(req.getApplyImages());
		afterSale.setAuditStatus(CommonConstant.AUDIT_STATUS_PENDING);
		afterSale.setStatus(CommonConstant.AFTER_SALE_STATUS_APPLYING);
		afterSale.setVersion(0);
		afterSale.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
		afterSale.setCreateTime(new Date());
		afterSale.setUpdateTime(new Date());
		mapperSupport.shopAfterSaleMapper.insertSelective(afterSale);
		
		// 2. 订单售后状态 → 申请中
		Date now = new Date();
		int rows = mapperSupport.shopOrderMapper.updateAfterSaleStatus(order.getId(),
				CommonConstant.AFTER_SALE_STATUS_APPLYING, order.getVersion(), now);
		if (rows == 0) {
			return IReturnBean.fail("订单状态已变更，请刷新后重试");
		}
		
		// 3. 写订单状态日志
		writeLog(order, null, null, null, null,
				order.getAfterSaleStatus(), CommonConstant.AFTER_SALE_STATUS_APPLYING,
				CommonConstant.ORDER_ACTION_PART_REFUND, CommonConstant.OPERATOR_TYPE_USER, customerId,
				CommonConstant.SOURCE_MINI_APP, req.getReason(),
				"{\"afterSaleNo\":\"" + afterSale.getAfterSaleNo()
						+ "\",\"refundAmt\":" + req.getRefundAmt()
						+ ",\"itemId\":" + req.getItemId() + "}");
		
		return IReturnBean.success("售后申请已提交", afterSale.getAfterSaleNo());
	}
	
	private String genAfterSaleNo() {
		return "AS" + System.currentTimeMillis() + String.format("%04d", (int) (Math.random() * 10000));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> delForApp(Integer orderId, Integer customerId) {
		if (orderId == null) return IReturnBean.fail("订单id不能为空");
		int rows = mapperSupport.shopOrderMapper.softDelete(orderId, customerId, new Date());
		if (rows == 0) return IReturnBean.fail("操作失败，查无此订单或无权删除");
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
		// 金额校验：微信回调金额必须等于应付金额
		if (paidAmt == null || order.getPayAmt() == null || !paidAmt.equals(order.getPayAmt())) {
			return IReturnBean.fail("支付金额与订单金额不一致");
		}
		Date now = new Date();
		int rows = mapperSupport.shopOrderMapper.updatePaid(order.getId(),
				CommonConstant.ORDER_STATUS_UNPAID, order.getVersion(),
				transactionId, payType == null ? CommonConstant.PAY_TYPE_WECHAT : payType,
				paidAmt, now, now);
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
		return IReturnBean.success();
	}
	
	// ==================== 定时任务用 ====================
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void closeExpiredUnpaid(Integer limit) {
		List<ShopOrder> list = mapperSupport.shopOrderMapper.selectExpiredUnpaid(new Date(), limit);
		if (CollectionUtil.isEmpty(list)) {
			return;
		}
		for (ShopOrder order : list) {
			Date now = new Date();
			int rows = mapperSupport.shopOrderMapper.updateCancel(order.getId(),
					CommonConstant.ORDER_STATUS_UNPAID, CommonConstant.ORDER_STATUS_TIMEOUT,
					order.getVersion(), now, null, "超时未支付自动关闭", now);
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
					CommonConstant.ORDER_STATUS_UNRECEIVED, order.getVersion(), now, now, now);
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