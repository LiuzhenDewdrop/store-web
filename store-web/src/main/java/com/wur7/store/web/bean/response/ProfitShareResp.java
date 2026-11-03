package com.wur7.store.web.bean.response;

import com.wur7.store.entity.ShopProfitShareRecord;

import lombok.Data;

@Data
public class ProfitShareResp extends ShopProfitShareRecord {
	private String nickname;
}
