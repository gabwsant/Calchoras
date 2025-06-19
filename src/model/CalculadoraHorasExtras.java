package model;

import java.util.List;

public class CalculadoraHorasExtras {

    public ResultadoHoras calcularHorasExtras(List<BatidaPonto> batidas, long jornada) {
        long horasExtras = 0;
        long horasNegativas = 0;

        for (BatidaPonto b : batidas) {
            long trabalhado = b.getMinutosTrabalhados();
            if (trabalhado > jornada) {
                horasExtras += trabalhado - jornada;
            }else if(trabalhado < jornada){
                horasNegativas += trabalhado - jornada;
            }
        }
        return new ResultadoHoras(horasExtras, horasNegativas);
    }
}