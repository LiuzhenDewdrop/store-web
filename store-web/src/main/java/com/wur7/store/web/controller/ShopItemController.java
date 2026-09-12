package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopItem;
import com.wur7.store.entity.SysRole;
import com.wur7.store.util.util.JsonUtil;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.bean.response.SysMenuResp;
import com.wur7.store.web.service.ShopItemService;
import com.wur7.store.web.service.SysMenuService;
import com.wur7.store.web.service.SysRoleService;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ShopItemController
 * @description: 
 * @author: L.zhen
 * @date:    16:37
 */
@Slf4j
@Controller
@RequestMapping("item")
public class ShopItemController extends BasicController {
	
	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private SysMenuService sysMenuService;
	@Resource
	private ShopItemService shopItemService;
	
	/**
	 * @title  toView
	 * @description 商品管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("shop:item:view")
	public String toView() {
		return "shop_item/list";
	}
	
	/**
	 * @title  list
	 * @description 查询商品列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("shop:item:list")
	public IReturnBean<List<ShopItem>> list(HttpServletRequest request, ShopItem query, LayPage page) {
		try {
			PageInfo<ShopItem> result = shopItemService.allOfPage(query, page);
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询商品列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增商品页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("shop:item:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		return "shop_item/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增商品
	 * @param request
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("shop:item:add")
	public IReturnBean<?> add(HttpServletRequest request, SysRole role) {
		try {
			return sysRoleService.saveOrUpd(role, true, getCurrentUser());
		} catch (Exception e) {
			log.error("add role error:"+e.getMessage());
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改商品页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upd")
	@RequiresPermissions("shop:item:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		SysRole role = sysRoleService.selectByPrimaryKey(id);
		request.setAttribute("role", role);
		request.setAttribute("pageFlag", "upd");
		return "shop_item/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改商品
	 * @param request
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions("shop:item:upd")
	public IReturnBean<?> upd(HttpServletRequest request, SysRole role) {
		try {
			return sysRoleService.saveOrUpd(role, false, getCurrentUser());
		} catch (Exception e) {
			log.error("upd role error:"+e.getMessage());
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除商品
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	@RequiresPermissions("shop:item:del")
	public IReturnBean<?> del(HttpServletRequest request, Integer id) {
		try {
			return sysRoleService.del(id);
		} catch (Exception e) {
			log.error("del role error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toGrant
	 * @description 跳转到商品授权页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/grant")
	@RequiresPermissions("shop:item:grant")
	public String toGrant(HttpServletRequest request, Integer id) {
		List<SysMenuResp> tree = sysMenuService.getTree(id);
		request.setAttribute("tree", JsonUtil.toJSONString(tree));
		request.setAttribute("roleId", id);
		return "shop_item/grant";
	}
	
	/**
	 * @title  grant
	 * @description 授权
	 * @param request
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	@RequestMapping(value = "/grant.do")
	@ResponseBody
	@RequiresPermissions("shop:item:grant")
	public IReturnBean<?> grant(HttpServletRequest request, @RequestParam("roleId") Integer roleId, @RequestParam("menuIds[]") List<Integer> menuIds) {
		try {
			return sysRoleService.grant(roleId, menuIds, getCurrentUser());
		} catch (Exception e) {
			log.error("del role error:", e);
			return IReturnBean.error();
		}
	}
}
