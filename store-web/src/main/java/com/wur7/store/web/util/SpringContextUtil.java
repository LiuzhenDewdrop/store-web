package com.wur7.store.web.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @class:  SpringContextUtil
 * @description:
 * @author: L.zhen
 * @date:   2025/12/17 16:08
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(SpringContextUtil.applicationContext == null) {
			SpringContextUtil.applicationContext = applicationContext;
		}
	}
	
	public static ApplicationContext getApplicationContext() {
		return SpringContextUtil.applicationContext;
	}
	
	public static <T> T getBean(String name,Class<T> clazz) {
		return (T) applicationContext.getBean(name,clazz);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return (T) applicationContext.getBean(clazz);
	}
	
}

