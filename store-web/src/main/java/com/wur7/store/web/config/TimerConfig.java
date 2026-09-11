package com.wur7.store.web.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.wur7.store.constant.CommonConstant;
import com.wur7.store.entity.ScheduledTask;
import com.wur7.store.web.time.BaseScheduledTask;
import com.wur7.store.web.util.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @class:  TimerConfig
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/17 16:24
 */
@Component
@Slf4j
public class TimerConfig implements ApplicationRunner {
    private static final String OPEN_STANDARD = "yes";
    @Value("${store.web.timer.open}")
    private String open;
	
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private DataSource dataSource;

    /**
     * 存放所有启动定时任务对象
     */
    private static HashMap<String, ScheduledFuture<?>> scheduleMap = new HashMap<>();

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        return threadPoolTaskScheduler;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(!OPEN_STANDARD.equals(open)){
            return;
        }
        System.out.println("timer config start ...");
        List<ScheduledTask> tasks = new ArrayList<>();
        String sql = "select * from scheduled_task where able_status = '"+ CommonConstant.ENABLE+"'";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                ScheduledTask scheduledTask = new ScheduledTask(
                        result.getInt(1),
                        result.getInt(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getString(6),
                        result.getString(7),
                        result.getDate(8),
                        result.getDate(9));
                tasks.add(scheduledTask);
            }
        } catch (Exception e) {
            log.error("get scheduled_task table error,sql={}",sql,e);
            throw e;
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
		// 开启定时
        startCron(tasks);
        System.out.println("timer config complete timer.size="+scheduleMap.size());
    }

	/**
	 * @title  startCron
	 * @description 动态设置定时任务方法
	 * @param tasks
	 */
    public void startCron(List<ScheduledTask> tasks){
        // 遍历所有库中动态数据，根据库中class取出所属的定时任务对象进行关闭，每次都会把之前所有的定时任务都关闭，根据新的状态重新启用一次，达到最新配置
        for (ScheduledTask task : tasks){
            ScheduledFuture<?> scheduledFuture = scheduleMap.get(task.getCronKey());
            if (scheduledFuture != null){
                scheduledFuture.cancel(true);
            }
            if(CommonConstant.ENABLE.equals(task.getAbleStatus())){
                Class<?> clazz;
                Object job;
                try {
                    clazz = Class.forName(task.getCronKey());
                    try {
                        job = SpringContextUtil.getBean(clazz);
                    } catch (BeansException e) {
                        job = SpringContextUtil.getBean(task.getBeanName(),clazz);
                    }
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("scheduled_task class:" + task.getCronKey() + " is error", e);
                } catch (BeansException e) {
                    throw new IllegalArgumentException(task.getCronKey() +"   "+ task.getBeanName()+" not managed by Spring", e);
                }
                Assert.isAssignable(BaseScheduledTask.class, job.getClass(), "the timed task class must implement the ScheduledBaseTask interface");
				BaseScheduledTask taskThread = (BaseScheduledTask) job;
                ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(taskThread, new CronTrigger(task.getCronExpression()));
                scheduleMap.put(task.getCronKey(),future);
                log.info("定时任务-" + task.getBeanName() + "已开启");
            }
        }
    }
	
	/**
	 * @title  closeCron
	 * @description 关闭定时
	 * @param task
	 * @author L.zhen
	 * @date   2025/12/17 16:21
	 */
    public void closeCron(ScheduledTask task){
        ScheduledFuture<?> scheduledFuture = scheduleMap.get(task.getCronKey());
        if (scheduledFuture != null){
            scheduledFuture.cancel(true);
            log.info("定时任务-" + task.getBeanName() + "已关闭");
        }
    }
}
