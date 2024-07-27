package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.TaskDTO;
import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public void saveBulkTasks(List<Task> tasks) {
        taskRepository.saveAll(tasks);
    }

    public List<TaskDTO> getTasksByUserId(Long userId) {
        System.out.println("Fetching tasks for user ID: " + userId);
        List<Task> tasks = taskRepository.findByUserId(userId);
        System.out.println("Found " + tasks.size() + " tasks");
        
        // TaskをTaskDTOに変換
        List<TaskDTO> taskDTOs = tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return taskDTOs;
    }

    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(
            task.getTaskId(),
            task.getTaskName(),
            task.getTaskDate(),
            task.getHours(),
            task.getMinutes(),
            task.getUser() != null ? task.getUser().getId() : null,
            task.getClient() != null ? task.getClient().getClientId() : null
        );
    }
}