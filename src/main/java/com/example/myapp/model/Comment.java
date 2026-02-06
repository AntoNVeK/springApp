package com.example.myapp.model;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private Long postId;
    private Long userId;
    private Long parentCommentId; // null если корневой комментарий

    public Comment(String text, Long postId, Long userId, Long parentCommentId) {
        this.text = text;
        this.postId = postId;
        this.userId = userId;
        this.parentCommentId = parentCommentId;
    }

    public Comment() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(Long parentCommentId) { this.parentCommentId = parentCommentId; }
}
