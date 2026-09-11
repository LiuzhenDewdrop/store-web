package com.wur7.store.web.exception;

import com.wur7.store.bean.IReturnBean;
import com.wur7.store.util.exception.SteamHelperException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  SteamhelperExceptionHandler
 * @description: 异常处理器
 * @author: L.zhen
 * @date:   2025/12/17 17:22
 */
@RestControllerAdvice
@Slf4j
public class SteamhelperExceptionHandler {
	
	/**
	 * @title  handleOpayException
	 * @description 处理自定义异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(SteamHelperException.class)
    public IReturnBean<?> handleException(SteamHelperException e){
        log.error(e.getMessage(),e);
        return IReturnBean.fail(e.getMessage());
    }
	
	/**
	 * @title  handleThrowable
	 * @description 兜底处理方法
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
    public IReturnBean<?> handleThrowable(Exception e){
        log.error("全局异常处理-",e);
        return IReturnBean.error("操作失败,"+e.getMessage());
    }
}
