package com.example.myapp.controller;

import com.example.myapp.RequestCounter;
import com.example.myapp.dto.ApiResponse;
import com.example.myapp.dto.UserDTO;
import com.example.myapp.model.User;
import com.example.myapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class TestUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestCounter requestCounter;

    // GET /users - получить всех пользователей
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userRepository.findAll().stream()
                .map(
                        user -> UserDTO.builder()
                                .fullName(user.getUserName())
                                .email(user.getEmail())
                                .build()
                )
                .toList();

        ApiResponse<List<UserDTO>> response = new ApiResponse<>("allUsers", users, requestCounter.getCount());
        requestCounter.increment();
        return ResponseEntity.ok(response);
    }

    // GET /users/{id} - получить пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    UserDTO userDTO = UserDTO.builder()
                            .fullName(user.getUserName())
                            .email(user.getEmail())
                            .build();

                    ApiResponse<UserDTO> response = new ApiResponse<>("user", userDTO);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // POST /users - создать нового пользователя
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // PUT /users/{id} - обновить пользователя
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUserName(userDetails.getUserName());
                    user.setEmail(userDetails.getEmail());
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /users/{id} - удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /users/search?name=... - поиск по имени
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String name) {
        return userRepository.findByUserNameContainingIgnoreCase(name);
    }

    // GET /users/count - количество пользователей
    @GetMapping("/count")
    public ResponseEntity<String> countUsers() {
        long count = userRepository.count();
        return ResponseEntity.ok("Всего пользователей в базе: " + count);
    }
}