package com.example.mvcnplight.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CellModel {
    private String cell;
    private int color;
    private int state;


    public int getColor() {
        return color-1;
    }

    @Override
    public String toString() {
        return "CellModel{" +
                "cell='" + cell + '\'' +
                ", color=" + color +
                ", state=" + state +
                '}';
    }
}
