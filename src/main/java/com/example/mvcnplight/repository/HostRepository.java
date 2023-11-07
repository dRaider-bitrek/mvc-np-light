package com.example.mvcnplight.repository;

import com.example.mvcnplight.entity.HostEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HostRepository extends MongoRepository<HostEntity,String> {

}
