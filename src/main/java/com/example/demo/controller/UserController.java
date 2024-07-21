package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import java.time.YearMonth;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
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
        System.out.println("到達");
        User user = userService.login(loginRequest);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getUserTasks(HttpSession session) {
        System.out.println("getUserTasks called");
        Long userId = (Long) session.getAttribute("userId");
        System.out.println("userId from session: " + userId);
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }
        List<Task> tasks = userService.getUserTasks(userId);
        List<TaskDTO> taskDTOs = tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        System.out.println("Retrieved tasks: " + taskDTOs);
        return ResponseEntity.ok(taskDTOs);
    }

    @GetMapping("/tasks/monthly")
    public ResponseEntity<Map<YearMonth, List<TaskDTO>>> getMonthlyTasks(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
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