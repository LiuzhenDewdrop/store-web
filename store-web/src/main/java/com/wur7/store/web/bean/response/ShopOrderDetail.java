package com.wur7.store.web.bean.response;

import java.util.List;

import com.wur7.store.entity.ShopOrder;
import com.wur7.store.entity.ShopOrderItem;
import com.wur7.store.entity.ShopOrderStatusLog;

import lombok.Data;

@Data
public class ShopOrderDetail {
	private ShopOrder order;
	private List<ShopOrderItem> itemList;
	private List<ShopOrderStatusLog> logList;
	
	public ShopOrderDetail() {}
	
	public ShopOrderDetail(ShopOrder order, List<ShopOrderItem> itemList, List<ShopOrderStatusLog> logList) {
		this.order = order;
		this.itemList = itemList;
		this.logList = logList;
	}
}