package com.wur7.store.web.bean.request;

import lombok.Data;

@Data
public class OrderPayCallbackReq {
	private String outTradeNo;
	private String transactionId;
	private Integer paidAmt;
	private Integer payType;
}