package com.wur7.store.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Value("${imagePath.local:}")
	private String filePath;
	@Value("${imagePath.server:}")
	private String serverPath;
	
	@Value("${server.servlet.context-path:}")
	private String contextPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(serverPath + "**").addResourceLocations("file:"+ filePath);
	}
}
