package com.wur7.store.mybatis.interceptors;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义mybatis拦截器
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class StoreMybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("------sqlId------" + sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        log.debug("------sqlCommandType------" + sqlCommandType);
        //updateWithVersion 不拦截
        String method = sqlId.split("\\.")[sqlId.split("\\.").length-1];
        if("updateWithVersion".equals(method) && "UPDATE".equals(sqlCommandType.name())) {
        	return invocation.proceed();
        }
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return invocation.proceed();
        }
        if (SqlCommandType.INSERT == sqlCommandType) {
            try {
                Field optimisticField = getFieldByFieldName(parameter, "optimistic");
                if (optimisticField != null) {
                    optimisticField.setAccessible(true);
                    Object optimisticFieldVal = optimisticField.get(parameter);
                    if (optimisticFieldVal == null) {
                        //没有赋值，设置为0
                        optimisticField.set(parameter, 0);
                    }
                    optimisticField.setAccessible(false);
                }
            } catch (Exception e) {
                log.error("INSERT optimistic error", e);
            }

            try {
                Field createTimeField = getFieldByFieldName(parameter, "createTime");
                if (createTimeField != null) {
                    createTimeField.setAccessible(true);
                    Object createTimeFieldVal = createTimeField.get(parameter);
                    if (createTimeFieldVal == null) {
                        //没有赋值，设置当前时间
                        createTimeField.set(parameter, new Date());
                    }
                    createTimeField.setAccessible(false);
                }
            } catch (Exception e) {
                log.error("INSERT createTime error", e);
            }
        } else if (SqlCommandType.UPDATE == sqlCommandType) {
            try {
                Field optimisticField = getFieldByFieldName(parameter, "optimistic");
                if (optimisticField != null) {
                    optimisticField.setAccessible(true);
                    Object optimisticFieldVal = optimisticField.get(parameter);
                    if (optimisticFieldVal != null) {
                        //有旧值，加1
                        if(optimisticFieldVal instanceof Integer){
                            optimisticField.set(parameter,(Integer)optimisticFieldVal + 1);
                        }else if(optimisticFieldVal instanceof Long){
                            optimisticField.set(parameter,(Long)optimisticFieldVal + 1);
                        }
                    }
                    optimisticField.setAccessible(false);
                }
            } catch (Exception e) {
                log.error("UPDATE optimistic error", e);
            }

            try {
                Field updateTimeField = getFieldByFieldName(parameter, "updateTime");
                if(updateTimeField != null){
                    updateTimeField.setAccessible(true);
                    //设置当前时间
                    updateTimeField.set(parameter, new Date());
                    updateTimeField.setAccessible(false);
                }
            } catch (Exception e) {
                log.error("UPDATE updateTime error", e);
            }

            try {
                //兼容有些表取lastUpdateTime最为更新时间
                Field lastUpdateTimeField = getFieldByFieldName(parameter, "lastUpdateTime");
                if(lastUpdateTimeField != null){
                    lastUpdateTimeField.setAccessible(true);
                    //设置当前时间
                    lastUpdateTimeField.set(parameter, new Date());
                    lastUpdateTimeField.setAccessible(false);
                }
            } catch (Exception e) {
                log.error("UPDATE lastUpdateTime error", e);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {}
        }
        return null;
    }
}
