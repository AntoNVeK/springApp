package com.example.myapp.controller;

import com.example.myapp.dto.UserProfileRequest;
import com.example.myapp.model.UserProfile;
import com.example.myapp.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long userId) {
        return userProfileService.getProfileByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<UserProfile> createOrUpdateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequest request) {

        try {
            UserProfile profile = userProfileService.createOrUpdateProfile(userId, request);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}