package controller;

import model.BatidaPonto;
import model.CalculadoraHorasExtras;
import model.JornadaPadrao;
import model.ResultadoHoras;
import view.JanelaPrincipal;
import util.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ControllerBatidas {
    private JanelaPrincipal view;
    private List<BatidaPonto> batidas = new ArrayList<>();
    private final DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ControllerBatidas(JanelaPrincipal view) {
        this.view = view;

        this.view.addAcaoAdicionar(e -> adicionarBatida());
        this.view.addAcaoCalcular(e -> calcularHorasExtras());
    }

    private void adicionarBatida() {
        try {
            LocalDate data = view.getData();
            LocalTime entrada = view.getEntrada();
            LocalTime saidaAlmoco = view.getSaidaAlmoco();
            LocalTime voltaAlmoco = view.getVoltaAlmoco();
            LocalTime saida = view.getSaida();

            BatidaPonto b = new BatidaPonto(data, entrada, saidaAlmoco, voltaAlmoco, saida);
            batidas.add(b);
            view.areaResultado.append("Batida adicionada: " + data.format(formatadorData) + "\n");

            LocalDate proximaData = data.plusDays(1);
            view.campoData.setText(proximaData.format(formatadorData));
            view.limpaCampos();
            view.campoEntrada.requestFocus();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao adicionar batida. Verifique os campos.");
        }
    }

    private void calcularHorasExtras() {
        CalculadoraHorasExtras calc = new CalculadoraHorasExtras();
        String jornadaEntrada = view.campoJornadaEntrada.getText();
        String jornadaSaida = view.campoJornadaSaida.getText();

        if(ValidacaoHorario.isJornadaValida(jornadaEntrada, jornadaSaida)) {
            JornadaPadrao jornada = new JornadaPadrao(
                    LocalTime.parse(jornadaEntrada),
                    LocalTime.parse(jornadaSaida)
            );
            ResultadoHoras resultado = calc.calcularHorasExtras(batidas, jornada.getJornadaPadrao());
            long extras = resultado.getHorasExtras();
            long negativas = resultado.getHorasNegativas();
            view.areaResultado.append("\nTotal de horas extras: " + (extras / 60) + "h " + (extras % 60) + "min\n" +
                                      "Total de horas negativas:   " + (negativas / 60) + "h " + (negativas % 60) + "min\n");
            view.resetaData();
            batidas.clear();
        }else{
            JOptionPane.showMessageDialog(view, "Jornada inválida! Confira o preenchimento.");;
        }
    }
}