package org.dewdrop.steamhelper.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.dewdrop.steamhelper.bean.IReturnBean;
import org.dewdrop.steamhelper.entity.SysUser;
import org.dewdrop.steamhelper.util.util.MD5Util;
import org.dewdrop.steamhelper.util.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController extends BasicController {

	@RequestMapping("/index")
	public String toIndexPage() {
		return "main/index";
	}

	@RequestMapping("/home")
	public String toHomePage() {
		return "main/home";
	}

	@RequestMapping("/login")
	public String toLoginPage() {
		return "login";
	}

	@RequestMapping("/")
	public String toPage()  {
		return "login";
	}

	@RequestMapping("/unauthorized")
	public String toUnauthorizedPage() {
		return "error/unauthorized";
	}

	@RequestMapping("/login.do")
	@ResponseBody
	public IReturnBean<?> login(HttpServletRequest request, String loginName, String password){
		log.info("login loginName:" + loginName);
		IReturnBean<?> returnBean = new IReturnBean<>(IReturnBean.SUCCESS_CODE,IReturnBean.SUCCESS_DESC);
		long start = System.currentTimeMillis();
		try {
			if (StringUtil.isEmpty(loginName)) {
				log.error("登录验证失败,原因:用户名不能为空");
				return IReturnBean.fail("用户名不能为空");
			}
			if (StringUtil.isEmpty(password)) {
				log.error("登录验证失败,原因:密码不能为空");
				return new IReturnBean<>(IReturnBean.FAIL_CODE,"密码不能为空");
			}
			UsernamePasswordToken token = new UsernamePasswordToken(loginName, MD5Util.md5(password));
			token.setRememberMe(true);
			SecurityUtils.getSubject().getSession().setTimeout(1000L*60*60*12);
			Subject currentUser = SecurityUtils.getSubject();
	
			currentUser.login(token);
			if (currentUser.isAuthenticated()) {
				request.getSession().setAttribute("LOGIN_USER" ,getCurrentUser());
			}else{
				returnBean = new IReturnBean<>(IReturnBean.FAIL_CODE,"用户名或密码不匹配");
			}
		} catch (IncorrectCredentialsException | AccountException ice) {
			log.error("登录验证失败,原因:用户名或密码不匹配");
			returnBean = new IReturnBean<>(IReturnBean.FAIL_CODE,"用户名或密码不匹配");
		} catch (Exception e) {
			log.error("登录验证失败,原因:系统登录异常", e);
			returnBean = new IReturnBean<>(IReturnBean.FAIL_CODE,"系统登录异常");
		} finally {
			log.info("登录验证处理结束,用时" + (System.currentTimeMillis() - start) + "毫秒");
		}
		return returnBean;
	}
	
	@RequestMapping(value = "/logout", produces = "text/html; charset=UTF-8")
	public String logout(){
		Subject currentUser = SecurityUtils.getSubject();
		log.info("logout currentUser:"+currentUser.getPrincipals().oneByType(SysUser.class).getUserName());
		currentUser.logout();
		return "login";
	}
	
	public static void main(String[] args) {
		System.out.println(MD5Util.md5("admin"));
	}

}
