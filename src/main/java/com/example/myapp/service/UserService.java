package com.example.myapp.service;

import com.example.myapp.dto.UserRequest;
import com.example.myapp.model.User;
import com.example.myapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(UserRequest userRequest) {
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setAge(userRequest.getAge());

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setAge(userRequest.getAge());

        return userRepository.update(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.findByUserNameContainingIgnoreCase(name);
    }

    public List<User> searchUsersByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public long countUsers() {
        return userRepository.count();
    }
}