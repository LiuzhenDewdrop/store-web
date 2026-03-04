package org.dewdrop.steamhelper.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.SysMenu;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.bean.response.SysMenuResp;
import org.dewdrop.steamhelper.web.service.SysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  SysMenuController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/18 15:11
 */
@Slf4j
@Controller
@RequestMapping("menu")
public class SysMenuController extends BasicController {
	
	@Resource
	private SysMenuService sysMenuService;

	/**
	 * 该用户下的菜单列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/left.do")
	@ResponseBody
	public List<SysMenuResp> left(HttpServletRequest request) {
		log.info("getCurrentUser-----------------------{}",getCurrentUser());
		try {
			return sysMenuService.getLeft(getCurrentRoleId());
		} catch (Exception e) {
			log.error("获取左菜单栏 error:", e);
		}
		return null;
	}
	
	/**
	 * @title  toView
	 * @description 菜单管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("sys:menu:view")
	public String toView() {
		return "sys_menu/list";
	}
	
	/**
	 * @title  list
	 * @description 查询菜单列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("sys:menu:list")
	public IReturnBean<List<SysMenu>> list(HttpServletRequest request, SysMenu query, LayPage page) {
		try {
			PageInfo<SysMenu> result = sysMenuService.allOfPage(query, page);
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询菜单列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增菜单页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("sys:menu:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		return "sys_menu/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增菜单
	 * @param request
	 * @param menu
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("sys:menu:add")
	public IReturnBean<?> add(HttpServletRequest request, SysMenu menu) {
		try {
			menu.setCreator(getCurrentLoginId());
			boolean b = sysMenuService.saveOrUpd(menu, true);
			if (b) {
				return IReturnBean.success();
			}
		} catch (Exception e) {
			log.error("add menu error:", e);
			return IReturnBean.error();
		}
		return IReturnBean.fail();
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改菜单页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upd")
	@RequiresPermissions("sys:menu:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		SysMenu menu = sysMenuService.selectByPrimaryKey(id);
		request.setAttribute("menu", menu);
		request.setAttribute("pageFlag", "upd");
		return "sys_menu/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改菜单
	 * @param request
	 * @param menu
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions("sys:menu:upd")
	public IReturnBean<?> upd(HttpServletRequest request, SysMenu menu) {
		try {
			menu.setCreator(getCurrentLoginId());
			boolean b = sysMenuService.saveOrUpd(menu, false);
			if (b) {
				return IReturnBean.success();
			}
		} catch (Exception e) {
			log.error("upd menu error:", e);
			return IReturnBean.error();
		}
		return IReturnBean.fail();
	}
	
	/**
	 * @title  del
	 * @description 删除菜单
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	@RequiresPermissions("sys:menu:del")
	public IReturnBean<?> del(HttpServletRequest request, Integer id) {
		try {
			sysMenuService.del(id);
		} catch (Exception e) {
			log.error("del menu error:", e);
			return IReturnBean.error();
		}
		return IReturnBean.fail();
	}
	
	/**
	 * @title  findParentMenu
	 * @description 获取父级菜单
	 * @param level 当前level
	 * @return
	 */
	@RequestMapping(value = "/findParentMenu.do")
	@ResponseBody
	@RequiresPermissions("sys:menu:view")
	public IReturnBean<List<SysMenu>> findParentMenu(Integer level) {
		try{
			List<SysMenu> list = sysMenuService.getHigherLevel(level);
			return IReturnBean.success((long) list.size(), list);
		} catch (Exception e) {
			log.error("findParentMenu err", e);
		}
		return IReturnBean.fail();
	}
	
	/**
	 * @title  icon
	 * @description 获取图标
	 * @return
	 */
	@RequestMapping(value = "/icon")
	@RequiresPermissions("sys:menu:view")
	public String icon() {
		return "sys_menu/icon";
	}
}
