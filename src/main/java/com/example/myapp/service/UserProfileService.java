package com.example.myapp.service;

import com.example.myapp.dto.UserProfileRequest;
import com.example.myapp.model.UserProfile;
import com.example.myapp.repository.UserProfileRepository;
import com.example.myapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileService(UserProfileRepository userProfileRepository,
                              UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserProfile createOrUpdateProfile(Long userId, UserProfileRequest request) {
        // Проверяем существование пользователя
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        // Проверяем, существует ли уже профиль
        Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);

        if (existingProfile.isPresent()) {
            // Обновляем существующий профиль
            UserProfile profile = existingProfile.get();
            profile.setFullName(request.getFullName());
            profile.setBio(request.getBio());
            profile.setCity(request.getCity());
            profile.setStreet(request.getStreet());
            profile.setZipCode(request.getZipCode());
            return userProfileRepository.save(profile);
        } else {
            // Создаем новый профиль
            UserProfile profile = new UserProfile();
            profile.setUserId(userId);
            profile.setFullName(request.getFullName());
            profile.setBio(request.getBio());
            profile.setCity(request.getCity());
            profile.setStreet(request.getStreet());
            profile.setZipCode(request.getZipCode());
            return userProfileRepository.save(profile);
        }
    }

    public Optional<UserProfile> getProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    public List<UserProfile> getAllProfiles() {
        return userProfileRepository.findAll();
    }
}