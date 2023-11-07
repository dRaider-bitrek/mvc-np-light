package com.example.mvcnplight.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class HostEntity {
    private String imei;
}
