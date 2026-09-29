package com.wur7.store.web.bean;

import lombok.Data;

@Data
public class WxLoginResponse {
    private String token; // 登录凭证，后续请求放header里
    private Integer userId;
    private String nickname;
    private String userName; // 昵称别名
    private String avatar;
    private String avatarUrl; // 头像别名，和前端字段对应
    private String phone;
    private String phoneNumber; // 手机号别名
    private Integer gender;
    
    // 兼容前端字段：设置avatar同时设置avatarUrl
    public void setAvatar(String avatar) {
    	this.avatar = avatar;
    	this.avatarUrl = avatar;
    }
    
    // 兼容前端字段：设置phone同时设置phoneNumber和userName
    public void setPhone(String phone) {
    	this.phone = phone;
    	this.phoneNumber = phone;
    }
    
    // 兼容前端字段：设置nickname同时设置userName
    public void setNickname(String nickname) {
    	this.nickname = nickname;
    	this.userName = nickname;
    }
}
