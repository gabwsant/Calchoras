package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.IDailyCalculationService;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class DailyCalculationService implements IDailyCalculationService {

    @Override
    public DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee) throws IllegalStateException{

        Duration expectedHours = calculateExpectedHours(employee, timeEntry.isDayOff());

        LocalTime clockIn = timeEntry.getClockIn();
        LocalTime lunchIn = timeEntry.getLunchIn();
        LocalTime lunchOut = timeEntry.getLunchOut();
        LocalTime clockOut = timeEntry.getClockOut();

        List<LocalTime> punches = Arrays.asList(clockIn, lunchIn, lunchOut, clockOut);

        if (timeEntry.isDayOff()) {
            return new DailyCalculationResult(
                    timeEntry.getEntryDate(),
                    punches,
                    Duration.ZERO, // workedHours
                    expectedHours, // expectedHours
                    Duration.ZERO, // overtimeHours
                    Duration.ZERO, // negativeHours
                    false          // isIncomplete
            );
        }

        if (clockIn == null && lunchIn == null && lunchOut == null && clockOut == null) {
            return new DailyCalculationResult(
                    timeEntry.getEntryDate(),
                    punches,
                    Duration.ZERO, // workedHours
                    expectedHours, // expectedHours
                    Duration.ZERO, // overtimeHours
                    expectedHours, // negativeHours
                    true          // isIncomplete
            );
        }

        boolean isIncomplete = clockIn == null || lunchIn == null || lunchOut == null || clockOut == null;

        Duration workedHours = calculateWorkedHours(clockIn, lunchIn, lunchOut, clockOut);

        Duration balance = workedHours.minus(expectedHours);
        Duration overtimeHours = Duration.ZERO;
        Duration negativeHours = Duration.ZERO;

        if (balance.isNegative()) {
            negativeHours = balance.abs();
        } else {
            overtimeHours = balance;
        }

        return new DailyCalculationResult(
                timeEntry.getEntryDate(),
                punches,
                workedHours,
                expectedHours,
                overtimeHours,
                negativeHours,
                isIncomplete
        );
    }

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

    private Duration calculateWorkedHours(LocalTime clockIn, LocalTime lunchIn, LocalTime lunchOut, LocalTime clockOut) {
        Duration totalWorked = Duration.ZERO;

        if (clockIn != null && lunchIn != null) {
            Duration morningShift = Duration.between(clockIn, lunchIn);
            if (lunchIn.isBefore(clockIn)) {
                morningShift = morningShift.plusDays(1);
            }
            totalWorked = totalWorked.plus(morningShift);
        }

        if (lunchOut != null && clockOut != null) {
            Duration afternoonShift = Duration.between(lunchOut, clockOut);
            if (clockOut.isBefore(lunchOut)) {
                afternoonShift = afternoonShift.plusDays(1);
            }
            totalWorked = totalWorked.plus(afternoonShift);
        }

        return totalWorked;
    }
}