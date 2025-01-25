package calchoras.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateDeHorario {
    @JsonProperty("modelo")
    private int modelo;

    @JsonProperty("empresa")
    private String empresa;

    @JsonProperty("horarios")
    private String[][] horarios;

    // Construtor
    public TemplateDeHorario(int modelo, String empresa, String[][] horarios) {
        this.modelo = modelo;
        this.empresa = empresa;
        this.horarios = horarios;
    }

    // Getters e Setters
    public int getModelo() {
        return modelo;
    }

    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String[][] getHorarios() {
        return horarios;
    }

    public void setHorarios(String[][] horarios) {
        this.horarios = horarios;
    }
}
