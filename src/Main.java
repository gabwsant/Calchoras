import controller.ControleBatidas;
import view.JanelaPrincipal;

public class Main {
    public static void main(String[] args) {
        JanelaPrincipal view = new JanelaPrincipal();
        new ControleBatidas(view);
    }
}
