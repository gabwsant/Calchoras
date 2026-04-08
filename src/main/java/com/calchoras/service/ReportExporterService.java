package com.calchoras.service;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee; // Importe seu modelo
import com.calchoras.model.PeriodCalculationResult;
import com.calchoras.service.interfaces.IReportExporterService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReportExporterService implements IReportExporterService {

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void exportToTxt(PeriodCalculationResult result, Employee employee, String companyName, String filePath) throws IOException {

        String dataGeracao = LocalDateTime.now().format(DATETIME_FORMATTER);

        String periodo = descobrirPeriodo(result.dailyResults());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("=================================================");
            writer.newLine();
            writer.write("               RELATÓRIO DE HORAS                ");
            writer.newLine();
            writer.write("=================================================");
            writer.newLine();
            writer.write("Empresa:     " + companyName);
            writer.newLine();
            writer.write("Funcionário: " + employee.getName());
            writer.newLine();
            writer.write("Período:     " + periodo);
            writer.newLine();
            writer.write("Gerado em:   " + dataGeracao);
            writer.newLine();
            writer.write("=================================================");
            writer.newLine();
            writer.newLine();

            writer.write("--- Resumo do Período ---");
            writer.newLine();
            writer.write("Horas Extras Acumuladas: " + formatarDuracao(result.totalOvertimeAccumulated()));
            writer.newLine();
            writer.write("Horas Negativas Acumuladas: " + formatarDuracao(result.totalNegativeHoursAccumulated()));
            writer.newLine();
            writer.write("Saldo Final: " + formatarDuracao(result.finalBalance()));
            writer.newLine();
            writer.write("Registros Incompletos: " + result.incompleteEntriesCount());
            writer.newLine();
            writer.newLine();

            writer.write("--- Detalhamento Diário ---");
            writer.newLine();

            for (DailyCalculationResult daily : result.dailyResults()) {
                String dataFormatada = daily.date().format(DATE_FORMATTER);

                String horariosDigitados = daily.punches().stream()
                        .map(time -> time.format(TIME_FORMATTER))
                        .collect(Collectors.joining(" - "));

                String extras = formatarDuracao(daily.overtimeHours());
                String negativas = formatarDuracao(daily.negativeHours());

                String linhaDetalhe = String.format("Data: %s | Batidas: [ %s ] | Extras: %s | Negativas: %s",
                        dataFormatada,
                        horariosDigitados,
                        extras,
                        negativas
                );

                if (daily.isIncomplete()) {
                    linhaDetalhe += " (Atenção: Marcações incompletas)";
                }

                writer.write(linhaDetalhe);
                writer.newLine();
            }
        }
    }

    private String descobrirPeriodo(List<DailyCalculationResult> dailyResults) {
        if (dailyResults == null || dailyResults.isEmpty()) {
            return "Período não definido";
        }

        String dataInicio = dailyResults.getFirst().date().format(DATE_FORMATTER);
        String dataFim = dailyResults.getLast().date().format(DATE_FORMATTER);

        if (dataInicio.equals(dataFim)) {
            return dataInicio;
        }

        return dataInicio + " a " + dataFim;
    }

    private String formatarDuracao(Duration duration) {
        long totalMinutos = duration.toMinutes();
        long horas = totalMinutos / 60;
        long minutos = Math.abs(totalMinutos % 60);

        if (horas == 0 && totalMinutos < 0) {
            return String.format("-00:%02d", minutos);
        }

        return String.format("%02d:%02d", horas, minutos);
    }
}