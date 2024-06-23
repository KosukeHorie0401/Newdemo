package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}
