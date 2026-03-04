package org.dewdrop.steamhelper.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.ResGame;
import org.dewdrop.steamhelper.constant.CommonConstant;
import org.dewdrop.steamhelper.util.util.JsonUtil;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.service.ResGameClfService;
import org.dewdrop.steamhelper.web.service.ResGameService;
import org.dewdrop.steamhelper.web.service.SysDictUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ResGameController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/29 16:02
 */
@Slf4j
@Controller
@RequestMapping("game")
public class ResGameController extends BasicController {
	
	@Resource
	private ResGameService resGameService;
	@Resource
	private ResGameClfService resGameClfService;

	/**
	 * @title  toView
	 * @description 游戏管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("res:game:view")
	public String toView(HttpServletRequest request) {
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		request.setAttribute("seriesMap", JsonUtil.toJSONString(resGameClfService.getSeries()));
		request.setAttribute("achv", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_ACHV_STATUS));
		request.setAttribute("iconPrefix", CommonConstant.URL_STEAM_ICON);
		return "res_game/list";
	}
	
	/**
	 * @title  auto
	 * @description 自动刷新所有游戏
	 * @param request
	 * @param platformId
	 * @return
	 */
	@RequestMapping(value = "/auto.do")
	@ResponseBody
	@RequiresPermissions("res:game:auto")
	public IReturnBean<?> auto(HttpServletRequest request, Integer platformId) {
		try {
			resGameService.auto(getCurrentLoginId(), platformId);
		} catch (Exception e) {
			log.error("refresh game error:", e);
			return IReturnBean.error();
		}
		return IReturnBean.fail();
	}
	
	/**
	 * @title  list
	 * @description 查询游戏列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("res:game:list")
	public IReturnBean<List<ResGame>> list(HttpServletRequest request, ResGame query, LayPage page) {
		try {
			PageInfo<ResGame> result = resGameService.allOfPage(query, page);
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询游戏列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增游戏页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("res:game:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		return "res_game/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增游戏
	 * @param request
	 * @param game
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("res:game:add")
	public IReturnBean<?> add(HttpServletRequest request, ResGame game) {
		try {
			return resGameService.saveOrUpd(game, true);
		} catch (Exception e) {
			log.error("add game error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改游戏页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upd")
	@RequiresPermissions("res:game:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		ResGame game = resGameService.selectByPrimaryKey(id);
		request.setAttribute("game", game);
		request.setAttribute("platforms", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_PLATFORM));
		request.setAttribute("pageFlag", "upd");
		return "res_game/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改游戏
	 * @param request
	 * @param game
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions("res:game:upd")
	public IReturnBean<?> upd(HttpServletRequest request, ResGame game) {
		try {
			return resGameService.saveOrUpd(game, false);
		} catch (Exception e) {
			log.error("upd game error:", e);
			return IReturnBean.error();
		}
	}
}
