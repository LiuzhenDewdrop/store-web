package com.wur7.store.enums;

public enum ITEM_STATUS {
	// 商品状态【1：上架，2：下架，3：草稿】
	
	UP(1),
	DOWN(2),
	DRAFT(3);
	
	public final int STATUS;
	
	ITEM_STATUS(int status) {
		this.STATUS = status;
	}
}
