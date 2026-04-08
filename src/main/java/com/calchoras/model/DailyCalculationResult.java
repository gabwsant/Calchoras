package com.calchoras.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DailyCalculationResult(
        LocalDate date,
        List<LocalTime> punches,
        Duration workedHours,
        Duration expectedHours,
        Duration overtimeHours,
        Duration negativeHours,
        boolean isIncomplete
) {}