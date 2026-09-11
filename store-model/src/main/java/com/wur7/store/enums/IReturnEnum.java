package com.wur7.store.enums;

import com.wur7.store.bean.IReturnBean;

public enum IReturnEnum {
	INADEQUATE_PERMISSIONS(IReturnBean.FAIL_CODE, "很遗憾，您的权限不足，无法操作"),
	;
	
	IReturnEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	private final String code;
	private final String msg;
	
	public String getCode() {
		return code;
	}
	
	public String getMsg() {
		return msg;
	}
}
