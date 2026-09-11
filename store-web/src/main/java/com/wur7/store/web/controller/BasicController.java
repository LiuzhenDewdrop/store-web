package com.wur7.store.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wur7.store.entity.SysUser;

/**
 * @class:  BasicController
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/18 12:14
 */
@Controller
@Scope("prototype")
public class BasicController {
	
	/**
	 * @title  getCurrentLoginName
	 * @description 获取登录用户名
	 * @return
	 */
	public String getCurrentLoginName() {
		return getCurrentUser().getLoginName();
	}
	
	/**
	 * @title  getCurrentLoginId
	 * @description 获取登录用户id
	 * @return
	 */
	public int getCurrentLoginId(){
		return getCurrentUser().getId();
	}
	
	public int getCurrentRoleId(){
		return getCurrentUser().getRoleId();
	}

	public SysUser getCurrentUser() {
		Subject currentUser = SecurityUtils.getSubject();
		return currentUser.getPrincipals().oneByType(SysUser.class);
	}

    

}
