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
import java.time.LocalDateTime;
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
            LocalDate dataFim = data.plusDays(1);
            LocalDateTime entrada = LocalDateTime.of(data, view.getEntrada());
            LocalDateTime saidaAlmoco = LocalDateTime.of(data,view.getSaidaAlmoco());
            LocalDateTime voltaAlmoco = LocalDateTime.of(data,view.getVoltaAlmoco());
            LocalDateTime saida = LocalDateTime.of(data,view.getSaida());

            //tratando fim da jornada no dia seguinte
            if (saidaAlmoco.isBefore(entrada)) {
                saidaAlmoco = saidaAlmoco.plusDays(1);
                voltaAlmoco = voltaAlmoco.plusDays(1);
                saida = saida.plusDays(1);
            }
            else if (voltaAlmoco.isBefore(saidaAlmoco)) {
                voltaAlmoco = voltaAlmoco.plusDays(1);
                saida = saida.plusDays(1);
            }

            if (saida.isBefore(voltaAlmoco)) {
                saida = saida.plusDays(1);
            }

            if(!ValidacaoHorario.horariosEmOrdem(entrada, saidaAlmoco, voltaAlmoco, saida)){
                view.exibirErro("Os horários não seguem uma ordem cronológica válida! Revise.");
                return;
            }


            BatidaPonto b = new BatidaPonto(entrada, saidaAlmoco, voltaAlmoco, saida);
            batidas.add(b);
            view.exibirMensagemResultado("Batida adicionada: " + data.format(formatadorData) + "\n");

            view.setData(data.plusDays(1));

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
        LocalDate data = view.getData();

        if(ValidacaoHorario.isJornadaValida(jornadaEntrada, jornadaSaida)) {
            JornadaPadrao jornada = new JornadaPadrao(
                data,
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