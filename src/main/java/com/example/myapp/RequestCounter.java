package com.example.myapp;
import org.springframework.stereotype.Component;

@Component
public class RequestCounter {

    private int count = 0;

    public RequestCounter() {
        System.out.println("=== Создан новый RequestCounter для запроса ===");
    }

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}