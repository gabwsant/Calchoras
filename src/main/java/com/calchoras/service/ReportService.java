package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.PeriodCalculationResult;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.IDailyCalculationService;
import com.calchoras.service.interfaces.IReportService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReportService implements IReportService {

    private final IDailyCalculationService dailyCalculationService;

    public ReportService(IDailyCalculationService dailyCalculationService) {
        this.dailyCalculationService = dailyCalculationService;
    }

    @Override
    public PeriodCalculationResult calculatePeriodBalance(Employee employee, List<TimeEntry> entries){
        List<DailyCalculationResult> dailyResults = new ArrayList<>();
        Duration totalOvertimeAccumulated = Duration.ZERO;
        Duration totalNegativeHoursAccumulated = Duration.ZERO;
        int incompleteEntriesCount = 0;

        for (TimeEntry entry : entries) {
            DailyCalculationResult dailyResult = dailyCalculationService.calculate(entry, employee);

            dailyResults.add(dailyResult);

            totalOvertimeAccumulated = totalOvertimeAccumulated.plus(dailyResult.overtimeHours());
            totalNegativeHoursAccumulated = totalNegativeHoursAccumulated.plus(dailyResult.negativeHours());

            if (dailyResult.isIncomplete()) {
                incompleteEntriesCount++;
            }
        }

        // --- 1. Cálculo do Saldo Final Líquido ---

        // Saldo total é o total de horas extras menos o total de horas negativas
        Duration finalBalance = totalOvertimeAccumulated.minus(totalNegativeHoursAccumulated);

        Duration finalOvertime = Duration.ZERO;
        Duration finalNegative = Duration.ZERO;

        // Determinar se o saldo final é positivo ou negativo
        if (finalBalance.isNegative()) {
            finalNegative = finalBalance.abs(); // O valor absoluto das horas a dever
        } else {
            finalOvertime = finalBalance; // O valor das horas a receber
        }

        // --- 2. Retornar o Resultado Completo do Período ---

        return new PeriodCalculationResult(
                dailyResults,
                totalOvertimeAccumulated,
                totalNegativeHoursAccumulated,
                finalOvertime,
                finalNegative,
                incompleteEntriesCount
        );
    }
}