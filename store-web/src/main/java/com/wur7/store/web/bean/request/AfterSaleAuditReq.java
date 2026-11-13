package com.wur7.store.web.bean.request;

import lombok.Data;

@Data
public class AfterSaleAuditReq {
	private Integer id;
	private Integer auditStatus;   // 2同意 3拒绝
	private Integer refundAmt;     // 同意时实际退款金额（分）
	private String auditRemark;    // 拒绝时必填
}