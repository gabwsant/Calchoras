package com.calchoras.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Employee {
    private int id;
    private int companyId;
    private String name;
    private LocalTime shiftIn;
    private LocalTime shiftOut;
    private long lunchBreakMinutes;

    public Employee(
            int companyId,
            String name,
            LocalTime shiftIn,
            LocalTime shiftOut,
            long lunchBreakMinutes) {
        this.companyId = companyId;
        this.name = name;
        this.shiftIn = shiftIn;
        this.shiftOut = shiftOut;
        this.lunchBreakMinutes = lunchBreakMinutes;
    }

    public String toString() {
        return name;
    }
}
