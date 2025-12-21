package org.dewdrop.steamhelper.web.config;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  ThreadPoolManager
 * @description: 线程池管理类
 * @author: L.zhen
 * @date:   2025/12/17 17:27
 */
@Component
@Slf4j
public class ThreadPoolManager implements ApplicationRunner {
    private static final Set<ExecutorService> executorServices = new CopyOnWriteArraySet();
	
	/**
	 * @title  register
	 * @description 注册线程池
	 * @param executorService
	 */
	public static void register(ExecutorService executorService){
        log.info("ThreadPoolManager register es={}",executorService);
        executorServices.add(executorService);
    }
	
	/**
	 * @title  destroy
	 * @description 销毁线程池
	 * @param executorService
	 * @throws InterruptedException
	 */
	public static void destroy(ExecutorService executorService) throws InterruptedException {
        if(executorService != null){
            if(!executorService.isShutdown()){
                executorService.shutdown();
                if(!executorService.awaitTermination(10, TimeUnit.SECONDS)){
                    executorService.shutdownNow();
                }
            }
        }
    }

    @Override
    public void run(ApplicationArguments args){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (ExecutorService es :executorServices) {
                try {
                    log.info("ThreadPoolManager destroy start es={}",es);
                    destroy(es);
                } catch (Exception ex) {
                    log.error("ThreadPoolManager destroy error es={}",es,ex);
                }
            }
        }));
    }
}
