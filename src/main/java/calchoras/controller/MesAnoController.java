package calchoras.controller;

import javax.swing.JOptionPane;


/**
 *
 * @author gabri
 */
public class MesAnoController {

    int ano;
    
    public boolean validaAno(String ano) {
        if (!ano.isEmpty()){
            try{
                this.ano = Integer.parseInt(ano);
                if(this.ano < 1500 || this.ano > 2999){
                    JOptionPane.showMessageDialog(null, "Data inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                return true;
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Data aceita apenas números. Corrigir.", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }else{
            JOptionPane.showMessageDialog(null, "A data deve ser informada.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
            
    }
    
}
