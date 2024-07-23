package com.example.demo.controller;

import java.time.YearMonth;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.TaskDTO;
import com.example.demo.entity.Client;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        User user = userService.login(loginRequest);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getId(), null, 
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
                )
            );
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            return ResponseEntity.ok()
                .header("X-Session-ID", session.getId())
                .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            session.invalidate();
            return ResponseEntity.ok().body("Logged out successfully");
        } else {
            return ResponseEntity.badRequest().body("No active session found");
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getUserTasks(HttpSession session, Authentication authentication) {
        System.out.println("Received request for getUserTasks");
        System.out.println("Session ID: " + session.getId());
        System.out.println("User ID from session: " + session.getAttribute("userId"));
        System.out.println("User Role from session: " + session.getAttribute("userRole"));
        System.out.println("Authentication: " + (authentication != null ? authentication.getName() : "null"));

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = (Long) authentication.getPrincipal();
        List<Task> tasks = userService.getUserTasks(userId);
        List<TaskDTO> taskDTOs = tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(taskDTOs);
    }

    @GetMapping("/tasks/monthly")
    public ResponseEntity<Map<YearMonth, List<TaskDTO>>> getMonthlyTasks(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Task> tasks = userService.getUserTasks(userId);
        Map<YearMonth, List<TaskDTO>> monthlyTasks = tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.groupingBy(
                task -> YearMonth.from(task.getTaskDate()),
                Collectors.toList()
            ));
        return ResponseEntity.ok(monthlyTasks);
    }

    @GetMapping("/client-info")
    public ResponseEntity<Map<String, String>> getClientInfo(HttpSession session, Authentication authentication) {
        System.out.println("Session ID: " + session.getId());
        System.out.println("User ID from session: " + session.getAttribute("userId"));
        System.out.println("User Role from session: " + session.getAttribute("userRole"));
        System.out.println("Authentication: " + (authentication != null ? authentication.getName() : "null"));

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = (Long) authentication.getPrincipal();
        User user = userService.getUserById(userId);
        Client client = user.getClient();
        Map<String, String> response = new HashMap<>();
        response.put("companyName", client != null ? client.getCompanyName() : "ゲスト");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-login")
    public ResponseEntity<Map<String, Boolean>> checkLoginStatus(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Map<String, Boolean> response = new HashMap<>();
        if (userId != null) {
            User user = userService.getUserById(userId);
            response.put("isLoggedIn", true);
            response.put("isAdmin", "ROLE_ADMIN".equals(user.getRole()));
        } else {
            response.put("isLoggedIn", false);
            response.put("isAdmin", false);
        }
        return ResponseEntity.ok(response);
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setTaskId(task.getTaskId());
        dto.setTaskName(task.getTaskName());
        dto.setTaskDate(task.getTaskDate());
        dto.setHours(task.getHours());
        dto.setMinutes(task.getMinutes());
        // 必要に応じて他のフィールドも設定
        return dto;
    }
}