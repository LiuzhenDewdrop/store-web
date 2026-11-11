package com.wur7.store.web.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wur7.store.bean.CommonReq;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.ShopCustomer;
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

}
