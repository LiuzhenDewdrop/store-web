package com.wur7.store.bean;

import lombok.Data;

@Data
public class CommonReq {
	private Integer id;
	
	private Integer intValue;
	
	private Integer offset;
	private Integer pageSize;
	
}
