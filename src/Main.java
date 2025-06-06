import controller.ControllerBatidas;
import view.JanelaPrincipal;

public class Main {
    public static void main(String[] args) {
        JanelaPrincipal view = new JanelaPrincipal();
        new ControllerBatidas(view);
    }
}
