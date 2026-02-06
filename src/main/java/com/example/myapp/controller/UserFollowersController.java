package com.example.myapp.controller;

import com.example.myapp.service.UserFollowersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserFollowersController {

    private final UserFollowersService userFollowersService;

    public UserFollowersController(UserFollowersService userFollowersService) {
        this.userFollowersService = userFollowersService;
    }

    @PostMapping("/{userId}/follow/{followerId}")
    public ResponseEntity<Void> followUser(
            @PathVariable Long userId,
            @PathVariable Long followerId) {

        try {
            userFollowersService.followUser(userId, followerId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{userId}/unfollow/{followerId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable Long userId,
            @PathVariable Long followerId) {

        userFollowersService.unfollowUser(userId, followerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<Long>> getFollowers(@PathVariable Long userId) {
        try {
            List<Long> followers = userFollowersService.getFollowers(userId);
            return ResponseEntity.ok(followers);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{followerId}/following")
    public ResponseEntity<List<Long>> getFollowing(@PathVariable Long followerId) {
        try {
            List<Long> following = userFollowersService.getFollowing(followerId);
            return ResponseEntity.ok(following);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}