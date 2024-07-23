package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ClientDTO {
    private Long clientId;
    private String companyName;
    private String representativeName;
    private String contactPersonName;
    private String email;
    private String phone;
    private String address;
    private String websiteUrl;
    private LocalDate contractStartDate;
    private String priority;
    private String meetingHistory;
    private String referralRecord;
    private List<UserDTO> users;  // Added this line
}