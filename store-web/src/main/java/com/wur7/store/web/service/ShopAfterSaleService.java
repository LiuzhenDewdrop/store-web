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
import com.wur7.store.entity.ShopAfterSale;
import com.wur7.store.entity.ShopOrder;
import com.wur7.store.entity.ShopOrderStatusLog;
import com.wur7.store.entity.SysUser;
import com.wur7.store.util.util.StringUtil;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.bean.request.AfterSaleApplyReq;
import com.wur7.store.web.mapper.MapperSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShopAfterSaleService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	// ==================== 小程序：申请售后 ====================
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> apply(AfterSaleApplyReq req, Integer customerId) {
		if (req == null || req.getOrderId() == null || req.getApplyAmt() == null) {
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
		if (order.getRefundAmt() + req.getApplyAmt() > order.getPaidAmt()) {
			return IReturnBean.fail("申请金额超过可退金额");
		}
		
		Date now = new Date();
		ShopAfterSale afterSale = new ShopAfterSale();
		afterSale.setAfterSaleNo(genAfterSaleNo());
		afterSale.setOrderId(order.getId());
		afterSale.setOrderNo(order.getOrderNo());
		afterSale.setOrderItemId(req.getOrderItemId());
		afterSale.setCustomerId(customerId);
		afterSale.setShopId(order.getShopId());
		afterSale.setAfterSaleType(req.getAfterSaleType() == null
				? CommonConstant.AFTER_SALE_TYPE_REFUND_ONLY : req.getAfterSaleType());
		afterSale.setApplyAmt(req.getApplyAmt());
		afterSale.setApplyReason(req.getApplyReason());
		afterSale.setApplyImages(req.getApplyImages());
		afterSale.setAuditStatus(CommonConstant.AUDIT_STATUS_PENDING);
		afterSale.setStatus(CommonConstant.AFTER_SALE_STATUS_APPLYING);
		afterSale.setVersion(0);
		afterSale.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
		afterSale.setCreateTime(now);
		afterSale.setUpdateTime(now);
		mapperSupport.shopAfterSaleMapper.insertSelective(afterSale);
		
		int rows = mapperSupport.shopOrderMapper.updateAfterSaleStatus(order.getId(),
				CommonConstant.AFTER_SALE_STATUS_APPLYING, order.getVersion(), now);
		if (rows == 0) {
			return IReturnBean.fail("订单状态已变更，请刷新后重试");
		}
		
		ShopOrderStatusLog log = new ShopOrderStatusLog();
		log.setOrderId(order.getId());
		log.setOrderNo(order.getOrderNo());
		log.setOldAfterSaleStatus(order.getAfterSaleStatus());
		log.setNewAfterSaleStatus(CommonConstant.AFTER_SALE_STATUS_APPLYING);
		log.setNewOrderStatus(order.getOrderStatus());
		log.setAction(CommonConstant.ORDER_ACTION_PART_REFUND);
		log.setOperatorType(CommonConstant.OPERATOR_TYPE_USER);
		log.setOperatorId(customerId);
		log.setSource(CommonConstant.SOURCE_MINI_APP);
		log.setRemark("用户申请售后：" + req.getApplyReason());
		log.setExtra("{\"afterSaleNo\":\"" + afterSale.getAfterSaleNo()
				+ "\",\"applyAmt\":" + req.getApplyAmt() + "}");
		mapperSupport.shopOrderStatusLogMapper.insertSelective(log);
		
		return IReturnBean.success("售后申请已提交", afterSale.getAfterSaleNo());
	}
	
	// ==================== 后台：分页（按 shopId 隔离）====================
	
	public PageInfo<ShopAfterSale> allOfPage(ShopAfterSale query, LayPage layPage, SysUser loginUser) {
		if (loginUser.getRoleId() == CommonConstant.ROLE_ID_SHOP) {
			query.setShopId(loginUser.getId());
		}
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<ShopAfterSale> list = mapperSupport.shopAfterSaleMapper.findAll(query);
		return new PageInfo<>(list);
	}
	
	// ==================== 后台：详情 ====================
	
	public IReturnBean<ShopAfterSale> detail(Integer id, SysUser loginUser) {
		if (id == null) return IReturnBean.fail("操作失败，查无此售后单");
		ShopAfterSale afterSale = mapperSupport.shopAfterSaleMapper.selectByPrimaryKey(id);
		if (afterSale == null || afterSale.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("操作失败，查无此售后单");
		}
		// 核心越权拦截：伪装成查无此单
		if (loginUser.getRoleId() == CommonConstant.ROLE_ID_SHOP
				&& !afterSale.getShopId().equals(loginUser.getId())) {
			return IReturnBean.fail("操作失败，查无此售后单");
		}
		return IReturnBean.success(afterSale);
	}
	
	// ==================== 后台：同意退款（金额可改）====================
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> agreeRefund(Integer afterSaleId, Integer refundAmt, String remark, SysUser operator) {
		if (operator == null) return IReturnBean.unlogin();
		if (afterSaleId == null) return IReturnBean.fail("售后单id不能为空");
		
		ShopAfterSale afterSale = mapperSupport.shopAfterSaleMapper.selectByPrimaryKey(afterSaleId);
		if (afterSale == null || afterSale.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("操作失败，查无此售后单");
		}
		// 核心越权拦截
		if (operator.getRoleId() == CommonConstant.ROLE_ID_SHOP
				&& !afterSale.getShopId().equals(operator.getId())) {
			return IReturnBean.fail("操作失败，查无此售后单");
		}
		
		if (afterSale.getStatus() != CommonConstant.AFTER_SALE_STATUS_APPLYING) {
			return IReturnBean.fail("该售后单已处理");
		}
		
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(afterSale.getOrderId());
		if (order == null) return IReturnBean.fail("操作失败，查无此订单");
		if (CommonConstant.isOrderFinalStatus(order.getOrderStatus())) {
			return IReturnBean.fail("订单已终结，不可退款");
		}
		
		int finalRefundAmt = refundAmt == null ? afterSale.getApplyAmt() : refundAmt;
		if (finalRefundAmt <= 0) return IReturnBean.fail("退款金额必须大于0");
		if (finalRefundAmt > afterSale.getApplyAmt()) {
			return IReturnBean.fail("退款金额不能超过申请金额");
		}
		if (order.getRefundAmt() + finalRefundAmt > order.getPaidAmt()) {
			return IReturnBean.fail("退款金额超过可退金额");
		}
		
		Date now = new Date();
		
		// 1. 售后单：同意 + 退款完成
		ShopAfterSale upd = new ShopAfterSale();
		upd.setId(afterSale.getId());
		upd.setAuditStatus(CommonConstant.AUDIT_STATUS_AGREED);
		upd.setAuditRemark(remark);
		upd.setAuditOperatorId(operator.getId());
		upd.setAuditTime(now);
		upd.setRefundAmt(finalRefundAmt);
		upd.setRefundTime(now);
		upd.setStatus(CommonConstant.AFTER_SALE_STATUS_REFUNDED);
		upd.setVersion(afterSale.getVersion());
		upd.setUpdateTime(now);
		int rows = mapperSupport.shopAfterSaleMapper.updateByPrimaryKeySelective(upd);
		if (rows == 0) return IReturnBean.fail("售后单状态已变更，请刷新后重试");
		
		// 2. 订单退款：部分/全额
		boolean fullRefund = finalRefundAmt >= (order.getPaidAmt() - order.getRefundAmt());
		int orderRows = fullRefund
				? mapperSupport.shopOrderMapper.updateFullRefundClose(order.getId(),
				order.getOrderStatus(), order.getVersion(), now, now)
				: mapperSupport.shopOrderMapper.updatePartialRefund(order.getId(),
				finalRefundAmt, order.getVersion(), now);
		if (orderRows == 0) return IReturnBean.fail("订单状态已变更，请刷新后重试");
		
		// 3. 行级退款
		if (afterSale.getOrderItemId() != null) {
			mapperSupport.shopOrderItemMapper.updateRefundByItemId(order.getId(),
					afterSale.getOrderItemId(), finalRefundAmt);
		}
		
		// 4. 订单状态日志
		writeOrderLog(order,
				fullRefund ? CommonConstant.ORDER_STATUS_FULL_REFUND : null,
				fullRefund ? CommonConstant.PAY_STATUS_FULL_REFUND : CommonConstant.PAY_STATUS_PART_REFUND,
				CommonConstant.AFTER_SALE_STATUS_REFUNDED,
				fullRefund ? CommonConstant.ORDER_ACTION_FULL_REFUND : CommonConstant.ORDER_ACTION_PART_REFUND,
				operator, "后台同意退款：" + finalRefundAmt + "分", afterSale.getAfterSaleNo());
		
		// TODO 三方支付：调退款接口
		// paymentChannelService.refund(order.getOrderNo(), finalRefundAmt, remark);
		
		return IReturnBean.success();
	}
	
	// ==================== 后台：拒绝售后 ====================
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> rejectRefund(Integer afterSaleId, String remark, SysUser operator) {
		if (operator == null) return IReturnBean.unlogin();
		if (afterSaleId == null) return IReturnBean.fail("售后单id不能为空");
		if (StringUtil.isBlank(remark)) return IReturnBean.fail("拒绝原因不能为空");
		
		ShopAfterSale afterSale = mapperSupport.shopAfterSaleMapper.selectByPrimaryKey(afterSaleId);
		if (afterSale == null || afterSale.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("操作失败，查无此售后单");
		}
		// 核心越权拦截
		if (operator.getRoleId() == CommonConstant.ROLE_ID_SHOP
				&& !afterSale.getShopId().equals(operator.getId())) {
			return IReturnBean.fail("操作失败，查无此售后单");
		}
		
		if (afterSale.getStatus() != CommonConstant.AFTER_SALE_STATUS_APPLYING) {
			return IReturnBean.fail("该售后单已处理");
		}
		
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(afterSale.getOrderId());
		if (order == null) return IReturnBean.fail("操作失败，查无此订单");
		
		Date now = new Date();
		
		// 1. 售后单：拒绝 + 关闭
		ShopAfterSale upd = new ShopAfterSale();
		upd.setId(afterSale.getId());
		upd.setAuditStatus(CommonConstant.AUDIT_STATUS_REJECTED);
		upd.setAuditRemark(remark);
		upd.setAuditOperatorId(operator.getId());
		upd.setAuditTime(now);
		upd.setStatus(CommonConstant.AFTER_SALE_STATUS_CLOSED);
		upd.setVersion(afterSale.getVersion());
		upd.setUpdateTime(now);
		int rows = mapperSupport.shopAfterSaleMapper.updateByPrimaryKeySelective(upd);
		if (rows == 0) return IReturnBean.fail("售后单状态已变更，请刷新后重试");
		
		// 2. 订单售后状态 → 关闭
		mapperSupport.shopOrderMapper.updateAfterSaleStatus(order.getId(),
				CommonConstant.AFTER_SALE_STATUS_CLOSED, order.getVersion(), now);
		
		// 3. 订单状态日志
		writeOrderLog(order, null, null, CommonConstant.AFTER_SALE_STATUS_CLOSED,
				CommonConstant.ORDER_ACTION_CLOSE, operator,
				"后台拒绝售后：" + remark, afterSale.getAfterSaleNo());
		
		return IReturnBean.success();
	}
	
	// ==================== 私有工具 ====================
	
	private String genAfterSaleNo() {
		return "AS" + System.currentTimeMillis() + String.format("%04d", (int) (Math.random() * 10000));
	}
	
	private void writeOrderLog(ShopOrder order, Integer newOrderStatus, Integer newPayStatus,
							   Integer newAfterSaleStatus, int action,
							   SysUser operator, String remark, String afterSaleNo) {
		ShopOrderStatusLog log = new ShopOrderStatusLog();
		log.setOrderId(order.getId());
		log.setOrderNo(order.getOrderNo());
		log.setNewOrderStatus(newOrderStatus == null ? order.getOrderStatus() : newOrderStatus);
		log.setNewPayStatus(newPayStatus);
		log.setNewAfterSaleStatus(newAfterSaleStatus);
		log.setAction(action);
		log.setOperatorType(operator.getRoleId() == CommonConstant.ROLE_ID_SHOP
				? CommonConstant.OPERATOR_TYPE_SHOP : CommonConstant.OPERATOR_TYPE_PLATFORM);
		log.setOperatorId(operator.getId());
		log.setSource(CommonConstant.SOURCE_ADMIN_API);
		log.setRemark(remark);
		log.setExtra("{\"afterSaleNo\":\"" + afterSaleNo + "\"}");
		mapperSupport.shopOrderStatusLogMapper.insertSelective(log);
	}
}