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
import com.wur7.store.web.bean.request.CartSelectReq;
import com.wur7.store.web.bean.response.ShopCartResp;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class ShopCartService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public IReturnBean<List<List<ShopCartResp>>> findByCustomer(Integer customer) {
		List<ShopCart> list = mapperSupport.shopCartMapper.findByCustomer(customer, null, null);
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
	
	public IReturnBean<?> add(ShopCart cart, Integer customer) {
		if (cart == null || cart.getItemId() == null) {
			return IReturnBean.fail("添加失败，无此商品");
		}
		if (cart.getSpecId() == null) {
			List<ShopItemSpec> specList = mapperSupport.shopItemSpecMapper.getByItem(cart.getItemId());
			if (CollectionUtil.isEmpty(specList)) {
				return IReturnBean.fail("添加失败，无此商品");
			}
			cart.setSpecId(specList.get(0).getId());
		}
		if (cart.getQuantity() == null || cart.getQuantity() < 1) {
			cart.setQuantity(1);
		}
		List<ShopCart> list = mapperSupport.shopCartMapper.findByCustomer(
				customer, cart.getItemId(), cart.getSpecId());
		if (CollectionUtil.isNotEmpty(list)) {
			ShopCart record = list.get(0);
			record.setQuantity(record.getQuantity() + cart.getQuantity());
			record.setSelectStatus(CommonConstant.SELECT_STATUS_YES);
			record.setUpdateTime(new Date());
			mapperSupport.shopCartMapper.updateByPrimaryKeySelective(record);
			return IReturnBean.success();
		}
		cart.setId(null);
		cart.setCustomerId(customer);
//		cart.setItemId();
//		cart.setSpecId();
//		cart.setQuantity();
		cart.setSelectStatus(CommonConstant.SELECT_STATUS_YES);
		cart.setItemStatus(CommonConstant.ITEM_STATUS_UP);
		cart.setDelStatus(CommonConstant.DEL_STATUS_EXIST);
		cart.setCreateTime(new Date());
		cart.setUpdateTime(new Date());		// 排序用
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
	
	public IReturnBean<?> select(Integer customer, CartSelectReq req) {
		if (req == null) {
			return IReturnBean.success();
		}
		if (req.getSelectAll() == 1) {
			ShopCart record = new ShopCart();
			record.setCustomerId(customer);
			record.setSelectStatus(req.getSelectStatus());
//			record.setUpdateTime(new Date());	切换选中状态不影响排序
			mapperSupport.shopCartMapper.updateCustomerSelectStatus(record);
			return IReturnBean.success();
		}
		if (CollectionUtil.isEmpty(req.getCartIds())) {
			return IReturnBean.success();
		}
		for (Integer cartId : req.getCartIds()) {
			if (cartId == null) {
				continue;
			}
			ShopCart record = new ShopCart();
			record.setId(cartId);
			record.setSelectStatus(req.getSelectStatus());
//			record.setUpdateTime(new Date());	切换选中状态不影响排序
			mapperSupport.shopCartMapper.updateCartSelectStatus(record);
		}
		return IReturnBean.success();
	}
	
}
