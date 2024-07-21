package com.example.demo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize
public class TaskDTO {
    private Long taskId;
    private String taskName;
    private LocalDate taskDate;
    private int hours;
    private int minutes;
    private Long userId;
    private Long clientId;
}