package org.dewdrop.steamhelper.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.SysRole;
import org.dewdrop.steamhelper.util.util.JsonUtil;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.bean.response.SysMenuResp;
import org.dewdrop.steamhelper.web.service.SysMenuService;
import org.dewdrop.steamhelper.web.service.SysRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  SysRoleController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/19 17:36
 */
@Slf4j
@Controller
@RequestMapping("role")
public class SysRoleController extends BasicController {
	
	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private SysMenuService sysMenuService;
	
	/**
	 * @title  toList
	 * @description 角色管理-跳转
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "/toList.do", produces = "text/html; charset=UTF-8")
	public String toList() {
		return "sys_role/list";
	}
	
	/**
	 * @title  list
	 * @description 查询角色列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do", produces = "text/html; charset=UTF-8")
	@ResponseBody
	@RequiresPermissions("sys:role:view")
	public String list(HttpServletRequest request, SysRole query, LayPage page) {
		try {
			PageInfo<SysRole> result = sysRoleService.allOfPage(query, page);
			return IReturnBean.success(result.getTotal(), result.getList()).toJson();
		} catch (Exception e) {
			log.error("查询角色列表 error:", e);
			return IReturnBean.error().toJson();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增角色页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toAdd.do", produces = "text/html; charset=UTF-8")
	@RequiresPermissions("sys:role:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		return "sys_role/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增角色
	 * @param request
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/add.do", produces = "text/html; charset=UTF-8")
	@ResponseBody
	@RequiresPermissions("sys:role:add")
	public String add(HttpServletRequest request, SysRole role) {
		try {
			return sysRoleService.saveOrUpd(role, true, getCurrentUser()).toJson();
		} catch (Exception e) {
			log.error("add role error:"+e.getMessage());
			return IReturnBean.error().toJson();
		}
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改角色页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/toUpd.do", produces = "text/html; charset=UTF-8")
	@RequiresPermissions("sys:role:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		SysRole role = sysRoleService.selectByPrimaryKey(id);
		request.setAttribute("role", role);
		request.setAttribute("pageFlag", "upd");
		return "sys_role/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改角色
	 * @param request
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/upd.do", produces = "text/html; charset=UTF-8")
	@ResponseBody
	@RequiresPermissions("sys:role:upd")
	public String upd(HttpServletRequest request, SysRole role) {
		try {
			return sysRoleService.saveOrUpd(role, false, getCurrentUser()).toJson();
		} catch (Exception e) {
			log.error("upd role error:"+e.getMessage());
			return IReturnBean.error().toJson();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除角色
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del.do", produces = "text/html; charset=UTF-8")
	@ResponseBody
	@RequiresPermissions("sys:role:del")
	public String del(HttpServletRequest request, Integer id) {
		try {
			return sysRoleService.del(id).toJson();
		} catch (Exception e) {
			log.error("del role error:", e);
			return IReturnBean.error().toJson();
		}
	}
	
	/**
	 * @title  toGrant
	 * @description 跳转到角色授权页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/toGrant.do", produces = "text/html; charset=UTF-8")
	@RequiresPermissions("sys:role:grant")
	public String toGrant(HttpServletRequest request, Integer id) {
		List<SysMenuResp> tree = sysMenuService.getTree(id);
		request.setAttribute("tree", JsonUtil.toJSONString(tree));
		request.setAttribute("roleId", id);
		return "sys_role/grant";
	}
	
	/**
	 * @title  grant
	 * @description 授权
	 * @param request
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	@RequestMapping(value = "/grant.do", produces = "text/html; charset=UTF-8")
	@ResponseBody
	@RequiresPermissions("sys:role:grant")
	public IReturnBean grant(HttpServletRequest request, @RequestParam("roleId") Integer roleId, @RequestParam("menuIds[]") List<Integer> menuIds) {
		try {
			return sysRoleService.grant(roleId, menuIds, getCurrentUser());
		} catch (Exception e) {
			log.error("del role error:", e);
			return IReturnBean.error();
		}
	}
}
