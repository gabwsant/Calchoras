package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JornadaPadrao {
    private LocalTime entrada, saida;
    private LocalDate data;

    public JornadaPadrao(LocalDate data, LocalTime entrada, LocalTime saida) {
        this.data = data;
        this.entrada = entrada;
        this.saida = saida;
    }

    public long getJornadaPadrao() {
        LocalDate dataFim = data;
        if (saida.isBefore(entrada)) {
            dataFim = data.plusDays(1);
        }

        LocalDateTime dataInicio = LocalDateTime.of(data, entrada);
        LocalDateTime dataFimDateTime = LocalDateTime.of(dataFim, saida);

        return Duration.between(dataInicio, dataFimDateTime).toMinutes() - 60; // desconta 1h almoço
    }
}
