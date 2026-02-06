package com.example.myapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostWithCountComments {
    private Long id;
    private String title;
    private Long commentCount;

    @Override
    public String toString() {
        return "PostWithCountComments{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", commentCount=" + commentCount +
                '}';
    }
}
