package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopOrder;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.bean.request.OrderShipReq;
import com.wur7.store.web.bean.response.ShopOrderDetail;
import com.wur7.store.web.service.ShopOrderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ShopOrderController
 * @description: 订单管理
 * @author: L.zhen
 */
@Slf4j
@Controller
@RequestMapping("order")
public class ShopOrderController extends BasicController {
	
	@Resource
	private ShopOrderService shopOrderService;
	
	/**
	 * @title  toView
	 * @description 订单管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("shop:order:view")
	public String toView() {
		return "shop_order/list";
	}
	
	/**
	 * @title  list
	 * @description 查询订单列表
	 * @param request
	 * @param query
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("shop:order:list")
	public IReturnBean<List<ShopOrder>> list(HttpServletRequest request, ShopOrder query, LayPage page) {
		try {
			PageInfo<ShopOrder> result = shopOrderService.allOfPage(query, page, getCurrentUser());
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询订单列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toDetail
	 * @description 跳转到订单详情页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/detail")
	@RequiresPermissions("shop:order:view")
	public String toDetail(HttpServletRequest request, Integer id) {
		IReturnBean<ShopOrderDetail> detail = shopOrderService.detail(id);
		if (detail.isSuccess()) {
			request.setAttribute("order", detail.getData().getOrder());
			request.setAttribute("itemList", detail.getData().getItemList());
			request.setAttribute("logList", detail.getData().getLogList());
		}
		return "shop_order/detail";
	}
	
	/**
	 * @title  ship
	 * @description 发货
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/ship.do")
	@ResponseBody
	@RequiresPermissions("shop:order:ship")
	public IReturnBean<?> ship(HttpServletRequest request, @RequestBody OrderShipReq req) {
		try {
			return shopOrderService.ship(req, getCurrentUser());
		} catch (Exception e) {
			log.error("发货 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  refund
	 * @description 退款（部分/全额）
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/refund.do")
	@ResponseBody
	@RequiresPermissions("shop:order:refund")
	public IReturnBean<?> refund(HttpServletRequest request, @RequestBody com.wur7.store.web.bean.request.OrderRefundReq req) {
		try {
			return shopOrderService.refund(req, getCurrentUser());
		} catch (Exception e) {
			log.error("退款 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  close
	 * @description 关闭订单（平台/商家）
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/close.do")
	@ResponseBody
	@RequiresPermissions("shop:order:close")
	public IReturnBean<?> close(HttpServletRequest request, @RequestParam("id") Integer id) {
		try {
			return shopOrderService.close(id, getCurrentUser());
		} catch (Exception e) {
			log.error("关闭订单 error:", e);
			return IReturnBean.error();
		}
	}
}