package com.ee.security.model;

/**
 * @Description 脱敏类型枚举
 * @Author HanQinrui
 * @CreateTime 2026-03-13 20:49
 */
public enum SensitiveType {
    /**
     * 手机号
     */
    MOBILE,

    /**
     * 身份证号
     */
    ID_CARD,

    /**
     * 邮箱
     */
    EMAIL,

    /**
     * 银行卡号
     */
    BANK_CARD,

    /**
     * 中文名
     */
    NAME,

    /**
     * 地址
     */
    ADDRESS,

    /**
     * 自定义
     */
    CUSTOM
}