package com.example.mvcnplight.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class CellAddressEntity {
    @Id
    private String cell;
    private String address;
    private String imei;

}
