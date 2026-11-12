package com.wur7.store.web.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.ShopCusDelivery;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class ShopCusDeliveryService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public IReturnBean<List<ShopCusDelivery>> findByCustomer(Integer customer) {
		List<ShopCusDelivery> list = mapperSupport.shopCusDeliveryMapper.findByCustomer(customer);
		return IReturnBean.success(list);
	}
	
	public IReturnBean<?> add(ShopCusDelivery delivery, Integer customer) {
		delivery.setCustomerId(customer);
		delivery.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
		delivery.setCreateTime(new Date());
		if (delivery.getDefaultStatus() == CommonConstant.YES) {
			// 如果新增这个是default，顶掉现在的
			mapperSupport.shopCusDeliveryMapper.updateDefaultByCustomer(customer);
		}
		delivery.setCustomerId(customer);
		mapperSupport.shopCusDeliveryMapper.insert(delivery);
		return IReturnBean.success();
	}
	
	public IReturnBean<?> del(Integer deliveryId, Integer customer) {
		ShopCusDelivery record = mapperSupport.shopCusDeliveryMapper.selectByPrimaryKey(deliveryId);
		if (record == null || record.getCustomerId() != customer) {
			return IReturnBean.fail("未找到该地址");
		}
		ShopCusDelivery delivery = new ShopCusDelivery();
		delivery.setId(deliveryId);
		delivery.setDelStatus(CommonConstant.DEL_STATUS_DEL);
		// 直接清default状态
		delivery.setDefaultStatus(CommonConstant.NO);
		mapperSupport.shopCusDeliveryMapper.updateByPrimaryKeySelective(delivery);
		return IReturnBean.success();
	}
	
	public IReturnBean<?> upd(ShopCusDelivery delivery, Integer customer) {
		if (delivery.getDefaultStatus() == CommonConstant.YES) {
			// 如果要修改的这个是default，顶掉现在的，不论现在这个是不是ta都无所谓
			mapperSupport.shopCusDeliveryMapper.updateDefaultByCustomer(customer);
		}
		mapperSupport.shopCusDeliveryMapper.updateByPrimaryKeySelective(delivery);
		return IReturnBean.success();
	}
	
	public IReturnBean<ShopCusDelivery> detail(Integer deliveryId, Integer customer) {
		ShopCusDelivery delivery = mapperSupport.shopCusDeliveryMapper.selectByPrimaryKey(deliveryId);
		if (delivery == null || delivery.getDelStatus() == CommonConstant.DEL_STATUS_DEL
				||delivery.getCustomerId() != customer) {
			return IReturnBean.fail("未查询到相关地址");
		}
		return IReturnBean.success(delivery);
	}
}
