package com.example.mvcnplight.model;

import lombok.Data;

import java.util.List;

@Data
public class HostModel {
    private String host;
    private List<CellModel> cells;

    @Override
    public String toString() {
        return "HostModel{" +
                "host='" + host + '\'' +
                ", cells=" + cells +
                '}';
    }
}
