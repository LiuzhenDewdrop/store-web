package com.wur7.store.web.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.util.util.Base64;
import com.wur7.store.web.bean.WxLoginRequest;
import com.wur7.store.web.bean.WxLoginResponse;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class WxLoginService {

    @Autowired
    private MapperSupport mapperSupport;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    // 内存缓存token
    private static final Map<String, Integer> TOKEN_CACHE = new HashMap<>();

    public WxLoginResponse login(WxLoginRequest request) throws Exception {
		// 用code请求微信接口获取openid
		String wxUrl = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
				appid, secret, request.getCode());
		RestTemplate restTemplate = new RestTemplate();
		String wxResult = restTemplate.getForObject(wxUrl, String.class);
		JSONObject wxJson = JSON.parseObject(wxResult);
		String openid = wxJson.getString("openid");
		if (openid == null) {
			throw new RuntimeException("微信登录失败：" + wxResult);
		}

        // 查找用户是否已存在
        ShopCustomer customer = mapperSupport.shopCustomerMapper.selectByOpenid(openid);

        boolean isNew = false;
        if (customer == null) {
            // 新用户自动注册
            isNew = true;
            customer = new ShopCustomer();
            customer.setOpenid(openid);
            customer.setCreateTime(new Date());
            customer.setStatus(1);
            customer.setGender(0);
        }

        // 解析用户昵称头像
        if (request.getUserInfoRaw() != null) {
            JSONObject userInfo = JSON.parseObject(request.getUserInfoRaw());
            customer.setNickname(userInfo.getString("nickName"));
            customer.setAvatar(userInfo.getString("avatarUrl"));
            customer.setGender(userInfo.getInteger("gender"));
        }

        // 解析手机号
        if (request.getEncryptedData() != null && request.getIv() != null) {
            String sessionKey = "dev_session_key"; // todo 正式环境从微信接口返回拿
            String phoneJson = decryptWxData(request.getEncryptedData(), sessionKey, request.getIv());
            JSONObject phoneObj = JSON.parseObject(phoneJson);
            customer.setPhone(phoneObj.getString("phoneNumber"));
        }

        if (isNew) {
        	customer.setCreateTime(new Date());
            mapperSupport.shopCustomerMapper.insert(customer);
        }

        // 生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKEN_CACHE.put(token, customer.getId());

        // 返回登录信息
        WxLoginResponse response = new WxLoginResponse();
        response.setToken(token);
        response.setUserId(customer.getId());
        response.setNickname(customer.getNickname());
        response.setAvatar(customer.getAvatar());
        response.setPhone(customer.getPhone());
        response.setGender(customer.getGender());
        return response;
    }

    public ShopCustomer getCustomerByToken(String token) {
        Integer userId = TOKEN_CACHE.get(token);
        if (userId == null) return null;
        return mapperSupport.shopCustomerMapper.selectByPrimaryKey(userId);
    }

    public void logout(String token) {
        TOKEN_CACHE.remove(token);
    }

    public ShopCustomer updateCustomerInfo(Integer userId, String nickname, String avatar, String phone) {
        ShopCustomer customer = mapperSupport.shopCustomerMapper.selectByPrimaryKey(userId);
        if (customer == null) return null;
        if (nickname != null) customer.setNickname(nickname);
        if (avatar != null) customer.setAvatar(avatar);
        if (phone != null) customer.setPhone(phone);
        customer.setUpdateTime(new Date());
        mapperSupport.shopCustomerMapper.updateByPrimaryKey(customer);
        return customer;
    }

    // 微信数据解密方法
    private String decryptWxData(String encryptedData, String sessionKey, String iv) throws Exception {
        Base64 base64 = new Base64();
        byte[] encryptedByte = base64.decode(encryptedData);
        byte[] keyByte = base64.decode(sessionKey);
        byte[] ivByte = base64.decode(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivByte);
        cipher.init(Cipher.DECRYPT_MODE, spec, ivSpec);
        byte[] resultByte = cipher.doFinal(encryptedByte);
        return new String(resultByte, StandardCharsets.UTF_8);
    }
}
