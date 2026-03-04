package org.dewdrop.steamhelper.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.SysUser;
import org.dewdrop.steamhelper.constant.CommonConstant;
import org.dewdrop.steamhelper.web.bean.LayPage;
import org.dewdrop.steamhelper.web.service.SysDictUtil;
import org.dewdrop.steamhelper.web.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  SysUserController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/20 13:47
 */
@Slf4j
@Controller
@RequestMapping("user")
public class SysUserController extends BasicController {
	
	@Resource
	private SysUserService sysUserService;
	
	/**
	 * @title  toView
	 * @description 用户管理-跳转
	 * @return
	 */
	@RequestMapping(value = "/view")
	@RequiresPermissions("sys:user:view")
	public String toView() {
		return "sys_user/list";
	}
	
	/**
	 * @title  list
	 * @description 查询用户列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	@ResponseBody
	@RequiresPermissions("sys:user:list")
	public IReturnBean<List<SysUser>> list(HttpServletRequest request, SysUser query, LayPage page) {
		try {
			PageInfo<SysUser> result = sysUserService.allOfPage(query, page);
			return IReturnBean.success(result.getTotal(), result.getList());
		} catch (Exception e) {
			log.error("查询用户列表 error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toAdd
	 * @description 跳转到新增用户页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add")
	@RequiresPermissions("sys:user:add")
	public String toAdd(HttpServletRequest request) {
		request.setAttribute("pageFlag", "add");
		return "sys_user/addEdit";
	}
	
	/**
	 * @title  add
	 * @description 新增用户
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	@ResponseBody
	@RequiresPermissions("sys:user:add")
	public IReturnBean<?> add(HttpServletRequest request, SysUser user) {
		try {
			return sysUserService.saveOrUpd(user, true, getCurrentUser());
		} catch (Exception e) {
			log.error("add user error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toUpd
	 * @description 跳转到修改用户页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/upd")
	@RequiresPermissions("sys:user:upd")
	public String toUpd(HttpServletRequest request, Integer id) {
		SysUser user = sysUserService.selectByPrimaryKey(id);
		request.setAttribute("user", user);
		request.setAttribute("roles", SysDictUtil.getGroup(CommonConstant.DICT_GROUP_ROLE));
		request.setAttribute("pageFlag", "upd");
		return "sys_user/addEdit";
	}
	
	/**
	 * @title  upd
	 * @description 修改用户
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/upd.do")
	@ResponseBody
	@RequiresPermissions("sys:user:upd")
	public IReturnBean<?> upd(HttpServletRequest request, SysUser user) {
		try {
			user.setCreator(getCurrentLoginId());
			return sysUserService.saveOrUpd(user, false, getCurrentUser());
		} catch (Exception e) {
			log.error("upd user error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  del
	 * @description 删除用户
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/del.do")
	@ResponseBody
	@RequiresPermissions("sys:user:del")
	public IReturnBean<?> del(HttpServletRequest request, Integer id) {
		try {
			return sysUserService.del(id, getCurrentUser());
		} catch (Exception e) {
			log.error("del user error:", e);
			return IReturnBean.error();
		}
	}
	
	/**
	 * @title  toUpdPwd
	 * @description 修改密码跳转
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updPwd")
	public String toUpdPwd(HttpServletRequest request) {
		request.setAttribute("user" ,getCurrentUser());
		return "sys_user/updPwd";
	}
	
	/**
	 *  修改密码
	 */
	@RequestMapping(value = "/updPwd.do")
	@ResponseBody
	public IReturnBean<?> updPwd(HttpServletRequest request, SysUser user, String newPassword) {
		try{
			IReturnBean<?> result = sysUserService.updPwd(user, newPassword, getCurrentUser());
			if (result.isSuccess()) {
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.logout();
			}
			return result;
		} catch (Exception e) {
			log.error("updatePassword error:"+e);
			return IReturnBean.error();
		}
	}
	
	/**
	 *  修改头像
	 */
	@RequestMapping(value = "/updAvatar.do")
	@ResponseBody
	public IReturnBean<String> updAvatar(HttpServletRequest request, MultipartFile file) {
		try{
			return sysUserService.updAvatar(getCurrentUser(), file);
		} catch (Exception e) {
			log.error("updatePassword error:"+e);
			return IReturnBean.error();
		}
	}
}
