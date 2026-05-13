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
    public DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee, int dailyAllowed, int perPunchAllowed) throws IllegalStateException{

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

        long clockInVariance = calculateShortestVariance(employee.getShiftIn(), clockIn);
        long clockOutVariance = calculateShortestVariance(employee.getShiftOut(), clockOut);

        long actualLunchMinutes = Duration.between(lunchIn, lunchOut).toMinutes();
        if (lunchOut.isBefore(lunchIn)) {
            actualLunchMinutes = Duration.between(lunchIn, lunchOut).plusDays(1).toMinutes();
        }
        long lunchVariance = Math.abs(actualLunchMinutes - employee.getLunchBreakMinutes());

        boolean exceededPunchTolerance = clockInVariance > perPunchAllowed ||
                clockOutVariance > perPunchAllowed ||
                lunchVariance > perPunchAllowed;

        // 3. Checks if it exceeded the TOTAL DAILY tolerance
        boolean exceededDailyTolerance = Math.abs(balance.toMinutes()) > dailyAllowed;

        // 4. Applies the rule
        if (exceededPunchTolerance || exceededDailyTolerance) {
            // Tolerance breached! Accrues the real balance.
            if (balance.isNegative()) {
                negativeHours = balance.abs();
            } else {
                overtimeHours = balance;
            }
        } else {
            // Within tolerance! The balance is "forgiven" and the variables remain Duration.ZERO
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

    /**
     * Calculates the shortest absolute variance in minutes between two LocalTimes,
     * properly handling cases that cross midnight.
     */
    private long calculateShortestVariance(LocalTime expected, LocalTime actual) {
        long absoluteDifference = Math.abs(Duration.between(expected, actual).toMinutes());
        // 1440 is the total number of minutes in 24 hours.
        // We take the minimum between the direct difference and the wrapped difference.
        return Math.min(absoluteDifference, 1440 - absoluteDifference);
    }
}