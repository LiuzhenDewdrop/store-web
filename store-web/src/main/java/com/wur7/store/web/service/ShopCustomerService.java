package com.wur7.store.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class ShopCustomerService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public PageInfo<ShopCustomer> allOfPage(ShopCustomer query, LayPage layPage) {
		PageHelper.startPage(layPage.getPage(), layPage.getLimit());
		List<ShopCustomer> list = mapperSupport.shopCustomerMapper.findAll(query);
		return new PageInfo<>(list);
	}

}
