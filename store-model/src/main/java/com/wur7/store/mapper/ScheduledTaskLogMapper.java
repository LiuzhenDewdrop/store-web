package com.wur7.store.mapper;

import com.wur7.store.entity.ScheduledTaskLog;

import java.util.List;

public interface ScheduledTaskLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ScheduledTaskLog record);

    int insertSelective(ScheduledTaskLog record);

    ScheduledTaskLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ScheduledTaskLog record);

    int updateByPrimaryKey(ScheduledTaskLog record);

    List<ScheduledTaskLog> findList(ScheduledTaskLog query);
}