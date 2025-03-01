package calchoras.controller;

import calchoras.model.*;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TemplateController {
     public boolean salvarTemplate(int modelo, String empresa, String[][] horarios) {
        for (String[] linha : horarios) {
            for (String horario : linha) {
                if (!ValidaHorarioModel.isHorarioValido(horario)) {
                    JOptionPane.showMessageDialog(null, "Horário inválido: " + horario, "Erro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        try {
            TemplateModel template = new TemplateModel(modelo, empresa, horarios);
            TemplateModel.salvarTemplate(template);
            JOptionPane.showMessageDialog(null, "Modelo cadastrado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar template", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
     public static TemplateModel buscarTemplate(int modelo){
         return TemplateModel.buscarTemplatePorModelo(modelo);
     }
     
     public static String[][] getHorarios(TemplateModel template){
         if(template != null){
             return template.getHorarios();
         }
         return new String[0][0];
     }
     
     public void validarCampo(JTextField campo) {
        String texto = campo.getText().trim();
        if (!texto.isEmpty()) {
            if (ValidaHorarioModel.isHorarioValido(texto)) {
                campo.setText(ValidaHorarioModel.formatarHorario(texto));
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
