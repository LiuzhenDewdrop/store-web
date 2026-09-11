package com.wur7.store.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ResAccount;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.web.bean.LayPage;
import com.wur7.store.web.service.ResAccountService;
import com.wur7.store.web.service.SysDictUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ResAccountController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/22 20:21
 */
@Slf4j
@Controller
@RequestMapping("acc")
public class ResAccountController extends BasicController {
	
	@Resource
	private ResAccountService resAccountService;

	
	/**
	 * @title  toView
	 * @description 账号管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("res:acc:view")
	public String toView(HttpServletRequest request) {
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		return "res_acc/list";
	}
	
	/**
	 * @title  list
	 * @description 查询账号列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("res:acc:list")
	public IReturnBean<List<ResAccount>> list(HttpServletRequest request, ResAccount query, LayPage page) {
		try {
			PageInfo<ResAccount> result = resAccountService.allOfPage(query, page, getCurrentUser());
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询账号列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增账号页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("res:acc:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		return "res_acc/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增账号
	 * @param request
	 * @param acc
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("res:acc:add")
	public IReturnBean<?> add(HttpServletRequest request, ResAccount acc) {
		try {
			acc.setUserId(getCurrentLoginId());
			return resAccountService.saveOrUpd(acc, true);
		} catch (Exception e) {
			log.error("add acc error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改账号页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upd")
	@RequiresPermissions("res:acc:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		ResAccount acc = resAccountService.selectByPrimaryKey(id);
		request.setAttribute("acc", acc);
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		request.setAttribute("pageFlag", "upd");
		return "res_acc/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改账号
	 * @param request
	 * @param acc
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions("res:acc:upd")
	public IReturnBean<?> upd(HttpServletRequest request, ResAccount acc) {
		try {
			return resAccountService.saveOrUpd(acc, false);
		} catch (Exception e) {
			log.error("upd acc error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除账号
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	@RequiresPermissions("res:acc:del")
	public IReturnBean<?> del(HttpServletRequest request, Integer id) {
		try {
			resAccountService.del(id);
		} catch (Exception e) {
			log.error("del acc error:", e);
			return IReturnBean.error();
		}
		return IReturnBean.fail();
	}
}
