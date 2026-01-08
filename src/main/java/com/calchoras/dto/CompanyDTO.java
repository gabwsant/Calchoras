package com.calchoras.dto;

public record CompanyDTO(
        int id,
        String name,
        int numOfEmployees
) {
    @Override
    public String toString() {
        return name;
    }
}
