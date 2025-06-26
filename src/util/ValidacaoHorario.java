package util;

import java.time.LocalDateTime;

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

    public static boolean horariosEmOrdem(LocalDateTime h1,
                                          LocalDateTime h2,
                                          LocalDateTime h3,
                                          LocalDateTime h4) {
        return h1.isBefore(h2) && h2.isBefore(h3) && h3.isBefore(h4);

    }
}
