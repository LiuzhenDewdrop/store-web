package com.wur7.store.web.controller.wx;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopCusDelivery;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.web.service.ShopCusDeliveryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/wx/delivery")
public class WxDeliveryController extends WxBasicController {
	
	@Resource
	private ShopCusDeliveryService shopCusDeliveryService;

	@RequestMapping(value = "/list")
	public IReturnBean<List<ShopCusDelivery>> list(HttpServletRequest request) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopCusDeliveryService.findByCustomer(currentUser.getId());
		} catch (Exception e) {
			log.error("查询快递地址列表 error:", e);
			return IReturnBean.error();
		}
	}

	/**
	 * @title  add
	 * @description 添加快递地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	public IReturnBean<?> add(HttpServletRequest request, @RequestBody ShopCusDelivery delivery) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopCusDeliveryService.add(delivery, currentUser.getId());
		} catch (Exception e) {
			log.error("添加快递地址 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除快递地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/del")
	public IReturnBean<?> del(HttpServletRequest request, @RequestBody Integer delivery) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopCusDeliveryService.del(delivery, currentUser.getId());
		} catch (Exception e) {
			log.error("删除快递地址中商品 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  upd
	 * @description 修改快递地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/upd")
	public IReturnBean<?> upd(HttpServletRequest request, @RequestBody ShopCusDelivery delivery) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopCusDeliveryService.upd(delivery, currentUser.getId());
		} catch (Exception e) {
			log.error("修改快递地址商品 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  detail
	 * @description 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/detail")
	public IReturnBean<?> detail(HttpServletRequest request, @RequestBody Integer id) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopCusDeliveryService.detail(id, currentUser.getId());
		} catch (Exception e) {
			log.error("查询快递地址详细信息 error:", e);
			return IReturnBean.error();
		}
	}
}
