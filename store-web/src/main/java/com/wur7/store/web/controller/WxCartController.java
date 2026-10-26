package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopCart;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.web.bean.request.CartSelectReq;
import com.wur7.store.web.bean.response.ShopCartResp;
import com.wur7.store.web.service.ShopCartService;
import com.wur7.store.web.service.WxLoginService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/wx/cart")
public class WxCartController extends WxBasicController {
	
	@Resource
	private ShopCartService shopCartService;
	@Resource
	private WxLoginService wxLoginService;

	/**
	 * @title  list
	 * @description 查询购物车列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public IReturnBean<List<List<ShopCartResp>>> list(HttpServletRequest request) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopCartService.findByCustomer(currentUser.getId());
		} catch (Exception e) {
			log.error("查询购物车列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  add
	 * @description 添加进购物车
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	public IReturnBean<?> add(HttpServletRequest request, @RequestBody ShopCart cart) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopCartService.add(cart, currentUser.getId());
		} catch (Exception e) {
			log.error("添加进购物车 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除购物车中商品
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/del")
	public IReturnBean<?> del(HttpServletRequest request, @RequestBody ShopCart cart) {
		try {
			if (checkWxUnlogin(request)) {
				return IReturnBean.unlogin();
			}
			return shopCartService.del(cart);
		} catch (Exception e) {
			log.error("删除购物车中商品 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  upd
	 * @description 修改购物车商品
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/upd")
	public IReturnBean<?> upd(HttpServletRequest request, @RequestBody ShopCart cart) {
		try {
			if (checkWxUnlogin(request)) {
				return IReturnBean.unlogin();
			}
			return shopCartService.upd(cart);
		} catch (Exception e) {
			log.error("修改购物车商品 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  sel
	 * @description 批量修改购物车商品选中状态
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sel")
	public IReturnBean<?> select(HttpServletRequest request, @RequestBody CartSelectReq req) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopCartService.select(currentUser.getId(), req);
		} catch (Exception e) {
			log.error("批量修改购物车商品选中状态 error:", e);
			return IReturnBean.error();
		}
	}
}
