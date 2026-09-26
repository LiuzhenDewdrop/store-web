package com.wur7.store.web.bean;

import java.util.List;

import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.ShopItemPic;
import com.wur7.store.entity.ShopItemSpec;

import lombok.Data;

@Data
public class ShopItemDetail {
	private ShopItem item;
	private List<ShopItemPic> pics;
	private List<ShopItemSpec> specs;
}
