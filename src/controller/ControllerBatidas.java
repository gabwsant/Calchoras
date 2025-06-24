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
        this.view.addAcaoRetroceder(e -> removerBatida());
        this.view.addAcaoReiniciar(e -> reiniciarCalculos());
        this.view.addAcaoLimpar(e -> limparResultado());
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
            view.exibirMensagemResultado("Batida adicionada: " + data.format(formatadorData) + "\n");

            LocalDate proximaData = data.plusDays(1);
            view.setData(proximaData);

            view.limpaCampos();
            view.focaCampoEntrada();

        } catch (Exception ex) {
            view.exibirErro("Erro ao adicionar batida. Verifique os campos.");
        }
    }

    private void calcularHorasExtras() {
        CalculadoraHorasExtras calc = new CalculadoraHorasExtras();
        String jornadaEntrada = view.getJornadaEntrada();
        String jornadaSaida = view.getJornadaSaida();

        if(ValidacaoHorario.isJornadaValida(jornadaEntrada, jornadaSaida)) {
            JornadaPadrao jornada = new JornadaPadrao(
                    LocalTime.parse(jornadaEntrada),
                    LocalTime.parse(jornadaSaida)
            );
            ResultadoHoras resultado = calc.calcularHorasExtras(batidas, jornada.getJornadaPadrao());
            long extras = resultado.getHorasExtras();
            long negativas = resultado.getHorasNegativas();
            view.exibirMensagemResultado("\nTotal de horas positivas: " + (extras / 60) + "h " + (extras % 60) + "min\n" +
                                      "Total de horas negativas: " + (negativas / 60) + "h " + (negativas % 60) + "min\n");
            view.resetaData();
            batidas.clear();
        }else{
            view.exibirErro("Jornada inválida! Confira o preenchimento.");
        }
    }

    public void removerBatida() {
        if(batidas.size() > 0) {
            LocalDate data = view.getData();

            batidas.remove(batidas.size() - 1);

            view.setData(data.plusDays(-1));
            view.exibirMensagemResultado("Batida em " + data.format(formatadorData) + " removida!\n");
        }
    }

    public void reiniciarCalculos() {
        view.resetaData();
        batidas.clear();
        view.exibirMensagemResultado("\nCálculo reiniciado.\n");
    }

    public void limparResultado() {
        view.limpaMensagemResultado();
    }
}