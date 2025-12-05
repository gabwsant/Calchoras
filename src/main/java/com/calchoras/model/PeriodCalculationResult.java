package com.calchoras.model;

import java.time.Duration;
import java.util.List;

public record PeriodCalculationResult(
        List<DailyCalculationResult> dailyResults,
        Duration totalOvertimeAccumulated,
        Duration totalNegativeHoursAccumulated,
        Duration finalOvertime,
        Duration finalNegative,
        int incompleteEntriesCount
) {}