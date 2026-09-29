package com.wur7.store.web.bean;

import lombok.Data;

@Data
public class WxLoginResponse {
    private String token; // 登录凭证，后续请求放header里
    private Integer userId;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer gender;
}
