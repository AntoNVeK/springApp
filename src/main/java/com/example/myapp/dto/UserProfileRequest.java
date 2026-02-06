package com.example.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserProfileRequest {
    private String fullName;
    private String bio;
    private String city;
    private String street;
    private String zipCode;
}
