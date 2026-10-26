package com.wur7.store.web.bean.request;

import java.util.List;

import lombok.Data;

@Data
public class CartSelectReq {
	private Integer selectAll;	// true:all,false:shop
	private List<Integer> cartIds;	// cart.id
	private Integer selectStatus;
}
