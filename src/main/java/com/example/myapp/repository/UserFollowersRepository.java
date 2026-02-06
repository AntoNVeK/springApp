package com.example.myapp.repository;

import com.example.myapp.model.UserFollower;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserFollowersRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserFollowersRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void follow(Long userId, Long followerId) {
        String sql = "INSERT INTO user_followers(user_id, follower_id, created_at) VALUES (?, ?, NOW())";
        jdbcTemplate.update(sql, userId, followerId);
    }

    public void unfollow(Long userId, Long followerId) {
        jdbcTemplate.update("DELETE FROM user_followers WHERE user_id=? AND follower_id=?", userId, followerId);
    }

    public List<Long> getFollowers(Long userId) {
        return jdbcTemplate.queryForList("SELECT follower_id FROM user_followers WHERE user_id=?", Long.class, userId);
    }

    public List<Long> getFollowing(Long followerId) {
        return jdbcTemplate.queryForList("SELECT user_id FROM user_followers WHERE follower_id=?", Long.class, followerId);
    }
}
