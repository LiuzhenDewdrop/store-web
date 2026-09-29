package com.wur7.store.web.bean;

import lombok.Data;

@Data
public class WxLoginRequest {
    private String code; // wx.login()获取的code
    private String encryptedData; // 手机号加密数据
    private String iv; // 手机号加密向量
    private String userInfoRaw; // 用户信息JSON字符串（昵称头像）
}
