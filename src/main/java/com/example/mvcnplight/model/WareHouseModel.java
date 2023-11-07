package com.example.mvcnplight.model;

import lombok.Data;

import java.util.List;

@Data
public class WareHouseModel {
    private String warehouse;
    private List<HostModel> data;

    @Override
    public String toString() {
        return "WareHouseModel{" +
                "warehouse='" + warehouse + '\'' +
                ", data=" + data +
                '}';
    }
}
