package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.service.ShopCustomerService;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ShopCustomerController
 * @description: 
 * @author: L.zhen
 * @date:   2026/11/1 12:33
 */
@Slf4j
@Controller
@RequestMapping("customer")
public class ShopCustomerController extends BasicController {
	
	@Resource
	private ShopCustomerService shopCustomerService;
	
	/**
	 * @title  toView
	 * @description 客户管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("shop:customer:view")
	public String toView() {
		return "shop_customer/list";
	}
	
	/**
	 * @title  list
	 * @description 查询客户列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("shop:customer:list")
	public IReturnBean<List<ShopCustomer>> list(HttpServletRequest request, ShopCustomer query, LayPage page) {
		try {
			PageInfo<ShopCustomer> result = shopCustomerService.allOfPage(query, page);
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询客户列表 error:", e);
			return IReturnBean.error();
		}
	}

}
