package com.ee.security.controller;

import com.ee.security.annotation.Sensitive;
import com.ee.security.annotation.SensitiveField;
import com.ee.security.model.SensitiveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description test controller
 * @Author HanQinrui
 * @CreateTime 2026-03-13 20:47
 */
@RestController
@RequestMapping("/test")
@Sensitive  // 类级别启用脱敏
public class TestController {

    /**
     * 测试普通对象脱敏
     */
    @GetMapping("/user")
    public User getUser() {
        return new User("张三", "13800138000", "123456199001011234", "zhangsan@qq.com");
    }

    /**
     * 测试List脱敏
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return Arrays.asList(
                new User("李四", "13912345678", "123456199002021235", "lisi@qq.com"),
                new User("王五", "15812348888", "123456199003031236", "wangwu@qq.com")
        );
    }

    /**
     * 测试Map脱敏
     */
    @GetMapping("/map")
    public Map<String, User> getMap() {
        Map<String, User> map = new HashMap<>();
        map.put("user1", new User("赵六", "17712345678", "123456199004041237", "zhaoliu@qq.com"));
        return map;
    }

    /**
     * 测试不启用脱敏（方法级别覆盖）
     */
    @GetMapping("/no-sensitive")
    @Sensitive(enabled = false)
    public User getNoSensitive() {
        return new User("张三", "13800138000", "123456199001011234", "zhangsan@qq.com");
    }

    /**
     * 测试自定义脱敏规则
     */
    @GetMapping("/custom")
    public CustomUser getCustomUser() {
        return new CustomUser("test123456", "1234567890123456");
    }

    @Data
    @AllArgsConstructor
    public static class User {
        @SensitiveField(type = SensitiveType.NAME)
        private String name;

        @SensitiveField(type = SensitiveType.MOBILE)
        private String phone;

        @SensitiveField(type = SensitiveType.ID_CARD)
        private String idCard;

        @SensitiveField(type = SensitiveType.EMAIL)
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class CustomUser {
        @SensitiveField(type = SensitiveType.CUSTOM, pattern = "(?<=.{4}).", replace = "*")
        private String username;

        @SensitiveField(type = SensitiveType.BANK_CARD, keepRight = 4)
        private String bankCard;
    }
}