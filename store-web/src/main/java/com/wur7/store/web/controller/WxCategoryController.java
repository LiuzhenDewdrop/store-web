package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.SysItemCategory;
import com.wur7.store.web.bean.response.CategoryDetailResp;
import com.wur7.store.web.service.ShopItemService;
import com.wur7.store.web.service.SysItemCategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/wx/category")
public class WxCategoryController extends BasicController {
	
	@Resource
	private SysItemCategoryService sysItemCategoryService;

	@Resource
	private ShopItemService shopItemService;
	
	/**
	 * @title  base
	 * @description 查询一级分类列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/base")
	public IReturnBean<List<SysItemCategory>> base(HttpServletRequest request) {
		try {
			return IReturnBean.success(sysItemCategoryService.findActivityBase());
		} catch (Exception e) {
			log.error("查询一级分类列表 error:", e);
			return IReturnBean.error();
		}
	}

	/**
	 * @title  detail
	 * @description 查询分类详情
	 * @param request
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/detail")
	public IReturnBean<List<CategoryDetailResp>> detail(HttpServletRequest request, @RequestBody SysItemCategory category) {
		try {
			if (category == null || category.getId() == null) {
				return IReturnBean.fail("查不到此分类");
			}
			return IReturnBean.success(sysItemCategoryService.findActivityDetail(category.getId()));
		} catch (Exception e) {
			log.error("查询分类详情 error:", e);
			return IReturnBean.error();
		}
	}
}
