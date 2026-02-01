package com.calchoras.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private int id;
    private String name;

    public Company(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
