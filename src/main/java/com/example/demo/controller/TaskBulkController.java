package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskBulkController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/bulk")
    public ResponseEntity<String> bulkInsertTasks(@RequestBody List<Task> tasks) {
        taskService.saveBulkTasks(tasks);
        return ResponseEntity.ok("Tasks inserted successfully");
    }
}