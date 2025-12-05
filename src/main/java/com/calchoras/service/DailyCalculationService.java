package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.IDailyCalculationService;

import java.time.Duration;
import java.time.LocalTime;

public class DailyCalculationService implements IDailyCalculationService {

    @Override
    public DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee) {

        Duration expectedHours = calculateExpectedHours(employee, timeEntry.isDayOff());

        LocalTime clockIn = timeEntry.getClockIn();
        LocalTime lunchIn = timeEntry.getLunchIn();
        LocalTime lunchOut = timeEntry.getLunchOut();
        LocalTime clockOut = timeEntry.getClockOut();

        if (timeEntry.isDayOff()) {
            return new DailyCalculationResult(
                    timeEntry.getEntryDate(),
                    Duration.ZERO, // workedHours
                    expectedHours, // expectedHours
                    Duration.ZERO, // overtimeHours
                    Duration.ZERO, // negativeHours
                    false          // isIncomplete
            );
        }

        if (clockIn == null || lunchIn == null || lunchOut == null || clockOut == null) {

            return new DailyCalculationResult(
                    timeEntry.getEntryDate(),
                    Duration.ZERO,      // workedHours: 0
                    expectedHours,      // expectedHours
                    Duration.ZERO,      // overtimeHours: 0
                    expectedHours,      // negativeHours = expectedHours
                    true                // isIncomplete
            );
        }

        Duration workedHours = calculateWorkedHours(clockIn, lunchIn, lunchOut, clockOut);

        Duration balance = workedHours.minus(expectedHours);

        Duration overtimeHours;
        Duration negativeHours;

        if (balance.isNegative()) {
            overtimeHours = Duration.ZERO;
            negativeHours = balance.abs();
        } else {
            overtimeHours = balance;
            negativeHours = Duration.ZERO;
        }

        return new DailyCalculationResult(
                timeEntry.getEntryDate(),
                workedHours,
                expectedHours,
                overtimeHours,
                negativeHours,
                false // isIncomplete
        );
    }

    /**
     * Calculates the expected total duration for the day (Total working hours - Lunch break).
     */
    private Duration calculateExpectedHours(Employee employee, boolean isDayOff) {
        if (isDayOff) {
            return Duration.ZERO;
        }

        LocalTime shiftIn = employee.getShiftIn();
        LocalTime shiftOut = employee.getShiftOut();

        Duration shiftDuration = Duration.between(shiftIn, shiftOut);

        if (shiftOut.isBefore(shiftIn)) {
            shiftDuration = shiftDuration.plusDays(1);
        }

        return shiftDuration.minusMinutes(employee.getLunchBreakMinutes());
    }

    /**
     * Calculates the worked total duration (morning shift + afternoon shift).
     */
    private Duration calculateWorkedHours(LocalTime clockIn, LocalTime lunchIn, LocalTime lunchOut, LocalTime clockOut) {

        Duration morningShift = Duration.between(clockIn, lunchIn);
        if (lunchIn.isBefore(clockIn)) {
            morningShift = morningShift.plusDays(1);
        }

        Duration afternoonShift = Duration.between(lunchOut, clockOut);
        if (clockOut.isBefore(lunchOut)) {
            afternoonShift = afternoonShift.plusDays(1);
        }

        return morningShift.plus(afternoonShift);
    }
}