package com.wur7.store.bean;

import java.io.Serializable;

import com.wur7.store.enums.IReturnEnum;
import com.wur7.store.util.util.JsonUtil;

import lombok.Data;

/**
 * @class:  IReturnBean
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/18 11:27
 * @param <T>
 */
@Data
public class IReturnBean<T> implements Serializable {
	
	public static final String SUCCESS_CODE = "0000";
	public static final String SUCCESS_DESC = "操作成功";
	
	public static final String ERROR_CODE = "0001";
	public static final String ERROR_DESC = "服务器维护中，请稍后重试";
	
	public static final String FAIL_CODE = "0002";
	public static final String FAIL_DESC = "操作失败";
	
	private String code;
	private String msg;
	private Long count;
	private T data;
	
	public IReturnBean() {
		super();
	}
	
	public IReturnBean(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public IReturnBean(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public IReturnBean(String code, String msg, T data, Long count) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.count = count;
	}
	
	public IReturnBean(String code, String msg, T data, Integer count) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.count = Long.valueOf(count);
	}

	public static <T> IReturnBean<T> success(String msg, T data, Long count) {
		return new IReturnBean<T>(SUCCESS_CODE, null == msg ? SUCCESS_DESC : msg, data, count);
	}
	public static <T> IReturnBean<T> success(String msg, T data) {
		return success(msg, data, null);
	}
	public static <T> IReturnBean<T> success(Long count, T data) {
		return success(SUCCESS_DESC, data, count);
	}
	public static <T> IReturnBean<T> success(T data) {
		return success(SUCCESS_DESC, data);
	}
	public static <T> IReturnBean<T> success() {
		return success(null);
	}

	public static <T> IReturnBean<T> fail(String msg, T data) {
		return new IReturnBean<T>(FAIL_CODE, null == msg ? FAIL_DESC : msg, data);
	}
	public static <T> IReturnBean<T> fail(String msg) {
		return fail(msg, null);
	}
	public static <T> IReturnBean<T> fail() {
		return fail(null);
	}

	public static <T> IReturnBean<T> error(String msg, T data) {
		return new IReturnBean<T>(ERROR_CODE, null == msg ? ERROR_DESC : msg, data);
	}
	public static <T> IReturnBean<T> error(String msg) {
		return error(msg, null);
	}
	public static <T> IReturnBean<T> error() {
		return error(null);
	}
	
	public static <T> IReturnBean<T> get(IReturnEnum e) {
		return new IReturnBean<T>(e.getCode(), e.getMsg());
	}

	public String getCode() {
		if(code == null) {
			return "";
		}
		if("".equals(code.trim())) {
			return "";
		}
		return code;
	}
	
	public String getMsg() {
		if(msg == null) {
			return "";
		}
		if("".equals(msg.trim())) {
			return "";
		}
		if(msg.length() > 512) {
			return msg.substring(0, 512);
		}
		return msg;
	}
	
	public boolean isSuccess() {
		return SUCCESS_CODE.equals(this.code);
	}
	
	public boolean isNotSuccess() {
		return !isSuccess();
	}
	
	public String toJson() {
		return JsonUtil.toJson(this);
	}
	
}
