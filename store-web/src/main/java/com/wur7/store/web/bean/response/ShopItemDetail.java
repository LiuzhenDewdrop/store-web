package com.wur7.store.web.bean.response;

import java.util.List;

import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.ShopItemPic;
import com.wur7.store.entity.ShopItemSpec;

import lombok.Data;

@Data
public class ShopItemDetail {
	private ShopItem item;
	private List<ShopItemSpec> specList;
	private List<ShopItemPic> picList;
	
	public ShopItemDetail() {
	}
	
	public ShopItemDetail(ShopItem item, List<ShopItemSpec> specList, List<ShopItemPic> picList) {
		this.item = item;
		this.specList = specList;
		this.picList = picList;
	}
}
