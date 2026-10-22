package com.wur7.store.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.web.service.WxLoginService;

@Controller
@Scope("prototype")
public class WxBasicController extends BasicController {
	
	@Resource
	private WxLoginService wxLoginService;
	
	public ShopCustomer getWxLoginUser(HttpServletRequest request) {
		String token = request.getHeader("token");
		return wxLoginService.getCustomerByToken(token);
	}
	
	public boolean checkWxUnlogin(HttpServletRequest request) {
		return getWxLoginUser(request) == null;
	}

}
