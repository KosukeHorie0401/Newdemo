package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}
