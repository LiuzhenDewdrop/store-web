package com.wur7.store.web.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @class:  AsyncConfig
 * @description:
 * @author: L.zhen
 * @date:   2025/12/17 16:16
 */
@Configuration
@EnableAsync
public class AsyncConfig {
	
	@Value("${store.web.async.corePoolSize}")
	private Integer corePoolSize;
	@Value("${store.web.async.maxPoolSize}")
	private Integer maxPoolSize;
	@Value("${store.web.async.keepAliveTime}")
	private Integer keepAliveTime;
	@Value("${store.web.async.threadNamePrefix}")
	private String threadNamePrefix;
	@Value("${store.web.async.queueCapacity}")
	private Integer queueCapacity;
	
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
