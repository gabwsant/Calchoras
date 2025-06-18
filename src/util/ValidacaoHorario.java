package util;

import javax.swing.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ValidacaoHorario {

    //regex para HHmm (hora: 00-23, minuto: 00-59)
    private static final String regex = "^([01]\\d|2[0-3])[0-5]\\d$";
    //regex para HH:mm (hora: 00-23, :, minuto: 00-59)
    private static final String regex1 = "^([01]\\d|2[0-3]):[0-5]\\d$";

    public static boolean isHorarioValido(String texto) {
        return texto.matches(regex) || texto.matches(regex1);
    }

    public static String formatarHorario(String horario) {
        if (horario.matches(regex)) {
            return horario.substring(0, 2) + ":" + horario.substring(2, 4);
        }
        return horario;
    }

    public static boolean isJornadaValida(String jornadaEntrada, String jornadaSaida) {
        return jornadaEntrada != null && !jornadaEntrada.trim().isEmpty() &&
                jornadaSaida != null && !jornadaSaida.trim().isEmpty();
    }

    //não estou usando esta classe pois há funcionários que começam a jornada em um dia e teminam no outro
    public static boolean horariosEmOrdem(String h1, String h2) {
        try {
            return LocalTime.parse(h1).isBefore(LocalTime.parse(h2));
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
