package com.wur7.store.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.web.bean.WxLoginRequest;
import com.wur7.store.web.bean.WxLoginResponse;
import com.wur7.store.web.service.WxLoginService;

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
    @PostMapping("/logout")
    public IReturnBean logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        customerService.logout(token);
        return IReturnBean.success();
    }

    // 更新用户信息（修改昵称、头像、手机号）
    @PostMapping("/update")
    public IReturnBean<ShopCustomer> updateInfo(@RequestBody ShopCustomer customer, HttpServletRequest request) {
        String token = request.getHeader("token");
        ShopCustomer currentUser = customerService.getCustomerByToken(token);
        if (currentUser == null) {
            return IReturnBean.fail("未登录");
        }
        ShopCustomer updated = customerService.updateCustomerInfo(currentUser.getId(),
                customer.getNickname(), customer.getAvatar(), customer.getPhone());
        return IReturnBean.success(updated);
    }
}
