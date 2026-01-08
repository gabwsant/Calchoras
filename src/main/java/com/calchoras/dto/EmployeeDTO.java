package com.calchoras.dto;

import java.time.LocalTime;

public record EmployeeDTO(
        int id,
        int companyId,
        String name,
        LocalTime shiftIn,
        LocalTime shiftOut,
        long lunchBreakMinutes,
        boolean active
) {
    @Override
    public String toString() {
        return name;
    }

    public String getShiftDescription() {
        return shiftIn + " - " + shiftOut;
    }
}
