package com.wur7.store.web.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.SysItemCategory;
import com.wur7.store.web.bean.response.CategoryDetailResp;
import com.wur7.store.web.emuns.Category;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class SysItemCategoryService {
	
	@Resource
	private MapperSupport mapperSupport;
	
	public IReturnBean<List<SysItemCategory>> findByPid(Integer pId) {
		return IReturnBean.success(mapperSupport.sysItemCategoryMapper.findByPid(pId));
	}
	
	public IReturnBean<List<SysItemCategory>> findActivityBase() {
		List<SysItemCategory> resultList = new ArrayList<>();
		SysItemCategory cat1 = Category.toEntity(Category.ACTIVITY);
		resultList.add(cat1);
		SysItemCategory cat2 = Category.toEntity(Category.COURSE);
		resultList.add(cat2);
		List<Integer> list = mapperSupport.shopItemMapper.findCategoryBase();
		for (Integer id : list) {
			if (id == null) {
				continue;
			}
			SysItemCategory cat = mapperSupport.sysItemCategoryMapper.selectByPrimaryKey(id);
			resultList.add(cat);
		}
		return IReturnBean.success(resultList);
	}

	public IReturnBean<List<CategoryDetailResp>> findActivityDetail(Integer pid) {
		List<CategoryDetailResp> resultList = new ArrayList<>();
		Category c = Category.fromId(pid);
		if (c != null) {
			List<ShopItem> itemList = mapperSupport.shopItemMapper.findByType(c.getItemType());
			SysItemCategory cat = Category.toEntity(c);
			
			CategoryDetailResp detailResp = new CategoryDetailResp(cat, itemList);
			resultList.add(detailResp);
			return IReturnBean.success(resultList);
		}
		List<ShopItem> itemList = mapperSupport.shopItemMapper.findByCategory1(pid);
		Map<Integer, List<ShopItem>> map = itemList.stream()
				.collect(Collectors.groupingBy(
						ShopItem::getItemCategory2,
						LinkedHashMap::new,
						Collectors.toList()
				));
		for (Map.Entry<Integer, List<ShopItem>> entry : map.entrySet()) {
			SysItemCategory cat = mapperSupport.sysItemCategoryMapper.selectByPrimaryKey(entry.getKey());
			resultList.add(new CategoryDetailResp(cat, entry.getValue()));
		}
		return IReturnBean.success(resultList);
	}
}
