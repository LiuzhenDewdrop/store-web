package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.SysSwiper;
import com.wur7.store.web.service.ShopItemService;
import com.wur7.store.web.service.SysSwiperService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/wx/sys")
public class WxSysController extends BasicController {
	
	@Resource
	private SysSwiperService sysSwiperService;

	@Resource
	private ShopItemService shopItemService;
	
	/**
	 * @title  swiper
	 * @description 查询swiper列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/swiper")
	public IReturnBean<List<SysSwiper>> swiper(HttpServletRequest request) {
		try {
			return sysSwiperService.listForApp();
		} catch (Exception e) {
			log.error("查询一级分类列表 error:", e);
			return IReturnBean.error();
		}
	}
}
