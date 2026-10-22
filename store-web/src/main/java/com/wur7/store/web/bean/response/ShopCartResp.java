package com.wur7.store.web.bean.response;

import com.wur7.store.entity.ShopCart;
import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.ShopItemSpec;

import lombok.Data;

@Data
public class ShopCartResp {
	private ShopCart cart;
	private ShopItem item;
	private ShopItemSpec spec;
	
	public ShopCartResp(ShopCart cart, ShopItem item, ShopItemSpec spec) {
		this.cart = cart;
		this.item = item;
		this.spec = spec;
	}
}
