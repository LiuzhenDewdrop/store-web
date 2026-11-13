package com.wur7.store.web.controller.wx;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.web.bean.request.AfterSaleApplyReq;
import com.wur7.store.web.service.ShopAfterSaleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/wx/afterSale")
public class WxAfterSaleController extends WxBasicController {
	
	@Resource
	private ShopAfterSaleService shopAfterSaleService;
	
	/**
	 * @title  apply
	 * @description 申请售后
	 */
	@RequestMapping(value = "/apply")
	public IReturnBean<?> apply(HttpServletRequest request, @RequestBody AfterSaleApplyReq req) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopAfterSaleService.apply(req, currentUser.getId());
		} catch (Exception e) {
			log.error("申请售后 error:", e);
			return IReturnBean.error();
		}
	}
}