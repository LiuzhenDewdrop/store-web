package org.dewdrop.steamhelper.web.config;

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
	
	@Value("${steamhelper.web.async.corePoolSize}")
	private Integer corePoolSize;
	@Value("${steamhelper.web.async.maxPoolSize}")
	private Integer maxPoolSize;
	@Value("${steamhelper.web.async.keepAliveTime}")
	private Integer keepAliveTime;
	@Value("${steamhelper.web.async.threadNamePrefix}")
	private String threadNamePrefix;
	@Value("${steamhelper.web.async.queueCapacity}")
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
