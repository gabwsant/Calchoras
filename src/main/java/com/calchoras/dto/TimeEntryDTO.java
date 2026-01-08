package com.calchoras.dto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record TimeEntryDTO(
        int id,
        int employeeId,
        LocalDate entryDate,
        LocalTime clockIn,
        LocalTime lunchIn,
        LocalTime lunchOut,
        LocalTime clockOut,
        boolean dayOff
) {
    public String getFormattedTotalTime() {
        if (dayOff) {
            return "Folga";
        }

        if (clockIn == null || clockOut == null) {
            return "--:--";
        }

        Duration totalDuration = Duration.between(clockIn, clockOut);

        if (lunchIn != null && lunchOut != null) {
            Duration lunchDuration = Duration.between(lunchIn, lunchOut);
            totalDuration = totalDuration.minus(lunchDuration);
        }

        long hours = totalDuration.toHours();
        long minutes = totalDuration.toMinutesPart();

        return String.format("%02d:%02d", hours, minutes);
    }
}
