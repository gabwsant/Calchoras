package com.calchoras.model;

import java.time.Duration;
import java.util.List;

/**
 * Guarda o resultado consolidado de um período de cálculo (ex: um mês).
 * @param dailyResults Uma lista com os detalhes de cada dia, para o relatório.
 * @param totalOvertimeAccumulated O total de horas extras BRUTAS no período.
 * @param totalNegativeHoursAccumulated O total de horas negativas BRUTAS no período.
 * @param finalOvertime O saldo final de horas (se positivo).
 * @param finalNegative O saldo final de horas (se negativo).
 * @param incompleteEntriesCount O número de dias com pontos incompletos/inválidos.
 */
public record PeriodCalculationResult(
        List<DailyCalculationResult> dailyResults,
        Duration totalOvertimeAccumulated,
        Duration totalNegativeHoursAccumulated,
        Duration finalOvertime,
        Duration finalNegative,
        int incompleteEntriesCount
) {}