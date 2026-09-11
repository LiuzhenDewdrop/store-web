package com.wur7.store.util.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {
    private ReflectUtil(){}
    /**
     * 获取obj对象fieldName的Field
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {}
        }
        return null;
    }

    /**
     * 反射SET值到Map中
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static Map<String, Object> setBeanValueToMap(Object bean) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> params = new HashMap<String, Object>();
        if (bean != null) {
            Field[] fields = bean.getClass().getDeclaredFields();
            // 属性值
            Object beanValue = null;
            for (Field field : fields) {
                // 字段名称
                String fieldName = field.getName();
                if (field.isAccessible()) {
                    beanValue = field.get(bean);
                } else {
                    field.setAccessible(true);
                    beanValue = field.get(bean);
                }
                // 设值
                params.put(fieldName, beanValue);

            }
        }
        return params;
    }

    /**
     * 获取obj对象fieldName的属性值
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }
}
