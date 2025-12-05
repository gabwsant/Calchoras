package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.PeriodCalculationResult;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.IDailyCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para o ReportService")
class ReportServiceTest {

    private ReportService reportService;
    private Employee sampleEmployee;

    private static class MockDailyCalculationService implements IDailyCalculationService {
        @Override
        public DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee) {
            return new DailyCalculationResult(
                    timeEntry.getEntryDate(),
                    Duration.ofHours(8),
                    Duration.ofHours(8).plusMinutes(5),
                    Duration.ofMinutes(10),
                    Duration.ofMinutes(15),
                    false
            );
        }
    }

    @BeforeEach
    void setUp() {
        IDailyCalculationService mockDailyCalculator = new MockDailyCalculationService();

        reportService = new ReportService(mockDailyCalculator);

        sampleEmployee = new Employee(1, "Funcionário Mock", LocalTime.of(8,0), LocalTime.of(17,0), 60);
    }

    @Test
    @DisplayName("Deve agregar corretamente os totais de múltiplas batidas de ponto")
    void calculateMonthlyBalance_withMultipleEntries_shouldAggregateTotals() {
       List<TimeEntry> entries = List.of(
                new TimeEntry(1, LocalDate.of(2025, 8, 1), LocalTime.of(8,0), LocalTime.of(12,0), LocalTime.of(13,0), LocalTime.of(17,10)),
                new TimeEntry(1, LocalDate.of(2025, 8, 2), LocalTime.of(8,0), LocalTime.of(12,0), LocalTime.of(13,0), LocalTime.of(16,45)),
                new TimeEntry(1, LocalDate.of(2025, 8, 3), LocalTime.of(8,0), LocalTime.of(12,0), LocalTime.of(13,0), LocalTime.of(17,0))
        );

        PeriodCalculationResult result = reportService.calculatePeriodBalance(sampleEmployee, entries);

        Duration expectedOvertime = Duration.ofMinutes(30);
        Duration expectedNegative = Duration.ofMinutes(45);

        assertNotNull(result, "O resultado não deveria ser nulo.");
        assertEquals(3, result.dailyResults().size(), "A lista de resultados diários deveria conter 3 itens.");
        assertEquals(expectedOvertime, result.totalOvertimeAccumulated(), "O total de horas extras está incorreto.");
        assertEquals(expectedNegative, result.totalNegativeHoursAccumulated(), "O total de horas negativas está incorreto.");
    }

    @Test
    @DisplayName("Deve retornar totais zerados para uma lista de batidas vazia")
    void calculateMonthlyBalance_withEmptyEntryList_shouldReturnZeroDurations() {
        List<TimeEntry> emptyList = Collections.emptyList();

        PeriodCalculationResult result = reportService.calculatePeriodBalance(sampleEmployee, emptyList);

        assertNotNull(result);
        assertTrue(result.dailyResults().isEmpty(), "A lista de resultados diários deveria estar vazia.");
        assertEquals(Duration.ZERO, result.totalOvertimeAccumulated(), "O total de horas extras deveria ser zero.");
        assertEquals(Duration.ZERO, result.totalNegativeHoursAccumulated(), "O total de horas negativas deveria ser zero.");
    }
}