package com.calchoras.service;

import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import com.calchoras.model.DailyCalculationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@DisplayName("Testes para o DailyCalculationService")
class DailyCalculationServiceTest {

    private DailyCalculationService service;
    private Employee standardEmployee;
    private final LocalDate TODAY = LocalDate.now();

    @BeforeEach
    void setUp() {
        service = new DailyCalculationService();
        standardEmployee = new Employee(
                1,
                "Funcionario Teste",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60);
    }

    @Test
    @DisplayName("1.1. Deve gerar um resultado para um dia com 1 hora extra")
    void testCalculate_WithOvertime_ShouldReturnCorrectValues() {
        TimeEntry timeEntry = new TimeEntry(
                1, 1,
                TODAY,
                LocalTime.parse("08:00"),
                LocalTime.parse("12:00"),
                LocalTime.parse("13:00"),
                LocalTime.parse("18:00"),
                false // isDayOff
        );

        DailyCalculationResult result = service.calculate(timeEntry, standardEmployee);

        assertEquals(Duration.ofHours(9), result.workedHours(), "Horas trabalhadas deve ser 9h.");
        assertEquals(Duration.ofHours(8), result.expectedHours(), "Horas esperadas deve ser 8h.");
        assertEquals(Duration.ofHours(1), result.overtimeHours(), "Deve ter 1 hora extra.");
        assertEquals(Duration.ZERO, result.negativeHours(), "Não deve ter horas negativas.");
        assertFalse(result.isIncomplete(), "Deve estar completo.");
    }

    @Test
    @DisplayName("1.2. Deve gerar um resultado para um dia com 1 hora negativa (Subtrabalho)")
    void testCalculate_WithNegativeHours_ShouldReturnCorrectValues() {
        TimeEntry timeEntry = new TimeEntry(
                2, 1,
                TODAY,
                LocalTime.parse("08:00"),
                LocalTime.parse("12:00"),
                LocalTime.parse("13:00"),
                LocalTime.parse("16:00"),
                false
        );

        DailyCalculationResult result = service.calculate(timeEntry, standardEmployee);

        assertEquals(Duration.ofHours(7), result.workedHours(), "Horas trabalhadas deve ser 7h.");
        assertEquals(Duration.ofHours(8), result.expectedHours(), "Horas esperadas deve ser 8h.");
        assertEquals(Duration.ZERO, result.overtimeHours(), "Não deve ter horas extras.");
        assertEquals(Duration.ofHours(1), result.negativeHours(), "Deve ter 1 hora negativa.");
        assertFalse(result.isIncomplete(), "Deve estar completo.");
    }

    @Test
    @DisplayName("1.3. Deve gerar um resultado neutro para jornada exata")
    void testCalculate_ExactShift_ShouldReturnZeroBalance() {
        TimeEntry timeEntry = new TimeEntry(
                3, 1,
                TODAY,
                LocalTime.parse("08:00"),
                LocalTime.parse("12:00"),
                LocalTime.parse("13:00"),
                LocalTime.parse("17:00"),
                false
        );

        DailyCalculationResult result = service.calculate(timeEntry, standardEmployee);

        assertEquals(Duration.ofHours(8), result.workedHours(), "Horas trabalhadas deve ser 8h.");
        assertEquals(Duration.ofHours(8), result.expectedHours(), "Horas esperadas deve ser 8h.");
        assertEquals(Duration.ZERO, result.overtimeHours(), "Horas extras deve ser zero.");
        assertEquals(Duration.ZERO, result.negativeHours(), "Horas negativas deve ser zero.");
        assertFalse(result.isIncomplete(), "Deve estar completo.");
    }

    @Test
    @DisplayName("2.1. Deve retornar INCOMPLETO quando o Clock Out estiver faltando")
    void testCalculate_MissingClockOut_ShouldReturnIncomplete() {
        TimeEntry timeEntry = new TimeEntry(
                4, 1,
                TODAY,
                LocalTime.parse("08:00"),
                LocalTime.parse("12:00"),
                LocalTime.parse("13:00"),
                null,
                false
        );

        DailyCalculationResult result = service.calculate(timeEntry, standardEmployee);

        Duration expectedHours = Duration.ofHours(8);

        assertEquals(Duration.ZERO, result.workedHours(), "Horas trabalhadas deve ser zero.");
        assertEquals(expectedHours, result.expectedHours(), "Horas esperadas deve ser 8h.");
        assertEquals(Duration.ZERO, result.overtimeHours(), "Horas extras deve ser zero.");
        assertEquals(expectedHours, result.negativeHours(), "Horas negativas deve ser igual ao esperado (8h).");
        assertTrue(result.isIncomplete(), "O status isIncomplete deve ser TRUE.");
    }

    @Test
    @DisplayName("2.2. Deve retornar ZERO para um Day Off (Folga)")
    void testCalculate_DayOff_ShouldReturnZeroDurations() {
        TimeEntry timeEntry = new TimeEntry(
                5, 1,
                TODAY,
                LocalTime.parse("08:00"),
                LocalTime.parse("12:00"),
                LocalTime.parse("13:00"),
                LocalTime.parse("17:00"),
                true // isDayOff
        );

        DailyCalculationResult result = service.calculate(timeEntry, standardEmployee);

        assertEquals(Duration.ZERO, result.workedHours(), "Horas trabalhadas deve ser zero.");
        assertEquals(Duration.ZERO, result.expectedHours(), "Horas esperadas deve ser zero.");
        assertEquals(Duration.ZERO, result.overtimeHours(), "Horas extras deve ser zero.");
        assertEquals(Duration.ZERO, result.negativeHours(), "Horas negativas deve ser zero.");
        assertFalse(result.isIncomplete(), "Deve estar completo (por ser folga, é um registro válido).");
    }

    @Test
    @DisplayName("3.1. Deve calcular corretamente uma jornada noturna que vira o dia")
    void testCalculate_OvernightShift_ShouldReturnCorrectValues() {
        Employee overnightEmployee = new Employee(
                2,
                "Func. Noturno",
                LocalTime.of(22, 0),
                LocalTime.of(7, 0),
                60);

        TimeEntry timeEntry = new TimeEntry(
                6, 1,
                TODAY,
                LocalTime.parse("22:00"),
                LocalTime.parse("02:00"),
                LocalTime.parse("03:00"),
                LocalTime.parse("07:00"),
                false
        );

        DailyCalculationResult result = service.calculate(timeEntry, overnightEmployee);

        Duration eightHours = Duration.ofHours(8);

        assertEquals(eightHours, result.workedHours(), "Horas trabalhadas deve ser 8h.");
        assertEquals(eightHours, result.expectedHours(), "Horas esperadas deve ser 8h.");
        assertEquals(Duration.ZERO, result.overtimeHours(), "Horas extras deve ser zero.");
        assertEquals(Duration.ZERO, result.negativeHours(), "Horas negativas deve ser zero.");
        assertFalse(result.isIncomplete(), "Deve estar completo.");
    }
}