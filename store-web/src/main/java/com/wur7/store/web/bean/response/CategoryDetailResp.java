package com.wur7.store.web.bean.response;

import java.util.List;

import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.SysItemCategory;

import lombok.Data;

@Data
public class CategoryDetailResp {
	private SysItemCategory category;
	private List<ShopItem> list;
	
	public CategoryDetailResp() {
	}
	
	public CategoryDetailResp(SysItemCategory category, List<ShopItem> list) {
		this.category = category;
		this.list = list;
	}
}


