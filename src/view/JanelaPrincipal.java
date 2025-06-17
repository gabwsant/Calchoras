package view;

import controller.ControllerValidacao;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JanelaPrincipal extends JFrame {
    public JTextField campoData = new JTextField(10);
    public JTextField campoJornadaEntrada = new JTextField(10);
    public JTextField campoJornadaSaida = new JTextField(10);
    public JTextField campoEntrada = new JTextField(5);
    public JTextField campoSaidaAlmoco = new JTextField(5);
    public JTextField campoVoltaAlmoco = new JTextField(5);
    public JTextField campoSaida = new JTextField(5);
    public JButton botaoAdicionar = new JButton("➕ Adicionar");
    public JButton botaoCalcular = new JButton("🧮 Calcular");
    public JTextArea areaResultado = new JTextArea(6, 30);

    private ControllerValidacao controllerValidacao = new ControllerValidacao();

    public JanelaPrincipal() {
        setTitle("Calchoras - Cálculo de Horas Extras");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 550);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setIcon("/resources/calc_icon.png");

        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 16));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 16));
        UIManager.put("TextArea.font", new Font("Monospaced", Font.PLAIN, 14));

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

        // Data
        painelPrincipal.add(criaLinha("Data (dd/mm/aaaa):", campoData));
        campoData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("01/MM/yyyy")));

        // Jornada
        JPanel jornadaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jornadaPanel.setBorder(new TitledBorder("Jornada"));
        jornadaPanel.add(campoJornadaEntrada);
        jornadaPanel.add(new JLabel("às"));
        jornadaPanel.add(campoJornadaSaida);
        adicionaValidacaoBatida(campoJornadaEntrada);
        adicionaValidacaoBatida(campoJornadaSaida);
        painelPrincipal.add(jornadaPanel);

        // Batidas
        painelPrincipal.add(criaLinha("Entrada:", campoEntrada));
        painelPrincipal.add(criaLinha("Saída Almoço:", campoSaidaAlmoco));
        painelPrincipal.add(criaLinha("Volta Almoço:", campoVoltaAlmoco));
        painelPrincipal.add(criaLinha("Saída:", campoSaida));
        adicionaValidacaoBatida(campoEntrada);
        adicionaValidacaoBatida(campoSaidaAlmoco);
        adicionaValidacaoBatida(campoVoltaAlmoco);
        adicionaValidacaoBatida(campoSaida);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoAdicionar.setBackground(new Color(200, 230, 200));
        botaoCalcular.setBackground(new Color(200, 200, 250));
        botoes.add(botaoAdicionar);
        botoes.add(botaoCalcular);
        painelPrincipal.add(botoes);

        // Resultado
        areaResultado.setBorder(BorderFactory.createTitledBorder("Resultado"));
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        painelPrincipal.add(new JScrollPane(areaResultado));

        add(painelPrincipal, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel criaLinha(String rotulo, JTextField campo) {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(rotulo);
        label.setPreferredSize(new Dimension(150, 30));
        painel.add(label);
        painel.add(campo);
        return painel;
    }

    public void limpaCampos() {
        campoEntrada.setText("");
        campoSaidaAlmoco.setText("");
        campoVoltaAlmoco.setText("");
        campoSaida.setText("");
    }

    public void resetaData() {
        campoData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("01/MM/yyyy")));
    }

    public void setIcon(String url) {
        URL iconURL = JanelaPrincipal.class.getResource(url);
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        } else {
            System.err.println("Ícone não encontrado!");
        }
    }

    private void adicionaValidacaoBatida(JTextField campo) {
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                controllerValidacao.validaBatida(campo);
            }
        });
    }
}
