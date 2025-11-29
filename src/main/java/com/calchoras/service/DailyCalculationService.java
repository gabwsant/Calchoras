package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.IDailyCalculationService;

import java.time.Duration;
import java.time.LocalTime;

public class DailyCalculationService implements IDailyCalculationService {

    /**
     * Calcula o resultado da jornada diária (workedHours, expectedHours, balance)
     * e define o status de incompletude do registro.
     *
     * @param timeEntry O registro de ponto do dia.
     * @param employee O funcionário com os dados da jornada esperada.
     * @return O resultado do cálculo diário, incluindo o status 'isIncomplete'.
     */
    @Override
    public DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee) {

        // --- 1. Pré-cálculo da Duração Esperada da Jornada ---
        Duration expectedHours = calculateExpectedHours(employee, timeEntry.isDayOff());

        // --- 2. Preparação dos dados e Verificação de Inconsistências ---

        LocalTime clockIn = timeEntry.getClockIn();
        LocalTime lunchIn = timeEntry.getLunchIn();
        LocalTime lunchOut = timeEntry.getLunchOut();
        LocalTime clockOut = timeEntry.getClockOut();

        // Verifica se o dia é de folga. Se for, retorna zero, status COMPLETO.
        if (timeEntry.isDayOff()) {
            return new DailyCalculationResult(
                    timeEntry.getEntryDate(),
                    Duration.ZERO, // workedHours
                    expectedHours, // expectedHours (que será ZERO)
                    Duration.ZERO, // overtimeHours
                    Duration.ZERO, // negativeHours
                    false          // isIncomplete
            );
        }

        // Verifica se algum dos 4 pontos essenciais está ausente.
        if (clockIn == null || lunchIn == null || lunchOut == null || clockOut == null) {

            // Se faltar algum ponto, o registro é considerado INCOMPLETO.
            return new DailyCalculationResult(
                    timeEntry.getEntryDate(),
                    Duration.ZERO,      // Horas trabalhadas: 0
                    expectedHours,      // Horas esperadas
                    Duration.ZERO,      // Horas extras: 0
                    expectedHours,      // Horas negativas = Horas esperadas
                    true                // INCOMPLETO
            );
        }

        // --- 3. Calcular a Duração Efetivamente Trabalhada ---
        Duration workedHours = calculateWorkedHours(clockIn, lunchIn, lunchOut, clockOut);


        // --- 4. Calcular o Saldo (Horas Extras e Negativas) ---

        Duration balance = workedHours.minus(expectedHours);

        Duration overtimeHours;
        Duration negativeHours;

        // Se o saldo for negativo (trabalhou menos que o esperado)
        if (balance.isNegative()) {
            overtimeHours = Duration.ZERO;
            // Usamos .abs() para obter o valor absoluto da diferença negativa.
            negativeHours = balance.abs();
        } else {
            overtimeHours = balance;
            negativeHours = Duration.ZERO;
        }

        // --- 5. Retornar o Resultado Completo (COMPLETO) ---

        return new DailyCalculationResult(
                timeEntry.getEntryDate(),
                workedHours,
                expectedHours,
                overtimeHours,
                negativeHours,
                false // COMPLETO
        );
    }

    // ------------------------------------------
    // --- MÉTODOS PRIVADOS DE APOIO ---
    // ------------------------------------------

    /**
     * Calcula a duração total esperada para o dia (Jornada total - Intervalo de Almoço).
     */
    private Duration calculateExpectedHours(Employee employee, boolean isDayOff) {
        if (isDayOff) {
            return Duration.ZERO;
        }

        LocalTime shiftIn = employee.getShiftIn();
        LocalTime shiftOut = employee.getShiftOut();

        // Cálculo da jornada bruta (entrada esperada até saída esperada)
        Duration shiftDuration = Duration.between(shiftIn, shiftOut);

        // Trata a jornada esperada que passa pela meia-noite (turnos noturnos esperados)
        if (shiftOut.isBefore(shiftIn)) {
            shiftDuration = shiftDuration.plusDays(1);
        }

        return shiftDuration.minusMinutes(employee.getLunchBreakMinutes());
    }

    /**
     * Calcula a duração total trabalhada (Turno manhã + Turno tarde).
     */
    private Duration calculateWorkedHours(LocalTime clockIn, LocalTime lunchIn, LocalTime lunchOut, LocalTime clockOut) {

        // Turno da Manhã: Entrada até Almoço
        Duration morningShift = Duration.between(clockIn, lunchIn);
        // Trata a passagem pela meia-noite
        if (lunchIn.isBefore(clockIn)) {
            morningShift = morningShift.plusDays(1);
        }

        // Turno da Tarde: Retorno do Almoço até Saída
        Duration afternoonShift = Duration.between(lunchOut, clockOut);
        // Trata a passagem pela meia-noite
        if (clockOut.isBefore(lunchOut)) {
            afternoonShift = afternoonShift.plusDays(1);
        }

        return morningShift.plus(afternoonShift);
    }
}