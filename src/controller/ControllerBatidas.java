package controller;

import util.Writter;
import model.BatidaPonto;
import model.CalculadoraHorasExtras;
import model.JornadaPadrao;
import view.JanelaPrincipal;
import util.*;

import java.time.Duration;
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
    private final DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm");

    public ControllerBatidas(JanelaPrincipal view) {
        this.view = view;

        this.view.addAcaoAdicionar(e -> adicionarBatida());
        this.view.addAcaoCalcular(e -> calcularHorasExtras());
        this.view.addAcaoRetroceder(e -> removerBatida());
        this.view.addAcaoReiniciar(e -> reiniciarCalculos());
        this.view.addAcaoLimpar(e -> limparResultado());
        this.view.addAcaoFolgar(e -> adicionarFolga());
    }

    private void adicionarBatida() {
        try {
            LocalDate data = view.getData();
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
            }else if (saida.isBefore(voltaAlmoco)) {
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

    private void adicionarFolga() {
        LocalDate data = view.getData();
        LocalDateTime folga = LocalDateTime.of(data, LocalTime.of(0, 0));
        BatidaPonto b = new BatidaPonto(folga, folga, folga, folga);
        batidas.add(b);

        view.exibirMensagemResultado("Folga adicionada: " + data.format(formatadorData) + "\n");

        view.setData(data.plusDays(1));
        view.limpaCampos();
        view.focaCampoEntrada();
    }

    private void calcularHorasExtras() {
        long totalHorasExtras = 0;
        long totalHorasNegativas = 0;
        long tempoAlmoco = view.getTempoAlmoco(); // em minutos

        String caminhoArquivo = System.getProperty("user.home") + "\\Documents\\calculos.txt";

        if(!ValidacaoHorario.isJornadaValida(view.getJornadaEntrada(), view.getJornadaSaida())){
            view.exibirErro("Por favor, preencha a jornada.");
            return;
        }

        LocalTime jornadaEntrada = LocalTime.parse(view.getJornadaEntrada());
        LocalTime jornadaSaida = LocalTime.parse(view.getJornadaSaida());
        JornadaPadrao jornada = new JornadaPadrao(jornadaEntrada, jornadaSaida);

        CalculadoraHorasExtras calc = new CalculadoraHorasExtras();
        int contador = 1;

        try {
            for (BatidaPonto b : batidas) {
                LocalDate data = b.getData();

                if (b.isFolga()) {
                    String mensagemFolga = String.format("Batida %02d\nFolga (%s)\n\n", contador++, data);
                    Writter.escreverNoArquivo(caminhoArquivo, mensagemFolga);
                    continue;
                }

                LocalDateTime entradaEsperada = LocalDateTime.of(data, jornada.getEntrada());
                LocalDateTime saidaEsperada = LocalDateTime.of(data, jornada.getSaida());

                if (jornada.isAtravessaMeiaNoite()) {
                    saidaEsperada = saidaEsperada.plusDays(1);
                }

                long minutosTrabalhados = b.getMinutosTrabalhados();
                long jornadaEsperadaMinutos = Duration.between(entradaEsperada, saidaEsperada).toMinutes() - tempoAlmoco;

                calc.calcularHorasExtras(minutosTrabalhados, jornadaEsperadaMinutos);

                long horasExtras = calc.getHorasExtras();
                long horasNegativas = calc.getHorasNegativas();

                totalHorasExtras += horasExtras;
                totalHorasNegativas += horasNegativas;

                String mensagem = String.format(
                        "Batida %02d (%s)\n" +
                                "Entrada      : %s\n" +
                                "Almoço ida   : %s\n" +
                                "Almoço volta : %s\n" +
                                "Saída        : %s\n" +
                                "Trabalhado   : %2dh %02dmin\n" +
                                "Esperado     : %2dh %02dmin\n" +
                                "Extras       : %2dh %02dmin\n" +
                                "Negativas    : %2dh %02dmin\n\n",
                        contador++,
                        data,
                        b.getEntrada().format(formatadorHora),
                        b.getSaidaAlmoco().format(formatadorHora),
                        b.getVoltaAlmoco().format(formatadorHora),
                        b.getSaida().format(formatadorHora),
                        minutosTrabalhados / 60, minutosTrabalhados % 60,
                        jornadaEsperadaMinutos / 60, jornadaEsperadaMinutos % 60,
                        horasExtras / 60, horasExtras % 60,
                        horasNegativas / 60, horasNegativas % 60
                );

                Writter.escreverNoArquivo(caminhoArquivo, mensagem);
            }

            String resumoFinal = String.format(
                    "\nTotal de horas positivas: %dh %02dmin\n" +
                            "Total de horas negativas: %dh %02dmin\n",
                    totalHorasExtras / 60, totalHorasExtras % 60,
                    totalHorasNegativas / 60, totalHorasNegativas % 60
            );

            Writter.escreverNoArquivo(caminhoArquivo, resumoFinal);
            view.exibirMensagemResultado(resumoFinal);

        } catch (Exception e) {
            view.exibirErro("Erro ao calcular horas extras ou gravar arquivo:\n" + e.getMessage());
            e.printStackTrace();
        } finally {
            view.resetaData();
            batidas.clear();
        }
    }

    public void removerBatida() {
        if(!batidas.isEmpty()) {
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