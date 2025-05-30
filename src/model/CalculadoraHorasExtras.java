package model;

import java.util.List;

public class CalculadoraHorasExtras {
    //private static final int JORNADA_PADRAO_MINUTOS = 8 * 60;

    public long calcularHorasExtras(List<BatidaPonto> batidas, long jornada) {
        long total = 0;
        for (BatidaPonto b : batidas) {
            long trabalhado = b.getMinutosTrabalhados();
            if (trabalhado > jornada) {
                total += trabalhado - jornada;
            }
        }
        return total;
    }
}
