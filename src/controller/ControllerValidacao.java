package controller;

import javax.swing.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ControllerValidacao {
    //regex para HHmm (hora: 00-23, minuto: 00-59)
    private static final String regex = "^([01]\\d|2[0-3])[0-5]\\d$";
    //regex para HH:mm (hora: 00-23, :, minuto: 00-59)
    private static final String regex1 = "^([01]\\d|2[0-3]):[0-5]\\d$";


    public ControllerValidacao() {

    }

    public void validaBatida(JTextField batida) {
        String texto = batida.getText().trim();
        if (!texto.isEmpty()) {
            if (texto.matches(regex) || texto.matches(regex1)) {
                batida.setText(formatarHorario(texto));
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Horário '" + texto + "' é inválido! Use o formato HH:mm.",
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE
                );
                batida.requestFocus();
            }
        }
    }

    public boolean validaJornada(String jornadaEntrada, String jornadaSaida) {
        if (jornadaEntrada == null || jornadaEntrada.trim().isEmpty() ||
                jornadaSaida == null || jornadaSaida.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "É necessário informar os dois horários da jornada!",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        try {
            LocalTime entrada = LocalTime.parse(jornadaEntrada);
            LocalTime saida = LocalTime.parse(jornadaSaida);

            if (!entrada.isBefore(saida)) {
                JOptionPane.showMessageDialog(
                        null,
                        "O horário de entrada deve ser anterior ao horário de saída!",
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Horário inválido! Use o formato HH:mm (ex: 08:00).",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return true;
    }


    public static String formatarHorario(String horario){
        if(horario.matches(regex)){
            return horario.substring(0, 2) + ":" + horario.substring(2, 4);
        }
        return horario;
    }
}
