package com.example.demo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class TaskDTO {
    private Long taskId;
    private String taskName;
    private LocalDate taskDate;
    private int hours;
    private int minutes;
    private Long userId;
    private Long clientId;

    // 追加のコンストラクタ（必要に応じて）
    public TaskDTO(Long taskId, String taskName, LocalDate taskDate, int hours, int minutes) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.hours = hours;
        this.minutes = minutes;
    }
}