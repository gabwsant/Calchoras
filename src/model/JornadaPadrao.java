package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JornadaPadrao {
    private LocalTime entrada, saida;
    private boolean atravessaMeiaNoite;

    public JornadaPadrao(LocalTime entrada, LocalTime saida) {
        this.entrada = entrada;
        this.saida = saida;
        this.atravessaMeiaNoite = entrada.isAfter(saida);
    }

    public LocalTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalTime entrada) {
        this.entrada = entrada;
    }

    public LocalTime getSaida() {
        return saida;
    }

    public void setSaida(LocalTime saida) {
        this.saida = saida;
    }

    public boolean isAtravessaMeiaNoite() {
        return atravessaMeiaNoite;
    }

    public long getJornadaPadrao() {
        return Duration.between(entrada, saida).toMinutes() - 60; // desconta 1h almoço
    }
}
