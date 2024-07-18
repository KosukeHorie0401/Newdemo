package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityContextService securityContextService; // New service injected

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username " + username));
    }

    public User createUser(User user) {
        System.out.println("Received user data: " + user);
        
        String rawPassword = user.getPassword();
        System.out.println("Raw password before encoding: " + rawPassword);
        
        // パスワードが既にエンコードされているかチェック
        String encodedPassword;
        if (rawPassword.startsWith("$2a$")) {
            encodedPassword = rawPassword;
        } else {
            encodedPassword = passwordEncoder.encode(rawPassword);
        }
        
        System.out.println("Encoded password: " + encodedPassword);
        user.setPassword(encodedPassword);
        System.out.println("Password set in user object: " + user.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        System.out.println("Saved user: " + savedUser);
        return savedUser;
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setRole(userDetails.getRole());
        user.setClient(userDetails.getClient());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean login(LoginRequest loginRequest) {
        System.out.println("PasswordEncoder instance in login: " + System.identityHashCode(passwordEncoder));
        
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("User found: " + user.getUsername());
            System.out.println("Encoded Password in DB: " + user.getPassword());
            System.out.println("Password from LoginRequest: " + loginRequest.getPassword());
            
            String rawPassword = loginRequest.getPassword();
            String encodedPasswordInDB = user.getPassword();
            System.out.println("Raw password from login: " + rawPassword);
            System.out.println("Encoded password in DB: " + encodedPasswordInDB);
            boolean matches = passwordEncoder.matches(rawPassword, encodedPasswordInDB);
            System.out.println("Password matches: " + matches);
            
            // 追加のデバッグ情報
            System.out.println("Raw password length: " + rawPassword.length());
            System.out.println("Encoded password length: " + encodedPasswordInDB.length());
            
            // デバッグ用：手動でエンコードして比較
            String manuallyEncodedPassword = passwordEncoder.encode(rawPassword);
            System.out.println("Manually encoded password: " + manuallyEncodedPassword);
            System.out.println("Manual comparison: " + encodedPasswordInDB.equals(manuallyEncodedPassword));
            
            if (matches) {
                System.out.println("Password matches");
                return true;
            } else {
                System.out.println("Password does not match");
            }
        } else {
            System.out.println("User not found");
        }
        return false;
    }

    public List<Task> getUserTasks() {
        User currentUser = securityContextService.getCurrentUser();
        return taskRepository.findByUser(currentUser);
    }
}