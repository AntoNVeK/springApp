package com.example.myapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Добро пожаловать в Spring Boot приложение! 🚀<br>" +
                "Доступные endpoints:<br>" +
                "• <a href='/health'>/health</a> - проверка здоровья<br>" +
                "• <a href='/api/users'>/api/users</a> - все пользователи<br>" +
                "• <a href='/api/users/count'>/api/users/count</a> - количество пользователей";
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", LocalDateTime.now().toString());
        healthInfo.put("service", "Spring Boot Demo");
        healthInfo.put("version", "1.0.0");
        return healthInfo;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Привет от Spring Boot! 👋";
    }
}