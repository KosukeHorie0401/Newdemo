package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.TestEntity;
import com.example.demo.repository.TestEntityRepository;

@Service
public class TestEntityService {
    @Autowired
    private TestEntityRepository repository;

    public TestEntity saveTestEntity(TestEntity testEntity) {
        return repository.save(testEntity);
    }

    public List<TestEntity> getAllTestEntities() {
        return repository.findAll();
    }
}
