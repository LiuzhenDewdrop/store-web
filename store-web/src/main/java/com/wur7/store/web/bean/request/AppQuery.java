package com.wur7.store.web.bean.request;

import lombok.Data;

@Data
public class AppQuery {
	
	private String keyword;
	private Integer pageNum;
	private Integer pageSize;
}
