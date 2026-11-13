package com.wur7.store.web.bean.request;

import lombok.Data;

@Data
public class OrderCancelReq {
	private Integer orderId;
	private String reason;
}