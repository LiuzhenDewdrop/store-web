package com.wur7.store.bean.vo;

import java.io.Serializable;
import java.util.List;

import com.wur7.store.entity.ShopOrder;
import com.wur7.store.entity.ShopOrderItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShopOrderResp extends ShopOrder implements Serializable {
	private List<ShopOrderItem> itemList;
	
	// 列表展示用的买家昵称
	private String customerNickname;
}
