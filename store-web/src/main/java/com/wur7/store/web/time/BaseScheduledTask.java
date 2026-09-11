package com.wur7.store.web.time;

import java.util.Date;

import com.wur7.store.web.util.SpringContextUtil;
import com.wur7.store.entity.ScheduledTaskLog;
import com.wur7.store.mapper.ScheduledTaskLogMapper;
import com.wur7.store.constant.CommonConstant;
import com.wur7.store.util.exception.SteamHelperException;
import com.wur7.store.util.util.IpUtil;
import com.wur7.store.util.util.LogUtil;

/**
 * @interface: BaseScheduledTask
 * @description:
 * @author: L.zhen
 * @date:   2025/12/17 15:59
 */
public interface BaseScheduledTask extends Runnable{
	
    @Override
    default void run() {
        ScheduledTaskLog log = new ScheduledTaskLog();
        ScheduledTaskLogMapper scheduledTaskLogMapper = SpringContextUtil.getBean(ScheduledTaskLogMapper.class);
        try {
            log.setCreateTime(new Date());
            log.setAbleStatus(CommonConstant.ENABLE);
            log.setCronKey(this.getClass().getName());
            log.setStartTime(new Date());
            log.setHandleResult(CommonConstant.INIT);
            try {
                log.setHostIp(IpUtil.getLocalIP());
            } catch (Exception e) {
                e.printStackTrace();
            }
            scheduledTaskLogMapper.insert(log);
            synchronized (this){
                execute(new String[]{});//保持同步
            }
            log.setHandleResult(CommonConstant.SUCCESS);
        } catch (SteamHelperException e) {
            LogUtil.error("定时任务-" + this.getClass().getName() + "执行失败",e);
            log.setHandleResult(CommonConstant.FAIL);
            log.setErrorRemark(e.getMessage());
        } catch (Throwable e){
            long errorFlag = System.currentTimeMillis();
            LogUtil.error("定时任务-" + this.getClass().getName() + "执行异常"+errorFlag,e);
            log.setHandleResult(CommonConstant.FAIL);
            log.setErrorRemark(errorFlag +"");
            System.out.println(log.getErrorRemark());
        } finally {
            log.setEndTime(new Date());
            scheduledTaskLogMapper.updateByPrimaryKey(log);
        }
    }

    /**
     * 定时任务-业务逻辑（保持同步）
     */
    void execute(String ... params) throws Exception;
}
