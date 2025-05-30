package view;

import javax.swing.*;
import java.awt.*;
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
    public JButton botaoAdicionar = new JButton("Adicionar");
    public JButton botaoCalcular = new JButton("Calcular");
    public JTextArea areaResultado = new JTextArea(5, 30);

    public JanelaPrincipal() {
        setTitle("Calchoras - Cálculo de Horas Extras");
        setSize(500, 500);
        setPreferredSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelEntrada = new JPanel(new GridLayout(8, 2));

        //Data
        JLabel data = new JLabel(("Data (dd/mm/aaaa):"));
        data.setFont(new Font("Arial", Font.PLAIN, 16));
        painelEntrada.add(data);
        painelEntrada.add(campoData);
        campoData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        //Jornada
        JLabel jornada = new JLabel(("Jornada:"));
        jornada.setFont(new Font("Arial", Font.PLAIN, 16));
        painelEntrada.add(jornada);
        JLabel gap = new JLabel((""));
        painelEntrada.add(gap);
        painelEntrada.add(campoJornadaEntrada);
        painelEntrada.add(campoJornadaSaida);

        //Entrada
        JLabel entrada = new JLabel(("Entrada:"));
        entrada.setFont(new Font("Arial", Font.PLAIN, 16));
        painelEntrada.add(entrada);
        painelEntrada.add(campoEntrada);

        //Saída Almoço
        JLabel saidaAlmoco = new JLabel(("Saida Almoço:"));
        saidaAlmoco.setFont(new Font("Arial", Font.PLAIN, 16));
        painelEntrada.add(saidaAlmoco);
        painelEntrada.add(campoSaidaAlmoco);

        //Volta Almoço
        JLabel voltaAlmoco = new JLabel(("Volta Almoço:"));
        voltaAlmoco.setFont(new Font("Arial", Font.PLAIN, 16));
        painelEntrada.add(voltaAlmoco);
        painelEntrada.add(campoVoltaAlmoco);

        //Saída
        JLabel saida = new JLabel(("Saida:"));
        saida.setFont(new Font("Arial", Font.PLAIN, 16));
        painelEntrada.add(saida);
        painelEntrada.add(campoSaida);

        //Botões
        painelEntrada.add(botaoAdicionar);
        painelEntrada.add(botaoCalcular);

        add(painelEntrada, BorderLayout.NORTH);
        add(new JScrollPane(areaResultado), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
