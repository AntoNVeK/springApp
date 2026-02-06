package com.example.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentWithPostInfo {
    private Long id;
    private String text;
    private String username;
    private String postTitle;
    private int totalComments;
}
