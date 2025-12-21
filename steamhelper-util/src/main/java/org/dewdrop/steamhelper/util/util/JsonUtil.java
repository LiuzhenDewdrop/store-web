package org.dewdrop.steamhelper.util.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @class:  JsonUtil
 * @description: 
 * @author: L.zhen
 * @date:   2025/12/20 14:09
 */
@Slf4j
public class JsonUtil {
    private static volatile ObjectMapper mapper = new ObjectMapper();

    private JsonUtil(){}

    public static byte[] toJsonBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (json == null){
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(byte[] json, Class<T> clazz) {
        if (json == null){
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        if (json == null){
            return null;
        }
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <T> T toObject(byte[] json, TypeReference<T> typeReference) {
        if (json == null){
            return null;
        }
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }
	
	/**
	 * 保留null值的字段信息，如需不保留可用{@link JsonUtil#toJSONString(Object)}
	 * @param o
	 * @return
	 */
	public static String toJson(Object o) {
		if (o == null){
			return null;
		}
		try {
			return mapper.writeValueAsString(o);
		} catch (IOException var3) {
			throw new RuntimeException(var3);
		}
	}
	
	/**
	 * 不保留null值的字段信息，如需保留可用{@link JsonUtil#toJson(Object)}
	 * @param o
	 * @return
	 */
	public static String toJSONString(Object o) {
		return JSON.toJSONString(o);
	}
    
    public static <T> T toObject(Map<String,Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return mapper.convertValue(map, clazz);
    }

    /**
     * json字符串转成list
     *
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> List<T> toList(@NonNull String jsonString, Class<T> cls) {
        try {
            return mapper.readValue(jsonString, getCollectionType(List.class, cls));
        } catch (JsonProcessingException e) {
            String className = cls.getSimpleName();
            log.error(" parse json [{}] to class [{}] error：{}", jsonString, className, e);
        }
        return null;
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  实体bean
     * @return JavaType Java类型
     */
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


    public static <T> T alibabaParseObject(String json,Class<T> clas){
        return JSON.parseObject(json,clas);
    }
    
    public static String alibabaJsonString(Object obj, SerializeFilter... filters) {
        return JSON.toJSONString(obj, filters, new SerializerFeature[0]);
    }
}

