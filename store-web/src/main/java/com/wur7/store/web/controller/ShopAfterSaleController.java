package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopAfterSale;
import com.wur7.store.entity.ShopOrder;
import com.wur7.store.entity.ShopOrderItem;
import com.wur7.store.entity.ShopOrderStatusLog;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.bean.request.AfterSaleRefundReq;
import com.wur7.store.web.mapper.MapperSupport;
import com.wur7.store.web.service.ShopAfterSaleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("afterSale")
public class ShopAfterSaleController extends BasicController {
	
	@Resource
	private ShopAfterSaleService shopAfterSaleService;
	@Resource
	private MapperSupport mapperSupport;
	
	/** 售后管理-跳转 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("shop:afterSale:view")
	public String toView() {
		return "shop_after_sale/list";
	}
	
	/** 售后列表（按登录人店铺隔离） */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("shop:afterSale:list")
	public IReturnBean<List<ShopAfterSale>> list(HttpServletRequest request, ShopAfterSale query, LayPage page) {
		try {
			PageInfo<ShopAfterSale> result = shopAfterSaleService.allOfPage(query, page, getCurrentUser());
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询售后列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/** 售后详情 */
	@RequestMapping(value = "/detail")
	@RequiresPermissions("shop:afterSale:view")
	public String toDetail(HttpServletRequest request, Integer id) {
		try {
			IReturnBean<ShopAfterSale> detail = shopAfterSaleService.detail(id, getCurrentUser());
			if (!detail.isSuccess()) {
				request.setAttribute("errorMsg", detail.getMsg());
				return "shop_after_sale/detail";
			}
			
			ShopAfterSale afterSale = detail.getData();
			request.setAttribute("afterSale", afterSale);
			
			ShopOrder order = mapperSupport.shopOrderMapper.selectByPrimaryKey(afterSale.getOrderId());
			request.setAttribute("order", order);
			
			List<ShopOrderItem> itemList = mapperSupport.shopOrderItemMapper.selectByOrderId(afterSale.getOrderId());
			request.setAttribute("itemList", itemList);
			
			List<ShopOrderStatusLog> logList = mapperSupport.shopOrderStatusLogMapper.selectByOrderId(afterSale.getOrderId());
			request.setAttribute("logList", logList);
			
		} catch (Exception e) {
			log.error("售后详情 error:", e);
			request.setAttribute("errorMsg", "系统异常，请稍后重试");
		}
		return "shop_after_sale/detail";
	}
	
	/** 同意退款（金额可改） */
	@RequestMapping(value = "/agreeRefund.do")
	@ResponseBody
	@RequiresPermissions("shop:afterSale:refund")
	public IReturnBean<?> agreeRefund(HttpServletRequest request, @RequestBody AfterSaleRefundReq req) {
		try {
			return shopAfterSaleService.agreeRefund(req.getAfterSaleId(), req.getRefundAmt(),
					req.getRemark(), getCurrentUser());
		} catch (Exception e) {
			log.error("同意退款 error:", e);
			return IReturnBean.error();
		}
	}
	
	/** 拒绝售后 */
	@RequestMapping(value = "/rejectRefund.do")
	@ResponseBody
	@RequiresPermissions("shop:afterSale:refund")
	public IReturnBean<?> rejectRefund(HttpServletRequest request, @RequestBody AfterSaleRefundReq req) {
		try {
			return shopAfterSaleService.rejectRefund(req.getAfterSaleId(), req.getRemark(), getCurrentUser());
		} catch (Exception e) {
			log.error("拒绝售后 error:", e);
			return IReturnBean.error();
		}
	}
}