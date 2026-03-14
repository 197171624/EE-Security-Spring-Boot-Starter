package com.ee.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EeSecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(EeSecurityApplication.class, args);
		System.out.println("""
            
            ╔══════════════════════════════════════════════════════════╗
            ║  EE Security Starter 启动成功！                          ║
            ║  访问 http://localhost:8080/test 测试脱敏功能            ║
            ║  JDK版本: 21                                             ║
            ║  Spring Boot: 3.5.12-SNAPSHOT                           ║
            ╚══════════════════════════════════════════════════════════╝
            """);
	}
}