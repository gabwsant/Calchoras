package calchoras;
import calchoras.view.MainFrame;

public class Calchoras {

    public static void main(String[] args) {
        //Seta Nimbus
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Executa o JFrame
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);      
        });
    }
}
