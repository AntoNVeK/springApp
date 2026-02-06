package com.example.myapp.model;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String userName;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String userName, String email, Integer age) {
        this.userName = userName;
        this.email = email;
        this.age = age;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
