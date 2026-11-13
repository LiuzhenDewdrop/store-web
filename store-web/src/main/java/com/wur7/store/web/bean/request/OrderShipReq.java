package com.wur7.store.web.bean.request;

import lombok.Data;

@Data
public class OrderShipReq {
	private Integer orderId;
	private String expressCompanyName;
	private String expressNo;
}