package com.wur7.store.web.bean.request;

import lombok.Data;

@Data
public class AfterSaleApplyReq {
	private Integer orderId;
	private Integer orderItemId;   // 可选，行级售后
	private Integer afterSaleType; // 1仅退款 2退货退款
	private Integer applyAmt;      // 分
	private String applyReason;
	private String applyImages;    // JSON 数组字符串
}