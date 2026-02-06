package com.example.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyAppApplication.class, args);
        System.out.println("=================================");
        System.out.println("✅ Spring Boot запущен!");
        System.out.println("🌐 Сервер: http://localhost:8080");
        System.out.println("=================================");




    }
}