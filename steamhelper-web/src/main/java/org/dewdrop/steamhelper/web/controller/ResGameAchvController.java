package org.dewdrop.steamhelper.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResGameAchievement;
import org.dewdrop.steamhelper.constant.CommonConstant;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.service.ResGameAchvService;
import org.dewdrop.steamhelper.web.service.SysDictUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ResGameAchvController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/29 16:38
 */
@Slf4j
@Controller
@RequestMapping("achv")
public class ResGameAchvController extends BasicController {
	
	@Resource
	private ResGameAchvService resGameAchvService;

	/**
	 * @title  toView
	 * @description 游戏成就管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("res:achv:view")
	public String toView(HttpServletRequest request) {
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		return "res_achv/list";
	}
	
	/**
	 * @title  list
	 * @description 查询游戏成就列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("res:achv:list")
	public IReturnBean<List<ResGameAchievement>> list(HttpServletRequest request, ResGameAchievement query, LayPage page) {
		try {
			PageInfo<ResGameAchievement> result = resGameAchvService.allOfPage(query, page);
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询游戏成就列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增游戏成就页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("res:achv:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		return "res_achv/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增游戏成就
	 * @param request
	 * @param game
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("res:achv:add")
	public IReturnBean<?> add(HttpServletRequest request, ResGameAchievement game) {
		try {
			return resGameAchvService.saveOrUpd(game, true);
		} catch (Exception e) {
			log.error("add game error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改游戏成就页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upd")
	@RequiresPermissions("res:achv:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		ResGameAchievement game = resGameAchvService.selectByPrimaryKey(id);
		request.setAttribute("game", game);
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		request.setAttribute("pageFlag", "upd");
		return "res_achv/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改游戏成就
	 * @param request
	 * @param game
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions("res:achv:upd")
	public IReturnBean<?> upd(HttpServletRequest request, ResGameAchievement game) {
		try {
			return resGameAchvService.saveOrUpd(game, false);
		} catch (Exception e) {
			log.error("upd game error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除游戏成就
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	@RequiresPermissions("res:achv:del")
	public IReturnBean<?> del(HttpServletRequest request, Integer id) {
		try {
			resGameAchvService.del(id);
		} catch (Exception e) {
			log.error("del game error:", e);
			return IReturnBean.error();
		}
		return IReturnBean.fail();
	}
}
