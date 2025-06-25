package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;

public class BatidaPonto {
    private LocalDateTime entrada, saidaAlmoco, voltaAlmoco, saida;

    public BatidaPonto(LocalDate data, LocalTime entrada, LocalTime saidaAlmoco,
                       LocalTime voltaAlmoco, LocalTime saida) {

        LocalDate dataFim = data.plusDays(1);
        this.entrada = LocalDateTime.of(data, entrada);
        this.saidaAlmoco = LocalDateTime.of(data, saidaAlmoco);
        this.voltaAlmoco = LocalDateTime.of(data, voltaAlmoco);
        this.saida = LocalDateTime.of(data, saida);

        //tratando fim da jornada no dia seguinte
        if (saidaAlmoco.isBefore(entrada)) {
            this.saidaAlmoco = LocalDateTime.of(dataFim, saidaAlmoco);
            this.voltaAlmoco = LocalDateTime.of(dataFim, voltaAlmoco);
            this.saida = LocalDateTime.of(dataFim, saida);
        }
        else if (voltaAlmoco.isBefore(saidaAlmoco)) {
            this.voltaAlmoco = LocalDateTime.of(dataFim, voltaAlmoco);
            this.saida = LocalDateTime.of(dataFim, saida);
        }

        if (saida.isBefore(voltaAlmoco)) {
            this.saida = LocalDateTime.of(dataFim, saida);
        }

    }

    public long getMinutosTrabalhados() {
        return Duration.between(entrada, saidaAlmoco).toMinutes()
                + Duration.between(voltaAlmoco, saida).toMinutes();
    }
}
