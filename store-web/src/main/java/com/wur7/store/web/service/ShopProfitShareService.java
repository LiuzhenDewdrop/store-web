package com.wur7.store.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wur7.store.bean.CommonReq;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.entity.ShopOrder;
import com.wur7.store.entity.ShopProfitShareRecord;
import com.wur7.store.web.bean.response.ProfitOverallVO;
import com.wur7.store.web.bean.response.ProfitShareResp;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class ShopProfitShareService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public IReturnBean<List<ProfitShareResp>> list(CommonReq req) {
		long total = mapperSupport.shopProfitShareRecordMapper.count(req);
		List<ShopProfitShareRecord> list = mapperSupport.shopProfitShareRecordMapper.findByCustomer(req);
		List<ProfitShareResp> resultList = new ArrayList<>(list.size());
		for (ShopProfitShareRecord record : list) {
			ProfitShareResp resp = new ProfitShareResp();
			if (record.getSrcCustomerId() == null) {
				resp.setNickname(CommonConstant.DEFAULT_NICKNAME);
			} else {
				ShopCustomer upper = mapperSupport.shopCustomerMapper.selectByPrimaryKey(record.getSrcCustomerId());
				if (upper != null && upper.getNickname() != null) {
					resp.setNickname(upper.getNickname());
				} else {
					resp.setNickname(CommonConstant.DEFAULT_NICKNAME);
				}
			}
			resp.setProfitAmt(record.getProfitAmt());
			if (req.getIntValue() == CommonConstant.SHARE_STATUS_UNFUND) {
				resp.setPayTime(record.getPayTime());
			} else {
				resp.setReceiveTime(record.getReceiveTime());
			}
			resultList.add(resp);
		}
		return IReturnBean.success(total, resultList);
	}
	
	
	public IReturnBean<List<ShopCustomer>> person(CommonReq req) {
		return IReturnBean.success(mapperSupport.shopCustomerMapper.findAllByUpper(req.getId()));
	}
	
	public IReturnBean<ProfitOverallVO> overall(CommonReq req) {
		ShopCustomer cus = mapperSupport.shopCustomerMapper.selectByPrimaryKey(req.getId());
		ProfitOverallVO vo = new ProfitOverallVO();
		vo.setPersonNum(cus.getPersonNum());
		vo.setUnfundAmt(cus.getUnfundAmt());
		vo.setFundAmt(cus.getFundAmt());
		return IReturnBean.success(vo);
	}
	
	// ==================== 订单完成触发分润 ====================
	
	/**
	 * 订单完成（status=31）触发分润
	 * 按分润规则算每个受益人金额，写 shop_profit_share_record，调三方分账
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> shareOnOrderFinish(Integer orderId) {
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(orderId);
		if (order == null) return IReturnBean.fail("订单不存在");
		
		// 幂等
		List<ShopProfitShareRecord> exists = mapperSupport.shopProfitShareRecordMapper.selectByOrderId(orderId);
		if (exists != null && !exists.isEmpty()) return IReturnBean.success();
		
		int baseAmt = order.getPaidAmt() - order.getFreightAmt();
		if (baseAmt <= 0) return IReturnBean.success();
		
		// 推广人 = 下单用户的上级
		ShopCustomer buyer = mapperSupport.shopCustomerMapper.selectByPrimaryKey(order.getCustomerId());
		if (buyer == null || buyer.getUpperId() == null) return IReturnBean.success();
		Integer promoterId = buyer.getUpperId();
		
		// TODO 分润比例，先写死 5%
		int shareRate = 500;
		int profitAmt = baseAmt * shareRate / 10000;
		if (profitAmt <= 0) return IReturnBean.success();
		
		// 写分润记录
		ShopProfitShareRecord record = new ShopProfitShareRecord();
		record.setOrderId(order.getId());
		record.setOrderNo(order.getOrderNo());
		record.setCustomerId(promoterId);
		record.setSrcCustomerId(order.getCustomerId());
		record.setBeneficiaryType(CommonConstant.BENEFICIARY_TYPE_PROMOTER);
		record.setShareType(CommonConstant.SHARE_TYPE_RATE);
		record.setShareRate(shareRate);
		record.setProfitAmt(profitAmt);
		record.setShareStatus(CommonConstant.SHARE_STATUS_UNFUND);
		record.setPayTime(order.getPayTime());
		mapperSupport.shopProfitShareRecordMapper.insertSelective(record);
		
		// 推广人待到账 +profitAmt
		mapperSupport.shopCustomerMapper.increaseUnfund(promoterId, profitAmt);
		
		// TODO 三方分账
		// paymentChannelService.share(order.getOrderNo(), record);
		
		return IReturnBean.success();
	}
	
	// ==================== 退款冲销分润 ====================
	
	/**
	 * 退款冲销分润
	 * 未到账的直接撤销；已到账的调三方分账回退
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public IReturnBean<?> revokeShareOnRefund(Integer orderId, Integer refundAmt) {
		ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(orderId);
		if (order == null) return IReturnBean.fail("订单不存在");
		
		List<ShopProfitShareRecord> records = mapperSupport.shopProfitShareRecordMapper.selectByOrderId(orderId);
		if (records == null || records.isEmpty()) return IReturnBean.success();
		
		for (ShopProfitShareRecord r : records) {
			// 按退款比例算冲销金额
			int revokeAmt = order.getPaidAmt() > 0
					? (int) ((long) r.getProfitAmt() * refundAmt / order.getPaidAmt())
					: r.getProfitAmt();
			if (revokeAmt <= 0) continue;
			
			boolean fullRevoke = (r.getRevokeAmt() + revokeAmt) >= r.getProfitAmt();
			
			if (r.getShareStatus() == CommonConstant.SHARE_STATUS_UNFUND) {
				// 待到账：减 unfund_amt，记录累加 revoke_amt（全撤才置 3）
				if (fullRevoke) {
					mapperSupport.shopProfitShareRecordMapper.updateShareStatusToCancel(
							r.getId(), revokeAmt, new Date(), r.getVersion());
				} else {
					mapperSupport.shopProfitShareRecordMapper.addRevokeAmt(r.getId(), revokeAmt, r.getVersion());
				}
				mapperSupport.shopCustomerMapper.decreaseUnfund(r.getCustomerId(), revokeAmt);
				
			} else if (r.getShareStatus() == CommonConstant.SHARE_STATUS_FUND) {
				// 已到账：TODO 三方分账回退
				// paymentChannelService.revokeShare(r.getSettleNo(), revokeAmt);
				// 回调成功后：
				//   全撤 → updateShareStatusToCancel
				//   部分 → addRevokeAmt
				//   decreaseFund(r.getCustomerId(), revokeAmt);
			}
		}
		return IReturnBean.success();
	}

}
