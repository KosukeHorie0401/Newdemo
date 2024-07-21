package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "representative_name", nullable = false)
    private String representativeName;

    @Column(name = "contact_person_name", nullable = false)
    private String contactPersonName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "contract_start_date", nullable = false)
    private LocalDate contractStartDate;

    @Column(name = "meeting_history", columnDefinition = "TEXT")
    private String meetingHistory;

    @Column(name = "referral_record", columnDefinition = "TEXT")
    private String referralRecord;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", companyName='" + companyName + '\'' +
                ", representativeName='" + representativeName + '\'' +
                ", contactPersonName='" + contactPersonName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", contractStartDate=" + contractStartDate +
                ", meetingHistory='" + meetingHistory + '\'' +
                ", referralRecord='" + referralRecord + '\'' +
                ", priority=" + priority +
                ", usersCount=" + (users != null ? users.size() : 0) +
                '}';
    }

    public enum Priority {
        高, 中, 低
    }
}