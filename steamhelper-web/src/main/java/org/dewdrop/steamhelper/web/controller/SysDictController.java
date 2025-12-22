package org.dewdrop.steamhelper.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.SysDict;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.service.SysDictService;
import org.dewdrop.steamhelper.web.service.SysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  SysDictController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/22 19:06
 */
@Slf4j
@Controller
@RequestMapping("dict")
public class SysDictController extends BasicController {
	
	@Resource
	private SysDictService sysDictService;
	@Resource
	private SysMenuService sysMenuService;
	
	/**
	 * @title  toList
	 * @description 字典管理-跳转
	 * @return
	 */
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "/list")
	public String toList() {
		return "sys_dict/list";
	}
	
	/**
	 * @title  list
	 * @description 查询字典列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("sys:dict:view")
	public IReturnBean<List<SysDict>> list(HttpServletRequest request, SysDict query, LayPage page) {
		try {
			PageInfo<SysDict> result = sysDictService.allOfPage(query, page);
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询字典列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增字典页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("sys:dict:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		return "sys_dict/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增字典
	 * @param request
	 * @param dict
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("sys:dict:add")
	public IReturnBean<?> add(HttpServletRequest request, SysDict dict) {
		try {
			return sysDictService.saveOrUpd(dict, true);
		} catch (Exception e) {
			log.error("add dict error:"+e.getMessage());
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改字典页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upd")
	@RequiresPermissions("sys:dict:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		SysDict dict = sysDictService.selectByPrimaryKey(id);
		request.setAttribute("dict", dict);
		request.setAttribute("pageFlag", "upd");
		return "sys_dict/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改字典
	 * @param request
	 * @param dict
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions("sys:dict:upd")
	public IReturnBean<?> upd(HttpServletRequest request, SysDict dict) {
		try {
			return sysDictService.saveOrUpd(dict, false);
		} catch (Exception e) {
			log.error("upd dict error:"+e.getMessage());
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除字典
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	@RequiresPermissions("sys:dict:del")
	public IReturnBean<?> del(HttpServletRequest request, Integer id) {
		try {
			return sysDictService.del(id);
		} catch (Exception e) {
			log.error("del dict error:", e);
			return IReturnBean.error();
		}
	}
}
