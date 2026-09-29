package com.wur7.store.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.web.bean.WxLoginRequest;
import com.wur7.store.web.bean.WxLoginResponse;
import com.wur7.store.web.service.WxLoginService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("wx")
public class WxLoginController {

    @Autowired
    private WxLoginService customerService;

    // 微信一键登录接口
    @PostMapping("/login")
    public IReturnBean<WxLoginResponse> login(@RequestBody WxLoginRequest request) {
        try {
            WxLoginResponse response = customerService.login(request);
            return IReturnBean.success(response);
        } catch (Exception e) {
        	log.error("wx login error", e);
            return IReturnBean.fail("登录失败：" + e.getMessage());
        }
    }

    // 获取当前登录用户信息
    @GetMapping("/info")
    public IReturnBean<ShopCustomer> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        ShopCustomer customer = customerService.getCustomerByToken(token);
        if (customer == null) {
            return IReturnBean.fail("未登录");
        }
        return IReturnBean.success(customer);
    }

    // 退出登录
    @GetMapping("/logout")
    public IReturnBean logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        customerService.logout(token);
        return IReturnBean.success();
    }
    
    @PostMapping("/logout")
    public IReturnBean logoutPost(HttpServletRequest request) {
    	return logout(request);
    }

    // 更新用户信息（修改昵称、性别）
    @PostMapping("/update")
    public IReturnBean<ShopCustomer> updateInfo(@RequestBody ShopCustomer customer, HttpServletRequest request) {
        String token = request.getHeader("token");
        ShopCustomer currentUser = customerService.getCustomerByToken(token);
        if (currentUser == null) {
            return IReturnBean.fail("未登录");
        }
        ShopCustomer updated = customerService.updateCustomerInfo(currentUser.getId(),
                customer.getNickname(), null, null, customer.getGender());
        return IReturnBean.success(updated);
    }
    
    // 上传用户头像
    @PostMapping("/uploadAvatar")
    public IReturnBean<String> uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    	try {
    		String token = request.getHeader("token");
    		ShopCustomer currentUser = customerService.getCustomerByToken(token);
    		if (currentUser == null) {
    			return IReturnBean.fail("未登录");
    		}
    		return customerService.updateAvatar(currentUser.getId(), file);
    	} catch (Exception e) {
    		log.error("upload avatar error", e);
    		return IReturnBean.fail("头像上传失败：" + e.getMessage());
    	}
    }
    
    // 通过微信code绑定手机号
    @PostMapping("/bindPhone")
    public IReturnBean<String> bindPhone(@RequestBody BindPhoneRequest req, HttpServletRequest request) {
    	try {
    		String token = request.getHeader("token");
    		ShopCustomer currentUser = customerService.getCustomerByToken(token);
    		if (currentUser == null) {
    			return IReturnBean.fail("未登录");
    		}
    		return customerService.bindPhoneByCode(currentUser.getId(), req.getCode());
    	} catch (Exception e) {
    		log.error("bind phone error", e);
    		return IReturnBean.fail("绑定手机号失败：" + e.getMessage());
    	}
    }
    
    // 手机号绑定请求参数
    public static class BindPhoneRequest {
    	private String code;
		public String getCode() { return code; }
		public void setCode(String code) { this.code = code; }
    }
}
