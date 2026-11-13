package com.wur7.store.web.service;

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
			return IReturnBean.fail("查不到此订单");
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
		mapperSupport.shopAfterSaleMapper.insertSelective(afterSale);
		
		mapperSupport.shopOrderMapper.updateAfterSaleStatus(order.getId(),
				CommonConstant.AFTER_SALE_STATUS_APPLYING, order.getVersion());
		
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
		log.setExtra("{\"afterSaleNo\":\"" + afterSale.getAfterSaleNo() + "\",\"applyAmt\":" + req.getApplyAmt() + "}");
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
		if (id == null) return IReturnBean.fail("查不到此售后单");
		ShopAfterSale afterSale = mapperSupport.shopAfterSaleMapper.selectByPrimaryKey(id);
		if (afterSale == null || afterSale.getDelStatus() == CommonConstant.DEL_STATUS_DEL) {
			return IReturnBean.fail("查不到此售后单");
		}
		// 店铺角色只能看自己店铺的
		if (loginUser.getRoleId() == CommonConstant.ROLE_ID_SHOP
				&& !afterSale.getShopId().equals(loginUser.getId())) {
			return IReturnBean.fail("无权查看该售后单");
		}
		return IReturnBean.success(afterSale);
	}
	
	private String genAfterSaleNo() {
		return "AS" + System.currentTimeMillis() + String.format("%04d", (int) (Math.random() * 10000));
	}
}