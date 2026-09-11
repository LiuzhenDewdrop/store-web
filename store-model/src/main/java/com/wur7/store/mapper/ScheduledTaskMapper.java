package com.wur7.store.mapper;

import com.wur7.store.entity.ScheduledTask;

import java.util.List;

public interface ScheduledTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScheduledTask record);

    int insertSelective(ScheduledTask record);

    ScheduledTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScheduledTask record);

    int updateByPrimaryKey(ScheduledTask record);

    List<ScheduledTask> findList(ScheduledTask query);

    ScheduledTask findOne(ScheduledTask query);
}