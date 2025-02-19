package calchoras.controller;

import calchoras.model.*;
import javax.swing.JOptionPane;

public class TemplateController {
     public boolean salvarTemplate(int modelo, String empresa, String[][] horarios) {
        for (String[] linha : horarios) {
            for (String horario : linha) {
                if (!ValidacaoHorario.isHorarioValido(horario)) {
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
}
