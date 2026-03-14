package com.ee.security.annotation;

import java.lang.annotation.*;

/**
 * @Description 启用数据脱敏，标注在类或方法上，表示该接口的返回值需要进行脱敏处理
 * @Author HanQinrui
 * @CreateTime 2026-03-13 20:44
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {
    /**
     * 是否启用脱敏
     */
    boolean enabled() default true;
}