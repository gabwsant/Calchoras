package model;

import java.time.Duration;
import java.time.LocalTime;

public class JornadaPadrao {
    private LocalTime entrada, saida;
    private long jornada;

    public JornadaPadrao(LocalTime entrada, LocalTime saida) {
        this.entrada = entrada;
        this.saida = saida;
    }

    public long getJornadaPadrao() {
        return Duration.between(entrada, saida).toMinutes() - 60;
    }
}
