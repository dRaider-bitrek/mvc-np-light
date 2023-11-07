package com.example.mvcnplight.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
public class DeviceEntity {
    @Id
    private String imei;
    private String hw;
    private String sw;
}
