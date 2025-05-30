package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class BatidaPonto {
    private LocalDate data;
    private LocalTime entrada, saidaAlmoco, voltaAlmoco, saida;

    public BatidaPonto(LocalDate data, LocalTime entrada, LocalTime saidaAlmoco,
                       LocalTime voltaAlmoco, LocalTime saida) {
        this.data = data;
        this.entrada = entrada;
        this.saidaAlmoco = saidaAlmoco;
        this.voltaAlmoco = voltaAlmoco;
        this.saida = saida;
    }

    public long getMinutosTrabalhados() {
        return Duration.between(entrada, saidaAlmoco).toMinutes()
                + Duration.between(voltaAlmoco, saida).toMinutes();
    }
}
