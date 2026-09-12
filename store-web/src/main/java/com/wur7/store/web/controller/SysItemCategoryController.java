package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.SysItemCategory;
import com.wur7.store.web.service.SysItemCategoryService;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  SysItemCategoryController
 * @description: 
 * @author: L.zhen
 * @date:    16:56
 */
@Slf4j
@Controller
@RequestMapping("category")
public class SysItemCategoryController extends BasicController {

	@Resource
	private SysItemCategoryService sysItemCategoryService;
	
	/**
	 * @title  category
	 * @description 商品分类
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("shop:item:view")
	public IReturnBean<List<SysItemCategory>> category(Integer pId) {
		try {
			List<SysItemCategory> result = sysItemCategoryService.findByPid(pId);
			return IReturnBean.success(result);
		} catch (Exception e) {
			log.error("查询商品列表 error:", e);
			return IReturnBean.error();
		}
	}
}
