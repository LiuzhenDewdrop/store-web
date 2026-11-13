package com.wur7.store.web.bean.request;

import lombok.Data;

@Data
public class OrderRefundReq {
	private Integer orderId;
	private Integer itemId;      // 可选：行级退款
	private Integer refundAmt;   // 单位：分
	private String reason;
}