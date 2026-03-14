package com.ee.security.serializer;

import com.ee.security.annotation.Sensitive;
import com.ee.security.annotation.SensitiveField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @Description 脱敏响应拦截器
 * @Author HanQinrui
 * @CreateTime 2026-03-13 20:47
 */
@ControllerAdvice
public class SensitiveResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private DesensitizationHandler desensitizationHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查方法或类上是否有@Sensitive注解
        Sensitive methodSensitive = returnType.getMethodAnnotation(Sensitive.class);
        if (methodSensitive != null) {
            return methodSensitive.enabled();
        }

        Sensitive classSensitive = AnnotationUtils.findAnnotation(returnType.getContainingClass(), Sensitive.class);
        return classSensitive != null && classSensitive.enabled();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return null;
        }

        // 处理脱敏
        try {
            return processDesensitization(body);
        } catch (Exception e) {
            // 实际项目中建议使用日志框架
            System.err.println("脱敏处理失败: " + e.getMessage());
            return body;
        }
    }

    /**
     * 递归处理脱敏
     */
    private Object processDesensitization(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }

        // 处理String类型（可能是JSON字符串）
        if (obj instanceof String str) {
            if (str.trim().startsWith("{") || str.trim().startsWith("[")) {
                // 尝试解析JSON
                try {
                    Object jsonObj = objectMapper.readValue(str, Object.class);
                    Object processed = processDesensitization(jsonObj);
                    return objectMapper.writeValueAsString(processed);
                } catch (JsonProcessingException e) {
                    // 不是JSON字符串，原样返回
                    return str;
                }
            }
            return str;
        }

        // 处理集合
        if (obj instanceof Collection<?> collection) {
            for (Object item : collection) {
                processDesensitization(item);
            }
            return obj;
        }

        // 处理Map
        if (obj instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    processDesensitization(entry.getValue());
                }
            }
            return obj;
        }

        // 处理普通对象
        processObjectFields(obj);
        return obj;
    }

    /**
     * 处理对象的字段
     */
    private void processObjectFields(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
            if (sensitiveField != null) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value instanceof String str) {
                    String desensitized = desensitizationHandler.desensitize(str, sensitiveField);
                    field.set(obj, desensitized);
                } else if (value != null && !isPrimitiveOrWrapper(value.getClass())) {
                    // 如果是对象类型，递归处理
                    processObjectFields(value);
                }
            }
        }
    }

    /**
     * 判断是否为基本类型或包装类
     */
    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Character.class);
    }
}