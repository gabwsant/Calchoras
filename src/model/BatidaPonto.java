package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;

public class BatidaPonto {
    private LocalDate data;
    private final LocalDateTime entrada, saidaAlmoco, voltaAlmoco, saida;
    public BatidaPonto(LocalDateTime entrada,
                       LocalDateTime saidaAlmoco,
                       LocalDateTime voltaAlmoco,
                       LocalDateTime saida) {
        this.data = entrada.toLocalDate();
        this.entrada = entrada;
        this.saidaAlmoco = saidaAlmoco;
        this.voltaAlmoco = voltaAlmoco;
        this.saida = saida;
    }

    public long getMinutosTrabalhados() {
        return Duration.between(entrada, saidaAlmoco).toMinutes()
                + Duration.between(voltaAlmoco, saida).toMinutes();
    }

    public boolean isFolga() {
        return entrada != null && entrada.equals(saidaAlmoco) &&
                entrada.equals(voltaAlmoco) && entrada.equals(saida);
    }

    public LocalDate getData() {
        return data;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public LocalDateTime getSaidaAlmoco() {
        return saidaAlmoco;
    }

    public LocalDateTime getVoltaAlmoco() {
        return voltaAlmoco;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

}
