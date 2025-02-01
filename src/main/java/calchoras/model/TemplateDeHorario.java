package calchoras.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemplateDeHorario {
    @JsonProperty("modelo")
    private int modelo;

    @JsonProperty("empresa")
    private String empresa;

    @JsonProperty("horarios")
    private String[][] horarios;
    
    private static final String FILE_PATH = "src/main/resources/templates.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    //construtores
    public TemplateDeHorario(){}
    
    public TemplateDeHorario(int modelo, String empresa, String[][] horarios) {
        this.modelo = modelo;
        this.empresa = empresa;
        this.horarios = horarios;
    }

    //métodos
    public static void salvarTemplate(TemplateDeHorario template) throws IOException {
        List<TemplateDeHorario> templates = carregarTemplates();  //carregar os templates existentes
        
        boolean existe = templates.stream().anyMatch(t -> t.getModelo() == template.getModelo());
        
        if(existe){
            throw new IllegalArgumentException("Erro: Já existe um modelo com o número " + template.getModelo());
        }
        
        templates.add(template);                                  //adicionar o novo template
        mapper.writeValue(new File(FILE_PATH), templates);        //salvar no arquivo
    }
    
    public static List<TemplateDeHorario> carregarTemplates() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();                         //se o arquivo não existe, retorna uma lista vazia
            }
            return mapper.readValue(file, new TypeReference<List<TemplateDeHorario>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();                             //se der erro, retorna uma lista vazia
        }
    }
    
    public static TemplateDeHorario buscarTemplatePorNome(String empresa){
        return carregarTemplates().stream()
                .filter(t -> t.getEmpresa().equalsIgnoreCase(empresa))
                .findFirst()
                .orElse(null);
    }
    
    public static TemplateDeHorario buscarTemplatePorModelo(int modelo){
        return carregarTemplates().stream()
                .filter(t -> t.getModelo() == modelo)
                .findFirst()
                .orElse(null);
    }
    
    //getters e setters
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
