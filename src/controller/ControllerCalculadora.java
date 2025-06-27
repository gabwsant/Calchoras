package controller;

import model.BatidaPonto;
import model.CalculadoraHorasExtras;
import model.JornadaPadrao;
import model.ResultadoHoras;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ControllerCalculadora {

    public ResultadoHoras calcular(List<BatidaPonto> batidas, JornadaPadrao jornada) {
        long horasExtras = 0;
        long horasNegativas = 0;

        CalculadoraHorasExtras calculadoraHorasExtras = new CalculadoraHorasExtras();

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

            calculadoraHorasExtras.calcularHorasExtras(trabalhado, jornadaEsperada);
            horasExtras += calculadoraHorasExtras.getHorasExtras();
            horasNegativas += calculadoraHorasExtras.getHorasNegativas();
        }

        return new ResultadoHoras(horasExtras, horasNegativas);
    }
}
