package com.wur7.store.web.service;

import java.io.IOException;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wur7.store.bean.IReturnBean;
import com.wur7.store.entity.ShopCustomer;
import com.wur7.store.util.util.Base64;
import com.wur7.store.web.bean.WxLoginRequest;
import com.wur7.store.web.bean.WxLoginResponse;
import com.wur7.store.web.mapper.MapperSupport;

@Service
public class WxLoginService {

    @Autowired
    private MapperSupport mapperSupport;
    
    @Autowired
    private FileService fileService;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    // 内存缓存token -> userId
    private static final Map<String, Integer> TOKEN_CACHE = new HashMap<>();
    // 内存缓存userId -> sessionKey
    private static final Map<Integer, String> SESSION_KEY_CACHE = new HashMap<>();
    // access_token缓存
    private static String accessToken = null;
    private static long accessTokenExpireTime = 0;

    public WxLoginResponse login(WxLoginRequest request) throws Exception {
		// 用code请求微信接口获取openid和session_key
		String wxUrl = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
				appid, secret, request.getCode());
		RestTemplate restTemplate = new RestTemplate();
		String wxResult = restTemplate.getForObject(wxUrl, String.class);
		JSONObject wxJson = JSON.parseObject(wxResult);
		String openid = wxJson.getString("openid");
		String sessionKey = wxJson.getString("session_key");
		if (openid == null) {
			throw new RuntimeException("微信登录失败：" + wxResult);
		}

        // 查找用户是否已存在
        ShopCustomer customer = mapperSupport.shopCustomerMapper.selectByOpenid(openid);

        if (customer == null) {
            // 新用户自动注册
            customer = new ShopCustomer();
            customer.setOpenid(openid);
            customer.setCreateTime(new Date());
            customer.setStatus(1);
            customer.setGender(0);
        	customer.setCreateTime(new Date());
            mapperSupport.shopCustomerMapper.insert(customer);
        }
        
        // 缓存session_key
        if (sessionKey != null) {
        	SESSION_KEY_CACHE.put(customer.getId(), sessionKey);
        }

        // 生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKEN_CACHE.put(token, customer.getId());

        // 返回登录信息
        WxLoginResponse response = new WxLoginResponse();
        response.setToken(token);
        response.setUserId(customer.getId());
        response.setNickname(customer.getNickname());
        response.setAvatarUrl(customer.getAvatar());
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
        Integer userId = TOKEN_CACHE.remove(token);
        if (userId != null) {
        	SESSION_KEY_CACHE.remove(userId);
        }
    }

    /**
     * 更新用户信息（支持昵称、头像、手机号、性别）
     */
    public ShopCustomer updateCustomerInfo(Integer userId, String nickname, String avatar, String phone, Integer gender) {
        ShopCustomer customer = mapperSupport.shopCustomerMapper.selectByPrimaryKey(userId);
        if (customer == null) return null;
        ShopCustomer upd = new ShopCustomer();
        upd.setId(userId);
        if (nickname != null) upd.setNickname(nickname);
        if (avatar != null) upd.setAvatar(avatar);
        if (phone != null) upd.setPhone(phone);
        if (gender != null) upd.setGender(gender);
        upd.setUpdateTime(new Date());
        mapperSupport.shopCustomerMapper.updateByPrimaryKeySelective(upd);
        return mapperSupport.shopCustomerMapper.selectByPrimaryKey(userId);
    }
    
    /**
     * 更新用户头像
     */
    public IReturnBean<String> updateAvatar(Integer userId, MultipartFile file) throws IOException {
    	ShopCustomer customer = mapperSupport.shopCustomerMapper.selectByPrimaryKey(userId);
    	if (customer == null) {
    		return IReturnBean.fail("用户不存在");
    	}
    	IReturnBean<String> saveFile = fileService.saveFile("wx_avatar", true, file, null);
    	if (saveFile.isNotSuccess()) {
    		return saveFile;
    	}
    	String avatarUrl = saveFile.getData();
    	ShopCustomer upd = new ShopCustomer();
    	upd.setId(userId);
    	upd.setAvatar(avatarUrl);
    	upd.setUpdateTime(new Date());
    	mapperSupport.shopCustomerMapper.updateByPrimaryKeySelective(upd);
    	return IReturnBean.success(avatarUrl);
    }
    
    /**
     * 通过微信手机号code换取并绑定手机号
     */
    public IReturnBean<String> bindPhoneByCode(Integer userId, String phoneCode) throws Exception {
    	// 获取access_token
    	String accessToken = getAccessToken();
    	// 调用微信接口换取手机号
    	String url = String.format("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=%s", accessToken);
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	Map<String, String> param = new HashMap<>();
    	param.put("code", phoneCode);
    	HttpEntity<Map<String, String>> request = new HttpEntity<>(param, headers);
    	ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
    	String result = responseEntity.getBody();
    	JSONObject json = JSON.parseObject(result);
    	if (json.getInteger("errcode") != 0) {
    		return IReturnBean.fail("获取手机号失败：" + json.getString("errmsg"));
    	}
    	JSONObject phoneInfo = json.getJSONObject("phone_info");
    	String phoneNumber = phoneInfo.getString("phoneNumber");
    	// 更新数据库
    	ShopCustomer upd = new ShopCustomer();
    	upd.setId(userId);
    	upd.setPhone(phoneNumber);
    	upd.setUpdateTime(new Date());
    	mapperSupport.shopCustomerMapper.updateByPrimaryKeySelective(upd);
    	return IReturnBean.success(phoneNumber);
    }
    
    /**
     * 获取微信接口调用access_token
     */
    private String getAccessToken() {
    	long now = System.currentTimeMillis();
    	if (accessToken != null && now < accessTokenExpireTime) {
    		return accessToken;
    	}
    	String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appid, secret);
    	RestTemplate restTemplate = new RestTemplate();
    	String result = restTemplate.getForObject(url, String.class);
    	JSONObject json = JSON.parseObject(result);
    	accessToken = json.getString("access_token");
    	// 提前5分钟过期
    	accessTokenExpireTime = now + (json.getInteger("expires_in") - 300) * 1000;
    	return accessToken;
    }

    // 微信数据解密方法（保留备用）
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
