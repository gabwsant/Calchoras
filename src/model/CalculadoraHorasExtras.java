package model;

public class CalculadoraHorasExtras {

    long horasExtras;
    long horasNegativas;

    public void calcularHorasExtras(long trabalhado, long jornadaEsperada) {
        horasExtras = 0;
        horasNegativas = 0;

        if (trabalhado > jornadaEsperada) {
            horasExtras += trabalhado - jornadaEsperada;
        }else if(trabalhado < jornadaEsperada){
            horasNegativas += jornadaEsperada - trabalhado;
        }
    }

    public long getHorasExtras() {
        return horasExtras;
    }

    public long getHorasNegativas() {
        return horasNegativas;
    }
}
