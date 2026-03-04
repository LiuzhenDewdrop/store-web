package org.dewdrop.steamhelper.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResGameDlc;
import org.dewdrop.steamhelper.constant.CommonConstant;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.service.ResGameDlcService;
import org.dewdrop.steamhelper.web.service.SysDictUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ResGameDlcDlcController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/29 16:31
 */
@Slf4j
@Controller
@RequestMapping("dlc")
public class ResGameDlcController extends BasicController {
	
	@Resource
	private ResGameDlcService resGameDlcService;

	/**
	 * @title  toView
	 * @description 游戏DLC管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("res:dlc:view")
	public String toView(HttpServletRequest request) {
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		return "res_dlc/list";
	}
	
	/**
	 * @title  list
	 * @description 查询游戏DLC列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("res:dlc:list")
	public IReturnBean<List<ResGameDlc>> list(HttpServletRequest request, ResGameDlc query, LayPage page) {
		try {
			PageInfo<ResGameDlc> result = resGameDlcService.allOfPage(query, page);
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询游戏DLC列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增游戏DLC页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("res:dlc:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		return "res_dlc/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增游戏DLC
	 * @param request
	 * @param game
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("res:dlc:add")
	public IReturnBean<?> add(HttpServletRequest request, ResGameDlc game) {
		try {
			return resGameDlcService.saveOrUpd(game, true);
		} catch (Exception e) {
			log.error("add game error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改游戏DLC页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upd")
	@RequiresPermissions("res:dlc:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		ResGameDlc game = resGameDlcService.selectByPrimaryKey(id);
		request.setAttribute("game", game);
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		request.setAttribute("pageFlag", "upd");
		return "res_dlc/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改游戏DLC
	 * @param request
	 * @param game
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions("res:dlc:upd")
	public IReturnBean<?> upd(HttpServletRequest request, ResGameDlc game) {
		try {
			return resGameDlcService.saveOrUpd(game, false);
		} catch (Exception e) {
			log.error("upd game error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除游戏DLC
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	@RequiresPermissions("res:dlc:del")
	public IReturnBean<?> del(HttpServletRequest request, Integer id) {
		try {
			resGameDlcService.del(id);
		} catch (Exception e) {
			log.error("del game error:", e);
			return IReturnBean.error();
		}
		return IReturnBean.fail();
	}
}
