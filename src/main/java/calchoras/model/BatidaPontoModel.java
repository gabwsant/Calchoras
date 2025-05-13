package calchoras.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class BatidaPontoModel {
    private LocalDate data;
    private LocalTime entrada;
    private LocalTime saidaAlmoco;
    private LocalTime retornoAlmoco;
    private LocalTime saida;
    
    public BatidaPontoModel(LocalDate data, LocalTime entrada, LocalTime saidaAlmoco, LocalTime retornoAlmoco, LocalTime saida) {
        this.data = data;
        this.entrada = entrada;
        this.saidaAlmoco = saidaAlmoco;
        this.retornoAlmoco = retornoAlmoco;
        this.saida = saida;
    }

    // Getters
    public LocalDate getData() { return data; }
    public LocalTime getEntrada() { return entrada; }
    public LocalTime getSaidaAlmoco() { return saidaAlmoco; }
    public LocalTime getRetornoAlmoco() { return retornoAlmoco; }
    public LocalTime getSaida() { return saida; }
}