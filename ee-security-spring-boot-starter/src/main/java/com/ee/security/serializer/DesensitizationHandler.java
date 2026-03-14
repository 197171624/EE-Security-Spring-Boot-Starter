package com.ee.security.serializer;

import com.ee.security.annotation.SensitiveField;
import com.ee.security.model.SensitiveType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @Description 脱敏处理器，在这里定义了各种数据类型对应的脱敏策略
 * @Author HanQinrui
 * @CreateTime 2026-03-13 20:47
 */
@Component
public class DesensitizationHandler {

    private final Map<SensitiveType, BiFunction<String, SensitiveField, String>> desensitizerMap = new HashMap<>();

    public DesensitizationHandler() {
        // 注册各种脱敏策略 - 使用JDK21的lambda表达式
        desensitizerMap.put(SensitiveType.MOBILE, this::desensitizeMobile);
        desensitizerMap.put(SensitiveType.ID_CARD, this::desensitizeIdCard);
        desensitizerMap.put(SensitiveType.EMAIL, this::desensitizeEmail);
        desensitizerMap.put(SensitiveType.NAME, this::desensitizeName);
        desensitizerMap.put(SensitiveType.BANK_CARD, this::desensitizeBankCard);
        desensitizerMap.put(SensitiveType.ADDRESS, this::desensitizeAddress);
    }

    /**
     * 根据注解进行脱敏
     */
    public String desensitize(String value, SensitiveField annotation) {
        if (StringUtils.isBlank(value)) {
            return value;
        }

        SensitiveType type = annotation.type();

        // 如果是自定义类型且提供了正则
        if (type == SensitiveType.CUSTOM && StringUtils.isNotBlank(annotation.pattern())) {
            return value.replaceAll(annotation.pattern(), annotation.replace());
        }

        // 使用预定义的脱敏策略
        BiFunction<String, SensitiveField, String> desensitizer = desensitizerMap.get(type);
        if (desensitizer != null) {
            return desensitizer.apply(value, annotation);
        }

        return value;
    }

    // 手机号脱敏：138****0000
    private String desensitizeMobile(String value, SensitiveField annotation) {
        if (value.length() < 7) {
            return value;
        }
        int keepLeft = Math.min(annotation.keepLeft(), 3);
        int keepRight = Math.min(annotation.keepRight(), 4);
        String replace = annotation.replace();
        return StringUtils.overlay(value, StringUtils.repeat(replace, 4), keepLeft, value.length() - keepRight);
    }

    // 身份证脱敏：123456********1234
    private String desensitizeIdCard(String value, SensitiveField annotation) {
        if (value.length() < 10) {
            return value;
        }
        int keepLeft = Math.min(annotation.keepLeft(), 6);
        int keepRight = Math.min(annotation.keepRight(), 4);
        String replace = annotation.replace();
        return StringUtils.overlay(value, StringUtils.repeat(replace, 8), keepLeft, value.length() - keepRight);
    }

    // 邮箱脱敏：tes***@qq.com
    private String desensitizeEmail(String value, SensitiveField annotation) {
        int atIndex = value.indexOf('@');
        if (atIndex <= 1) {
            return value;
        }
        int keepLeft = Math.min(annotation.keepLeft(), 3);
        String prefix = value.substring(0, atIndex);
        String suffix = value.substring(atIndex);
        if (prefix.length() <= keepLeft) {
            return value;
        }
        return prefix.substring(0, keepLeft) + StringUtils.repeat(annotation.replace(), 3) + suffix;
    }

    // 姓名脱敏：张*
    private String desensitizeName(String value, SensitiveField annotation) {
        if (value.length() <= 1) {
            return value;
        }
        int keepLeft = Math.min(annotation.keepLeft(), 1);
        return value.substring(0, keepLeft) + StringUtils.repeat(annotation.replace(), value.length() - keepLeft);
    }

    // 银行卡脱敏：**** **** **** 1234
    private String desensitizeBankCard(String value, SensitiveField annotation) {
        if (value.length() < 10) {
            return value;
        }
        int keepRight = Math.min(annotation.keepRight(), 4);
        String last4 = value.substring(value.length() - keepRight);
        return StringUtils.repeat(annotation.replace(), value.length() - keepRight) + last4;
    }

    // 地址脱敏：保留前6个字符，后面用*代替
    private String desensitizeAddress(String value, SensitiveField annotation) {
        if (value.length() <= annotation.keepLeft()) {
            return value;
        }
        return value.substring(0, annotation.keepLeft()) +
                StringUtils.repeat(annotation.replace(), value.length() - annotation.keepLeft());
    }
}