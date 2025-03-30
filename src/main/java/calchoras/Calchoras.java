package calchoras;
import calchoras.view.*;
import javax.swing.UIManager;

public class Calchoras {
    
    public static void main(String[] args) {
        //Seta Nimbus
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastraTemplate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            System.out.print("Não foi possível definir o LAF");
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new CalculaHoraExtra().setVisible(true);        
        });
        
    }
}