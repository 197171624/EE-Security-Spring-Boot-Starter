package com.ee.security.annotation;

import com.ee.security.model.SensitiveType;

import java.lang.annotation.*;

/**
 * @Description 标记需要脱敏的字段，标注在字段上，指定该字段的脱敏规则
 * @Author HanQinrui
 * @CreateTime 2026-03-13 20:47
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveField {
    /**
     * 脱敏类型
     */
    SensitiveType type() default SensitiveType.CUSTOM;

    /**
     * 自定义正则表达式（当type=CUSTOM时有效）
     */
    String pattern() default "";

    /**
     * 替换字符
     */
    String replace() default "*";

    /**
     * 保留左边字符数
     */
    int keepLeft() default 2;

    /**
     * 保留右边字符数
     */
    int keepRight() default 2;
}