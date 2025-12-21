package org.dewdrop.steamhelper.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduledTask {
    private Integer id;

    private Integer optimistic;

    private String cronKey;

    private String beanName;

    private String cronExpression;

    private String ableStatus;

    private String taskRemark;

    private Date createTime;

    private Date lastUpdateTime;

    public ScheduledTask() {
    }

    public ScheduledTask(Integer id, Integer optimistic, String cronKey, String beanName, String cronExpression, String ableStatus, String taskRemark, Date createTime, Date lastUpdateTime) {
        this.id = id;
        this.optimistic = optimistic;
        this.cronKey = cronKey;
        this.beanName = beanName;
        this.cronExpression = cronExpression;
        this.ableStatus = ableStatus;
        this.taskRemark = taskRemark;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }
}