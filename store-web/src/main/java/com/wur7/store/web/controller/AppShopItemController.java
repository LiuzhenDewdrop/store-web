package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.SysRole;
import com.wur7.store.util.util.JsonUtil;
import com.wur7.store.util.util.StringUtil;
import com.wur7.store.web.bean.ShopItemDetail;
import com.wur7.store.web.bean.request.AppQuery;
import com.wur7.store.web.bean.response.SysMenuResp;
import com.wur7.store.web.service.ShopItemService;
import com.wur7.store.web.service.SysMenuService;
import com.wur7.store.web.service.SysRoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/app/item")
public class AppShopItemController extends BasicController {
	
	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private SysMenuService sysMenuService;
	@Resource
	private ShopItemService shopItemService;
	
	/**
	 * @title  list
	 * @description 查询商品列表
	 * @param request
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public IReturnBean<List<ShopItemDetail>> list(HttpServletRequest request,@RequestBody AppQuery query) {
		try {
			return shopItemService.queryForApp(query);
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
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		return "shop_item/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增商品
	 * @param request
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("shop:item:add")
	public IReturnBean<?> add(HttpServletRequest request, @RequestBody ShopItemDetail detail) {
		try {
			return shopItemService.saveOrUpd(detail, true, getCurrentUser());
		} catch (Exception e) {
			log.error("add ShopItemDetail error:"+e.getMessage());
			return IReturnBean.error();
		}
	}
	
	/**
	 *  上传图片
	 */
	@RequestMapping(value = "/upload.do")
	@ResponseBody
	@RequiresPermissions(value={"shop:item:add", "shop:item:upd"}, logical = Logical.OR)
	public IReturnBean<String> upload(HttpServletRequest request, MultipartFile file, String path) {
		try{
			if (StringUtil.isBlank(path)) {
				path = "";
			}
			return shopItemService.uploadImage(file, path);
		} catch (Exception e) {
			log.error("upload error:"+e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toSpec
	 * @description 跳转到新增商品页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/spec")
	@RequiresPermissions(value={"shop:item:add", "shop:item:upd"}, logical = Logical.OR)
	public String toSpec(HttpServletRequest request) {
		return "shop_item/spec";
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
