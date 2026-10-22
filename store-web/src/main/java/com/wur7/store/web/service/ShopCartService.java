package com.wur7.store.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.ShopCart;
import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.ShopItemSpec;
import com.wur7.store.util.util.CollectionUtil;
import com.wur7.store.web.bean.response.ShopCartResp;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class ShopCartService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public IReturnBean<List<List<ShopCartResp>>> findByCustomer(Integer customer) {
		List<ShopCart> list = mapperSupport.shopCartMapper.findByCustomer(customer, null, null);
		List<ShopCartResp> queryResult = new ArrayList<>(list.size());
		Map<Integer, List<ShopCartResp>> map = new LinkedHashMap<>();
		for (ShopCart cart : list) {
			ShopItem item = mapperSupport.shopItemMapper.selectByPrimaryKey(cart.getItemId());
			ShopItemSpec spec = mapperSupport.shopItemSpecMapper.selectByPrimaryKey(cart.getSpecId());
			ShopCartResp resp = new ShopCartResp(cart, item, spec);
			if (map.containsKey(item.getShopId())) {
				map.get(item.getShopId()).add(resp);
			} else {
				List<ShopCartResp> tempList = new ArrayList<>();
				tempList.add(resp);
				map.put(item.getShopId(), tempList);
			}
		}
		List<List<ShopCartResp>> resultList = new ArrayList<>(map.values());
		return IReturnBean.success(resultList);
	}
	
	public IReturnBean<?> add(ShopCart cart) {
		List<ShopCart> list = mapperSupport.shopCartMapper.findByCustomer(
				cart.getCustomerId(), cart.getItemId(), cart.getSpecId());
		if (CollectionUtil.isNotEmpty(list)) {
			ShopCart record = list.get(0);
			record.setQuantity(record.getQuantity() + cart.getQuantity());
			record.setSelectStatus(CommonConstant.SELECT_STATUS_YES);
			record.setUpdateTime(new Date());
			mapperSupport.shopCartMapper.updateByPrimaryKeySelective(record);
			return IReturnBean.success();
		}
		cart.setId(null);
//		cart.setCustomerId();
//		cart.setItemId();
//		cart.setSpecId();
//		cart.setQuantity();
		cart.setSelectStatus(CommonConstant.SELECT_STATUS_YES);
		cart.setItemStatus(CommonConstant.ITEM_STATUS_UP);
		cart.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
		cart.setCreateTime(new Date());
//		cart.setUpdateTime();
		mapperSupport.shopCartMapper.insert(cart);
		return IReturnBean.success();
	}
	
	public IReturnBean<?> del(ShopCart record) {
		ShopCart cart = new ShopCart();
		cart.setId(record.getId());
		cart.setDelStatus(CommonConstant.DEL_STATUS_DEL);
		cart.setUpdateTime(new Date());
		mapperSupport.shopCartMapper.updateByPrimaryKeySelective(cart);
		return IReturnBean.success();
	}
	
	public IReturnBean<?> upd(ShopCart record) {
		record.setUpdateTime(new Date());
		mapperSupport.shopCartMapper.updateByPrimaryKeySelective(record);
		return IReturnBean.success();
	}
	
}
