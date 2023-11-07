package com.example.mvcnplight.repository;

import com.example.mvcnplight.entity.DeviceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<DeviceEntity, String> {
}
