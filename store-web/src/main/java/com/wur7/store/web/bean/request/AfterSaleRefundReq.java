package com.wur7.store.web.bean.request;

import lombok.Data;

@Data
public class AfterSaleRefundReq {
	private Integer afterSaleId;
	private Integer refundAmt;
	private String remark;
}