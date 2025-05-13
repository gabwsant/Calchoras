package calchoras.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class MesAnoModel {
    private int mes; // 1 a 12
    private int ano;

    public MesAnoModel(int mes, int ano) {
        this.mes = mes;
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public int getNumeroDeDias() {
        YearMonth ym = YearMonth.of(ano, mes);
        return ym.lengthOfMonth();
    }

    public DayOfWeek getDiaDaSemana(int dia) {
        LocalDate data = LocalDate.of(ano, mes, dia);
        return data.getDayOfWeek(); // Ex: terça-feira
    }

    public List<String> getDiasDoMesComSemana() {
        List<String> dias = new ArrayList<>();
        int totalDias = getNumeroDeDias();
        for (int i = 1; i <= totalDias; i++) {
            LocalDate data = LocalDate.of(ano, mes, i);
            dias.add(String.format("%02d/%02d/%d - %s", i, mes, ano, data.getDayOfWeek()));
        }
        return dias;
    }
}
