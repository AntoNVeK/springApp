package com.example.myapp.dto;


import java.util.List;

public class ApiResponse<T> {
    private String type;
    private T data;
    private int count;

    public ApiResponse(String type, T data) {
        this.type = type;
        this.data = data;
        if (data instanceof List) {
            this.count = ((List<?>) data).size();
        }
    }

    public ApiResponse(String type, T data, int count) {
        this.type = type;
        this.data = data;
        this.count = count;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public T getData() { return data; }
    public void setData(T data) {
        this.data = data;
        if (data instanceof List) {
            this.count = ((List<?>) data).size();
        }
    }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}


