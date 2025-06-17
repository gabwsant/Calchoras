package model;

public class ResultadoHoras {
    private long horasExtras;
    private long horasNegativas;

    public ResultadoHoras(long horasExtras, long horasNegativas) {
        this.horasExtras = horasExtras;
        this.horasNegativas = horasNegativas;
    }

    public long getHorasExtras() {
        return horasExtras;
    }

    public long getHorasNegativas() {
        return horasNegativas;
    }
}

