# EE Security Spring Boot Starter (易安全)

<div align="center">
  <p>
    <a href="https://github.com/yourusername/spring-boot-starter-security-enhance">
      <img src="https://img.shields.io/badge/JDK-21%2B-green.svg" alt="JDK">
    </a>
    <a href="https://github.com/yourusername/spring-boot-starter-security-enhance">
      <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg" alt="Spring Boot">
    </a>
    <a href="LICENSE">
      <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License">
    </a>
    <a href="https://github.com/yourusername/spring-boot-starter-security-enhance/stargazers">
      <img src="https://img.shields.io/github/stars/yourusername/spring-boot-starter-security-enhance?style=social" alt="GitHub Stars">
    </a>
  </p>

<h3>🔒 一行注解，安全无忧</h3>
  <p>为 Spring Boot 打造的轻量级安全增强组件，提供数据脱敏、数字水印、操作审计等功能</p>
</div>

---

## 📖 项目简介

EE Security Starter 是一个专注于 Spring Boot 应用安全的轻量级组件，通过简单的注解即可实现数据脱敏、数字水印、操作审计等安全功能，帮助开发者快速满足等保 2.0 合规要求。

---

## ✨ 功能特性

| 模块         | 功能                                                 | 状态      |
| :----------- | :--------------------------------------------------- | :-------- |
| **数据脱敏** | 手机号、身份证、邮箱、姓名、银行卡、地址、自定义正则 | ✅ 稳定版 |
| **数字水印** | 后端水印生成、前端 Canvas 渲染                       | 🚧 开发中 |
| **操作审计** | `@AuditLog`注解、自动记录操作日志                    | 🚧 开发中 |
| **企业版**   | 审计日志存储、可视化看板、告警功能                   | 🚧 开发中 |

---

## 🎯 数据脱敏演示

### 使用示例

```
@RestController
@Sensitive  // 类级别启用脱敏
public class UserController {

    @GetMapping("/user")
    public User getUser() {
        return new User("张三", "13800138000", "123456199001011234", "zhangsan@qq.com");
    }
}

@Data
@AllArgsConstructor
public class User {
    @SensitiveField(type = SensitiveType.NAME)
    private String name;

    @SensitiveField(type = SensitiveType.MOBILE)
    private String phone;

    @SensitiveField(type = SensitiveType.ID_CARD)
    private String idCard;

    @SensitiveField(type = SensitiveType.EMAIL)
    private String email;
}
```

### 返回结果

```
{
  "name": "张*",
  "phone": "138****0000",
  "idCard": "123456********1234",
  "email": "zha***@qq.com"
}
```

## 🚀 快速开始（3 分钟集成）

### 1、添加依赖

#### Maven

```
<dependency>
    <groupId>com.ee.security</groupId>
    <artifactId>ee-security-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

#### Gradle

```
implementation 'com.ee.security:ee-security-spring-boot-starter:1.0.0-SNAPSHOT'
```

### 2、在 Controller 上启用脱敏

```
@RestController
@Sensitive
public class TestController {

    @GetMapping("/test")
    public User test() {
        return new User("张三", "13800138000");
    }
}
```

### 3、标记需要脱敏的字段

```java
@Data
public class User {
    @SensitiveField(type = SensitiveType.NAME)
    private String name;

    @SensitiveField(type = SensitiveType.MOBILE)
    private String phone;
}
```

### 4、完成！访问接口查看效果 ✨

访问 http://localhost:8080/test 即可看到自动脱敏后的结果。

## 🛠️ 配置说明

### 在 application.yml 中添加以下配置：

```
ee:
  security:
    enabled: true               # 总开关，默认true
    sensitive:
      enabled: true              # 脱敏模块开关，默认true
      default-replace: "*"       # 默认替换字符，默认*
    watermark:
      enabled: false             # 水印模块开关（开发中）
    audit:
      enabled: false             # 审计模块开关（开发中）
```

## 📄 许可证

本项目采用 Apache License 2.0 许可证 - 查看 [LICENSE 文件](https://github.com/197171624/EE-Security-Spring-Boot-Starter/blob/main/LICENSE)了解详情。

## ⭐ 支持项目

如果你觉得这个项目对你有帮助：

1、⭐ 点个 Star 支持一下

2、📝 在评论区留言反馈

3、🔄 转发给需要的朋友

你的支持是我持续开源的动力！

## 📬 联系作者

📧 邮箱：hanqinrui@qq.com

<div align="center"> <p>Made with ❤️ by HanQinrui</p>  </div>
