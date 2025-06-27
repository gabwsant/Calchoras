package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CalculadoraHorasExtras {

    public ResultadoHoras calcularHorasExtras(List<BatidaPonto> batidas, JornadaPadrao jornada) {
        long horasExtras = 0;
        long horasNegativas = 0;

        for (BatidaPonto b : batidas) {
            LocalDate data = b.getData();

            LocalDateTime entradaEsperada = LocalDateTime.of(data, jornada.getEntrada());
            LocalDateTime saidaEsperada = LocalDateTime.of(data, jornada.getSaida());

            if (jornada.isAtravessaMeiaNoite()) {
                saidaEsperada = saidaEsperada.plusDays(1);
            }

            long trabalhado = b.getMinutosTrabalhados();
            long jornadaEsperada =
                    Duration.between(entradaEsperada, saidaEsperada).toMinutes() - 60;

            System.out.println(entradaEsperada + " " + saidaEsperada);

            if (trabalhado > jornadaEsperada) {
                horasExtras += trabalhado - jornadaEsperada;
            }else if(trabalhado < jornadaEsperada){
                horasNegativas += jornadaEsperada - trabalhado;
            }
        }
        return new ResultadoHoras(horasExtras, horasNegativas);
    }
}