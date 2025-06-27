package controller;

import model.ArquivoCalculo;
import model.BatidaPonto;
import model.CalculadoraHorasExtras;
import model.JornadaPadrao;
import view.JanelaPrincipal;
import util.*;

import java.io.File;
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

            System.out.println(entrada);
            System.out.println(saidaAlmoco);
            System.out.println(voltaAlmoco);
            System.out.println(saida);

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

    private void calcularHorasExtras(){
        long horasExtras = 0;
        long horasNegativas = 0;
        String documentos = System.getProperty("user.home") + "\\Documents";
        String caminhoArquivo = documentos + "\\calculos.txt";

        LocalTime jornadaEntrada = LocalTime.parse(view.getJornadaEntrada());
        LocalTime jornadaSaida = LocalTime.parse(view.getJornadaSaida());

        JornadaPadrao jornada = new JornadaPadrao(jornadaEntrada, jornadaSaida);
        CalculadoraHorasExtras calc = new CalculadoraHorasExtras();

        int i = 1;
        for(BatidaPonto b : batidas) {
            LocalDate data = b.getData();

            LocalDateTime entradaEsperada = LocalDateTime.of(data, jornada.getEntrada());
            LocalDateTime saidaEsperada = LocalDateTime.of(data, jornada.getSaida());

            if (jornada.isAtravessaMeiaNoite()) {
                saidaEsperada = saidaEsperada.plusDays(1);
            }

            long trabalhado = b.getMinutosTrabalhados();
            long jornadaEsperada = Duration.between(entradaEsperada, saidaEsperada).toMinutes() - 60;

            calc.calcularHorasExtras(trabalhado, jornadaEsperada);
            horasExtras += calc.getHorasExtras();
            horasNegativas += calc.getHorasNegativas();

            String mensagem = String.format(
                    "Batida %02d (%s)\n" +
                            "Entrada : %s\n" +
                            "Almoço  : %s\n" +
                            "          %s\n" +
                            "Saída   : %s\n" +
                            "Extras     -> %2dh %02dmin\n" +
                            "Negativas  -> %2dh %02dmin\n" +
                            "Esperada   -> %2dh %02dmin\n",
                    i++,
                    data.toString(),                                    // data da batida
                    b.getEntrada().format(formatadorHora),              // entrada
                    b.getSaidaAlmoco().format(formatadorHora),          // almoço
                    b.getVoltaAlmoco().format(formatadorHora),          // volta
                    b.getSaida().format(formatadorHora),                // saída
                    horasExtras / 60, horasExtras % 60,
                    horasNegativas / 60, horasNegativas % 60,
                    jornadaEsperada / 60, jornadaEsperada % 60
            );
            ArquivoCalculo.escreverNoArquivo(caminhoArquivo, mensagem);
        }
        view.exibirMensagemResultado("\nTotal de horas positivas: " + (horasExtras / 60) + "h " + (horasExtras % 60) + "min\n" +
                    "Total de horas negativas: " + (horasNegativas / 60) + "h " + (horasNegativas % 60) + "min\n");
        view.resetaData();
        batidas.clear();
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