package calchoras.model;

import java.time.DayOfWeek;
import java.time.YearMonth;

public class DiasUteis {
    
    //Retorna os dias úteis no mês
    public static int calcularDiasUteis(int ano, int mes) {
        return (int) YearMonth.of(ano, mes)
        .atDay(1)
        .datesUntil(YearMonth.of(ano, mes).plusMonths(1).atDay(1))
        .filter(data -> !data.getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                        !data.getDayOfWeek().equals(DayOfWeek.SUNDAY))
        .count();
    }
    
    public static int calcularDiasUteis(int ano, int mes, int feriados){
        int diasUteisOriginais = DiasUteis.calcularDiasUteis(ano, mes);
        int diasUteisComFeriados = diasUteisOriginais - feriados; 
        return diasUteisComFeriados;
    }
}
