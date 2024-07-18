package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TaskDTO {
    private Long taskId;
    private String taskName;
    private LocalDate taskDate;
    private int hours;
    private int minutes;
    private Long userId;
    private Long clientId;
}