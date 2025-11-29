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
        // Jornada Esperada: 8h às 17h, 60 min almoço = 8 horas de trabalho líquido
        standardEmployee = new Employee(
                1,
                "Funcionario Teste",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60);
    }

    // --- 1. CASOS DE BALANÇO (Worked Hours vs. Expected Hours) ---

    @Test
    @DisplayName("1.1. Deve gerar um resultado para um dia com 1 hora extra")
    void testCalculate_WithOvertime_ShouldReturnCorrectValues() {
        TimeEntry timeEntry = new TimeEntry(
                1, 1,
                TODAY,
                LocalTime.parse("08:00"),
                LocalTime.parse("12:00"),
                LocalTime.parse("13:00"),
                LocalTime.parse("18:00"), // Saiu 1h mais tarde
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
                LocalTime.parse("16:00"), // Saiu 1h mais cedo
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
                LocalTime.parse("17:00"), // Jornada exata
                false
        );

        DailyCalculationResult result = service.calculate(timeEntry, standardEmployee);

        assertEquals(Duration.ofHours(8), result.workedHours(), "Horas trabalhadas deve ser 8h.");
        assertEquals(Duration.ofHours(8), result.expectedHours(), "Horas esperadas deve ser 8h.");
        assertEquals(Duration.ZERO, result.overtimeHours(), "Horas extras deve ser zero.");
        assertEquals(Duration.ZERO, result.negativeHours(), "Horas negativas deve ser zero.");
        assertFalse(result.isIncomplete(), "Deve estar completo.");
    }

    // --- 2. CASOS DE INCONSISTÊNCIA E EXCEÇÃO ---

    @Test
    @DisplayName("2.1. Deve retornar INCOMPLETO quando o Clock Out estiver faltando")
    void testCalculate_MissingClockOut_ShouldReturnIncomplete() {
        // Clock Out está nulo
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
        // isDayOff está TRUE
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

    // --- 3. CASOS DE JORNADA NOTURNA ---

    @Test
    @DisplayName("3.1. Deve calcular corretamente uma jornada noturna que vira o dia")
    void testCalculate_OvernightShift_ShouldReturnCorrectValues() {
        // Jornada esperada noturna: 22h às 7h, 60 min almoço = 8 horas de trabalho líquido.
        Employee overnightEmployee = new Employee(
                2,
                "Func. Noturno",
                LocalTime.of(22, 0), // 22:00 (dia 1)
                LocalTime.of(7, 0),  // 07:00 (dia 2)
                60);

        // Ponto batido: 22:00 (In) -> 02:00 (LI) -> 03:00 (LO) -> 07:00 (Out) = 8h trabalhadas (Exato)
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