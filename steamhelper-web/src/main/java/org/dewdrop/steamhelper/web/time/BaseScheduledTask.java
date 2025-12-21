package org.dewdrop.steamhelper.web.time;

import java.util.Date;

import org.dewdrop.steamhelper.web.util.SpringContextUtil;
import org.dewdrop.steamhelper.entity.ScheduledTaskLog;
import org.dewdrop.steamhelper.mapper.ScheduledTaskLogMapper;
import org.dewdrop.steamhelper.util.constant.CommonConstant;
import org.dewdrop.steamhelper.util.exception.SteamHelperException;
import org.dewdrop.steamhelper.util.util.IpUtil;
import org.dewdrop.steamhelper.util.util.LogUtil;

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
