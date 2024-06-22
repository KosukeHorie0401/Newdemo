package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.TestEntity;
import com.example.demo.service.TestEntityService;

@RestController
@RequestMapping("/test-entity")
public class TestEntityController {
    @Autowired
    private TestEntityService service;

    @PostMapping
    public TestEntity createTestEntity(@RequestBody TestEntity testEntity) {
        return service.saveTestEntity(testEntity);
    }

    @GetMapping
    public List<TestEntity> getAllTestEntities() {
        return service.getAllTestEntities();
    }
}
