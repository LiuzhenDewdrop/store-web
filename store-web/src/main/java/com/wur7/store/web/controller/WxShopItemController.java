package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopItem;
import com.wur7.store.web.bean.ShopItemDetail;
import com.wur7.store.web.bean.request.AppQuery;
import com.wur7.store.web.service.ShopItemService;
import com.wur7.store.web.service.SysMenuService;
import com.wur7.store.web.service.SysRoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/wx/item")
public class WxShopItemController extends BasicController {
	
	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private SysMenuService sysMenuService;
	@Resource
	private ShopItemService shopItemService;
	
	/**
	 * @title  query
	 * @description 查询商品列表
	 * @param request
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/query")
	public IReturnBean<List<ShopItemDetail>> query(HttpServletRequest request,@RequestBody AppQuery query) {
		try {
			return shopItemService.queryForApp(query);
		} catch (Exception e) {
			log.error("查询商品列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  detail
	 * @description 查询商品详情
	 * @param request
	 * @param item
	 * @return
	 */
	@RequestMapping(value = "/detail")
	public IReturnBean<ShopItemDetail> detail(HttpServletRequest request, @RequestBody ShopItem item) {
		try {
			if (item == null || item.getId() == null) {
				return IReturnBean.fail("查不到此商品");
			}
			return shopItemService.detailForApp(item.getId());
		} catch (Exception e) {
			log.error("查询商品详情 error:", e);
			return IReturnBean.error();
		}
	}
	

	
	/**
	 *  上传图片
	 */
	@RequestMapping(value = "/upload.do")
	@ResponseBody
	@RequiresPermissions(value={"shop:item:add", "shop:item:upd"}, logical = Logical.OR)
	public IReturnBean<String> upload(HttpServletRequest request, MultipartFile file) {
		try{
			return shopItemService.uploadImage(file);
		} catch (Exception e) {
			log.error("upload error:"+e);
			return IReturnBean.error();
		}
	}
	


	

}
