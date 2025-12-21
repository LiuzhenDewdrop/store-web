package org.dewdrop.steamhelper.web.config;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.dewdrop.steamhelper.entity.SysMenu;
import org.dewdrop.steamhelper.entity.SysUser;
import org.dewdrop.steamhelper.util.util.CollectionUtil;
import org.dewdrop.steamhelper.util.util.StringUtil;
import org.dewdrop.steamhelper.web.service.SysMenuService;
import org.dewdrop.steamhelper.web.service.SysUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ShiroDbRealm
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/18 13:31
 */
@Slf4j
public class ShiroDbRealm extends AuthorizingRealm {
	
	@Resource
    private SysUserService sysUserService;
	@Resource
	private SysMenuService sysMenuService;
	
	/**
	 * @title  doGetAuthenticationInfo
	 * @description 获取认证信息
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		String username = userToken.getUsername();
		if (StringUtil.isEmpty(username)) {
			log.error("获取认证信息失败，原因:用户名为空");
			throw new AccountException("用户名为空");
		}
		// 查询登录用户
		SysUser user = sysUserService.findLoginUser(username);
		if (user == null) {
			throw new AccountException("用户信息为空");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
		log.info("用户认证通过:登录用户名:" + user.getUserName());
		return info;
	}
	
	/**
	 * @title  doGetAuthorizationInfo
	 * @description 获取授权信息
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException("Principal对象不能为空");
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		SysUser user = (SysUser) getAvailablePrincipal(principals);
		log.info("加载用户权限信息，当前登录用户名:" + user.getUserName());

		List<SysMenu> menus = sysMenuService.getListByRole(user.getRoleId());
		// 赋予用户菜单权限
		if (CollectionUtil.isNotEmpty(menus)) {
			for (SysMenu menu:menus) {
				info.addStringPermission(menu.getPermission());
			}
		}
		return info;
	}
	
	public void refresh() {
//		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
//		doGetAuthorizationInfo(principals);
		
//		RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
//		ShiroDbRealm userRealm = (ShiroDbRealm) securityManager.getRealms().iterator().next();
//		userRealm.refresh();
	}
	
	/**
	 * @title  clearAllCachedAuthorizationInfo
	 * @description 清除所有用户授权信息缓存
	 */
	public void clearAllCachedAuthorizationInfo() {
		log.info("清除所有账号缓存");
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null){
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
}
