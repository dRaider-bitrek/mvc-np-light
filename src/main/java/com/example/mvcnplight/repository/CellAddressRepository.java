package com.example.mvcnplight.repository;

import com.example.mvcnplight.entity.CellAddressEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CellAddressRepository extends MongoRepository<CellAddressEntity,String> {
}
