package com.wur7.store.web.controller.wx;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.entity.ShopOrder;
import com.wur7.store.web.bean.request.AppQuery;
import com.wur7.store.web.bean.request.OrderCancelReq;
import com.wur7.store.web.bean.request.OrderRefundReq;
import com.wur7.store.web.service.ShopOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/wx/order")
public class WxShopOrderController extends WxBasicController {
	
	@Resource
	private ShopOrderService shopOrderService;
	
	/**
	 * @title  list
	 * @description 我的订单列表
	 * @param request
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/list")
	public IReturnBean<?> list(HttpServletRequest request, @RequestBody AppQuery query) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			return shopOrderService.listForApp(query, currentUser.getId());
		} catch (Exception e) {
			log.error("我的订单列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  detail
	 * @description 订单详情
	 * @param request
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/detail")
	public IReturnBean<?> detail(HttpServletRequest request, @RequestBody ShopOrder order) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			if (order == null || order.getId() == null) {
				return IReturnBean.fail("查不到此订单");
			}
			return shopOrderService.detailForApp(order.getId(), currentUser.getId());
		} catch (Exception e) {
			log.error("订单详情 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  cancel
	 * @description 取消订单（仅待付款）
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/cancel")
	public IReturnBean<?> cancel(HttpServletRequest request, @RequestBody OrderCancelReq req) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			if (req == null || req.getOrderId() == null) {
				return IReturnBean.fail("订单id不能为空");
			}
			return shopOrderService.cancelForApp(req, currentUser.getId());
		} catch (Exception e) {
			log.error("取消订单 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  receive
	 * @description 确认收货
	 * @param request
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/receive")
	public IReturnBean<?> receive(HttpServletRequest request, @RequestBody ShopOrder order) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			if (order == null || order.getId() == null) {
				return IReturnBean.fail("订单id不能为空");
			}
			return shopOrderService.receiveForApp(order.getId(), currentUser.getId());
		} catch (Exception e) {
			log.error("确认收货 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  refund
	 * @description 申请退款
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/refund")
	public IReturnBean<?> refund(HttpServletRequest request, @RequestBody OrderRefundReq req) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			if (req == null || req.getOrderId() == null || req.getRefundAmt() == null) {
				return IReturnBean.fail("参数不完整");
			}
			return shopOrderService.refundForApp(req, currentUser.getId());
		} catch (Exception e) {
			log.error("申请退款 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除订单（软删）
	 * @param request
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/del")
	public IReturnBean<?> del(HttpServletRequest request, @RequestBody ShopOrder order) {
		try {
			ShopCustomer currentUser = getWxLoginUser(request);
			if (currentUser == null) {
				return IReturnBean.unlogin();
			}
			if (order == null || order.getId() == null) {
				return IReturnBean.fail("订单id不能为空");
			}
			return shopOrderService.delForApp(order.getId(), currentUser.getId());
		} catch (Exception e) {
			log.error("删除订单 error:", e);
			return IReturnBean.error();
		}
	}
}