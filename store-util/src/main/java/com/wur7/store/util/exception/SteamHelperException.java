package com.wur7.store.util.exception;

/**
 * @class:  SteamHelperException
 * @description: 自定义运行时异常
 * @author: L.zhen
 * @date:   2025/12/17 17:22
 */
public class SteamHelperException extends RuntimeException{
    
    private String code;		// 错误码
    private String msg;			// 错误描述

    public SteamHelperException(String code, String message) {
    	this.code = code;
    	this.msg = message;
    }
    

    public SteamHelperException(String message) {
        this.msg = message;
    }

    public SteamHelperException(Throwable cause) {
        super(cause);
    }

    public SteamHelperException(String message, Throwable cause) {
        super(message, cause);
    }


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	@Override
	public String getMessage() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
