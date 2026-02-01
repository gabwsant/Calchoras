package com.calchoras.dto;

public record CompanyDTO(
        int id,
        String name
) {
    @Override
    public String toString() {
        return name;
    }
}
