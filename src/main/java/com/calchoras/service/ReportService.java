package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.PeriodCalculationResult;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.IDailyCalculationService;
import com.calchoras.service.interfaces.IReportService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService implements IReportService {

    private final IDailyCalculationService dailyCalculationService;

    public ReportService(IDailyCalculationService dailyCalculationService) {
        this.dailyCalculationService = dailyCalculationService;
    }

    @Override
    public PeriodCalculationResult calculatePeriodBalance(Employee employee, List<TimeEntry> entries, LocalDate initialDate, LocalDate finalDate) {

        Map<LocalDate, TimeEntry> entriesMap = entries.stream()
                .collect(Collectors.toMap(TimeEntry::getEntryDate, entry -> entry));

        List<DailyCalculationResult> dailyResults = new ArrayList<>();
        Duration totalOvertimeAccumulated = Duration.ZERO;
        Duration totalNegativeHoursAccumulated = Duration.ZERO;
        int incompleteEntriesCount = 0;

        LocalDate currentDate = initialDate;

        while (!currentDate.isAfter(finalDate)) {

            TimeEntry entry = entriesMap.get(currentDate);

            if (entry == null) {
                entry = new TimeEntry();
                entry.setEntryDate(currentDate);
                entry.setDayOff(false);
            }

            DailyCalculationResult dailyResult = dailyCalculationService.calculate(entry, employee);

            dailyResults.add(dailyResult);

            totalOvertimeAccumulated = totalOvertimeAccumulated.plus(dailyResult.overtimeHours());
            totalNegativeHoursAccumulated = totalNegativeHoursAccumulated.plus(dailyResult.negativeHours());

            if (dailyResult.isIncomplete()) {
                incompleteEntriesCount++;
            }

            currentDate = currentDate.plusDays(1);
        }

        Duration finalBalance = totalOvertimeAccumulated.minus(totalNegativeHoursAccumulated);

        return new PeriodCalculationResult(
                dailyResults,
                totalOvertimeAccumulated,
                totalNegativeHoursAccumulated,
                finalBalance,
                incompleteEntriesCount
        );
    }
}