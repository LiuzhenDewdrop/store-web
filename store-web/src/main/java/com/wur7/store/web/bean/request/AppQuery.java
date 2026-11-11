package com.wur7.store.web.bean.request;

import lombok.Data;

@Data
public class AppQuery {
	
	private String keyword;
	private Integer itemType;
	private Integer pageNum=1;
	private Integer pageSize=20;
}
