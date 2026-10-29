package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.SysSwiper;
import com.wur7.store.web.service.SysSwiperService;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  SysSwiperController
 * @description: 
 * @author: L.zhen
 */
@Slf4j
@Controller
@RequestMapping("swiper")
public class SysSwiperController extends BasicController {
	
	@Resource
	private SysSwiperService sysSwiperService;
	
	/**
	 * @title  toView
	 * @description 跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("sys:swiper:view")
	public String toView() {
		return "sys_swiper/list";
	}
	
	/**
	 * @title  list
	 * @description 查询列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("sys:swiper:list")
	public IReturnBean<List<SysSwiper>> list(HttpServletRequest request) {
		try {
			return sysSwiperService.list();
		} catch (Exception e) {
			log.error("查询swiper列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 *  上传图片
	 */
	@RequestMapping(value = "/upload.do")
	@ResponseBody
	@RequiresPermissions(value="sys:swiper:upd")
	public IReturnBean<String> upload(HttpServletRequest request, MultipartFile file) {
		try{
			return sysSwiperService.uploadImage(file);
		} catch (Exception e) {
			log.error("upload error:"+e);
			return IReturnBean.error();
		}
	}

	/**
	 * @title  upd
	 * @description 修改swiper(包括新增、修改、删除、排序等)
	 * @param request
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions(value="sys:swiper:upd")
	public IReturnBean<?> upd(HttpServletRequest request, List<SysSwiper> list) {
		try {
			return sysSwiperService.update(list);
		} catch (Exception e) {
			log.error("upd swiper error:"+e.getMessage());
			return IReturnBean.error();
		}
	}
}
