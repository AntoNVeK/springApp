package com.example.myapp.service;

import com.example.myapp.repository.UserFollowersRepository;
import com.example.myapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserFollowersService {

    private final UserFollowersRepository userFollowersRepository;
    private final UserRepository userRepository;

    public UserFollowersService(UserFollowersRepository userFollowersRepository,
                                UserRepository userRepository) {
        this.userFollowersRepository = userFollowersRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void followUser(Long userId, Long followerId) {
        // Проверяем существование пользователей
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        if (!userRepository.existsById(followerId)) {
            throw new RuntimeException("Follower not found with id: " + followerId);
        }

        // Нельзя подписаться на самого себя
        if (userId.equals(followerId)) {
            throw new RuntimeException("Cannot follow yourself");
        }

        userFollowersRepository.follow(userId, followerId);
    }

    @Transactional
    public void unfollowUser(Long userId, Long followerId) {
        userFollowersRepository.unfollow(userId, followerId);
    }

    public List<Long> getFollowers(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return userFollowersRepository.getFollowers(userId);
    }

    public List<Long> getFollowing(Long followerId) {
        if (!userRepository.existsById(followerId)) {
            throw new RuntimeException("User not found with id: " + followerId);
        }
        return userFollowersRepository.getFollowing(followerId);
    }
}