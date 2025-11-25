package com.calchoras.model;

import java.time.Duration;
import java.time.LocalDate;

public record DailyCalculationResult(
        LocalDate date,
        Duration workedHours,
        Duration expectedHours,
        Duration overtimeHours,
        Duration negativeHours
) {}