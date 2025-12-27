package org.dewdrop.steamhelper.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResGameClassification;
import org.dewdrop.steamhelper.web.service.ResClassificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ResClassificationController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/26 19:00
 */
@Slf4j
@Controller
@RequestMapping("clf")
public class ResClassificationController extends BasicController {
	
	@Resource
	private ResClassificationService resClassificationService;

	
	/**
	 * @title  toList
	 * @description 游戏分类管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/list")
	@RequiresPermissions("res:clf:view")
	public String toList(HttpServletRequest request) {
		return "res_clf/list";
	}
	
	/**
	 * @title  list
	 * @description 查询游戏分类列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("res:clf:view")
	public IReturnBean<List<ResGameClassification>> list(HttpServletRequest request) {
		try {
			return IReturnBean.success(resClassificationService.getAll());
		} catch (Exception e) {
			log.error("查询游戏分类列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增游戏分类页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("res:clf:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		request.setAttribute("series", resClassificationService.getSeries());
		return "res_clf/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增游戏分类
	 * @param request
	 * @param clf
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("res:clf:add")
	public IReturnBean<?> add(HttpServletRequest request, ResGameClassification clf) {
		try {
			return resClassificationService.saveOrUpd(clf, true);
		} catch (Exception e) {
			log.error("add clf error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改游戏分类页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upd")
	@RequiresPermissions("res:clf:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		ResGameClassification clf = resClassificationService.selectByPrimaryKey(id);
		request.setAttribute("clf", clf);
		request.setAttribute("pageFlag", "upd");
		return "res_clf/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改游戏分类
	 * @param request
	 * @param clf
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions("res:clf:upd")
	public IReturnBean<?> upd(HttpServletRequest request, ResGameClassification clf) {
		try {
			return resClassificationService.saveOrUpd(clf, false);
		} catch (Exception e) {
			log.error("upd clf error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除游戏分类
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	@RequiresPermissions("res:clf:del")
	public IReturnBean<?> del(HttpServletRequest request, Integer id) {
		try {
			resClassificationService.del(id);
		} catch (Exception e) {
			log.error("del clf error:", e);
			return IReturnBean.error();
		}
		return IReturnBean.fail();
	}
}
