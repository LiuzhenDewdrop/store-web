package com.wur7.store.bean.vo;

import com.wur7.store.entity.ShopOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShopOrderQuery extends ShopOrder {
	/** 下单时间范围-开始 yyyy-MM-dd */
	private String createTimeStart;
	/** 下单时间范围-结束 yyyy-MM-dd */
	private String createTimeEnd;
	/** 买家昵称（搜索用） */
	private String customerNickname;
}