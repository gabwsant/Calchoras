package calchoras.controller;

import calchoras.model.ValidacaoHorarioModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ValidaHorarioController {
    
    public void validarCampo(JTextField campo) {
        String texto = campo.getText().trim();
        if (!texto.isEmpty()) {
            if (ValidacaoHorarioModel.isHorarioValido(texto)) {
                campo.setText(ValidacaoHorarioModel.formatarHorario(texto));
            } else {
                JOptionPane.showMessageDialog(
                    null, 
                    "Horário '" + texto + "' é inválido! Use o formato HH:mm.", 
                    "Erro de Validação", 
                    JOptionPane.ERROR_MESSAGE
                );
                campo.requestFocus();
            }
        }
    }
}
