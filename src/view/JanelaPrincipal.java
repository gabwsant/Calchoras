package view;

import util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JanelaPrincipal extends JFrame {
    public JFormattedTextField campoData;
    public JTextField campoJornadaEntrada = new JTextField(5);
    public JTextField campoJornadaSaida = new JTextField(5);
    public JTextField campoTempoAlmoco = new JTextField(2);
    public JTextField campoEntrada = new JTextField(5);
    public JTextField campoSaidaAlmoco = new JTextField(5);
    public JTextField campoVoltaAlmoco = new JTextField(5);
    public JTextField campoSaida = new JTextField(5);
    public JButton botaoAdicionar = new JButton("➕ Adicionar");
    public JButton botaoCalcular = new JButton("\uD83D\uDD0E Calcular");
    public JButton botaoRetroceder = new JButton("⬅ Retroceder");
    public JButton botaoReiniciar = new JButton("\uD83D\uDD01 Reiniciar");
    public JButton botaoLimparResultado = new JButton("✖ Limpar Área Resultado");
    public JTextArea areaResultado = new JTextArea(6, 30);

    private final DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            System.err.println("Erro criar formatador: " + e.getMessage());
        }
        campoData = new JFormattedTextField(mask);
        painelPrincipal.add(criaLinha("Data:", campoData));
        adicionaValidacaoData(campoData);
        campoData.setText(
                LocalDate.now()
                         .minusMonths(1)
                         .withDayOfMonth(1)
                         .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        //Jornada
        JPanel jornadaPanel = new JPanel(new GridBagLayout());
        jornadaPanel.setBorder(new TitledBorder("Jornada"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento interno
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Campo jornadaEntrada
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        jornadaPanel.add(campoJornadaEntrada, gbc);
        // Label "às"
        gbc.gridx = 1;
        gbc.weightx = 0;
        jornadaPanel.add(new JLabel("às"), gbc);
        // Campo jornadaSaida
        gbc.gridx = 2;
        gbc.weightx = 0.3;
        jornadaPanel.add(campoJornadaSaida, gbc);
        // Label Tempo de Almoço
        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        jornadaPanel.add(new JLabel("Tempo de Almoço:"), gbc);
        // Campo Tempo de Almoço
        gbc.gridx = 4;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        jornadaPanel.add(campoTempoAlmoco, gbc);
        // Label minutos
        gbc.gridx = 5;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        jornadaPanel.add(new JLabel("minutos"), gbc);
        painelPrincipal.add(jornadaPanel);

        // Batidas
        painelPrincipal.add(criaLinha("Entrada:", campoEntrada));
        painelPrincipal.add(criaLinha("Saída Almoço:", campoSaidaAlmoco));
        painelPrincipal.add(criaLinha("Volta Almoço:", campoVoltaAlmoco));
        painelPrincipal.add(criaLinha("Saída:", campoSaida));
        adicionaValidacaoBatida(campoEntrada);
        adicionaAvancoAutomatico(campoEntrada);
        adicionaValidacaoBatida(campoSaidaAlmoco);
        adicionaAvancoAutomatico(campoSaidaAlmoco);
        adicionaValidacaoBatida(campoVoltaAlmoco);
        adicionaAvancoAutomatico(campoVoltaAlmoco);
        adicionaValidacaoBatida(campoSaida);
        adicionaAvancoAutomatico(campoSaida);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoAdicionar.setBackground(new Color(46, 204, 113));
        botaoCalcular.setBackground(new Color(52, 152, 219));
        botaoRetroceder.setBackground(new Color(231, 76, 60));
        botaoReiniciar.setBackground(new Color(241, 196, 15));
        botoes.add(botaoAdicionar);
        botoes.add(botaoCalcular);
        botoes.add(botaoRetroceder);
        botoes.add(botaoReiniciar);
        painelPrincipal.add(botoes);

        // Resultado
        areaResultado.setBorder(BorderFactory.createTitledBorder("Resultado"));
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        painelPrincipal.add(new JScrollPane(areaResultado));

        // Botão Limpar
        JPanel painelLimpar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoLimparResultado.setBackground(new Color(200, 200, 200));
        painelLimpar.add(botaoLimparResultado);
        painelPrincipal.add(painelLimpar);

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
        campoData.setText(
                LocalDate.now()
                         .minusMonths(1)
                         .withDayOfMonth(1)
                         .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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

    public void addAcaoAdicionar(ActionListener listener) {
        botaoAdicionar.addActionListener(listener);
    }

    public void addAcaoCalcular(ActionListener listener) {
        botaoCalcular.addActionListener(listener);
    }

    public void addAcaoRetroceder(ActionListener listener) {
        botaoRetroceder.addActionListener(listener);
    }

    public void addAcaoReiniciar(ActionListener listener) {
        botaoReiniciar.addActionListener(listener);
    }

    public void addAcaoLimpar(ActionListener listener) {
        botaoLimparResultado.addActionListener(listener);
    }

    private void adicionaValidacaoBatida(JTextField campo) {
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String batida = campo.getText().trim();
                if (!batida.isEmpty()){
                    if (!ValidacaoHorario.isHorarioValido(batida)) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Horário inválido! Use o formato HH24:MI (ex: 17:45).",
                                "Erro de Validação",
                                JOptionPane.ERROR_MESSAGE);
                        campo.requestFocus();
                    } else {
                        campo.setText(ValidacaoHorario.formatarHorario(batida));
                    }
                }
            }
        });
    }

    private void adicionaValidacaoData(JTextField campo) {
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String texto = campo.getText();
                try {
                    LocalDate.parse(texto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Data inválida! Use o formato dd/mm/aaaa (ex: 03/07/1985).",
                            "Erro de Validação",
                            JOptionPane.ERROR_MESSAGE
                    );
                    campo.requestFocus();
                }
            }
        });
    }

    private void adicionaAvancoAutomatico(JTextField campo){
        campo.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override public void insertUpdate(DocumentEvent e){ avancar(); }
            @Override public void removeUpdate(DocumentEvent e) { avancar(); }

            private void avancar() {
                String texto = campo.getText();
                if ((texto.length() == 4 && !texto.contains(":")) ||
                        (texto.length() == 5 && texto.contains(":"))) {
                    SwingUtilities.invokeLater(() -> { //aguarda o sistema concluir o evento shift+tab
                        if (campo.hasFocus()) { //só avança se o campo ainda tem foco (evita conflito com shift+tab)
                            campo.transferFocus();
                        }
                    });
                }
            }
        });
    }

    public void focaCampoEntrada() {
        campoEntrada.requestFocus();
    }

    public void exibirMensagemResultado(String mensagem){
        areaResultado.append(mensagem);
    }

    public void limpaMensagemResultado(){
        areaResultado.setText("");
    }

    public void exibirErro(String mensagem){
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public LocalDate getData() {
        String texto = campoData.getText();
        return LocalDate.parse(texto, formatadorData);
    }

    //O parse da jornada é feito no ControllerBatidas pois possuí validação específica
    public String getJornadaEntrada() {
        return campoJornadaEntrada.getText();
    }
    public String getJornadaSaida() {
        return campoJornadaSaida.getText();
    }

    public LocalTime getEntrada(){
        String texto = campoEntrada.getText();
        return LocalTime.parse(texto);
    }

    public LocalTime getSaidaAlmoco(){
        String texto = campoSaidaAlmoco.getText();
        return LocalTime.parse(texto);
    }

    public LocalTime getVoltaAlmoco(){
        String texto = campoVoltaAlmoco.getText();
        return LocalTime.parse(texto);
    }

    public LocalTime getSaida(){
        String texto = campoSaida.getText();
        return LocalTime.parse(texto);
    }

    public void setData(LocalDate data) {
        campoData.setText(data.format(formatadorData));
    }
}
