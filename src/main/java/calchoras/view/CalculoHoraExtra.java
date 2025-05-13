package calchoras.view;

import calchoras.model.*;
import calchoras.controller.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class CalculoHoraExtra extends javax.swing.JFrame {

    public CalculoHoraExtra() {
        initComponents();
        
        Map<String, Integer> mapaModelos = new HashMap<>();
        
        carregarEmpresasDoJson(cbEmpresa, mapaModelos, "src/main/resources/templates.json");
        
        //Criação de Listeners para cbEmpresa e spnModelo para visualização dos modelos
        cbEmpresa.addActionListener(e -> {
             strEmpresaSelecionada = (String) cbEmpresa.getSelectedItem();
             if (strEmpresaSelecionada != null && mapaModelos.containsKey(strEmpresaSelecionada)) {
                int modelo = mapaModelos.get(strEmpresaSelecionada);
                spnModelo.setValue(modelo); //Atualiza o spinner com o modelo da empresa selecionada
            }
        });
        spnModelo.addChangeListener(e -> {
            int valorSelecionado = (int) spnModelo.getValue();
            for (Map.Entry<String, Integer> entry : mapaModelos.entrySet()) {
                if (entry.getValue() == valorSelecionado) {
                String empresa = entry.getKey();
                cbEmpresa.setSelectedItem(empresa);
                break;
                }
            }
        });     
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cbEmpresa = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        btnCadastroDeModelo = new javax.swing.JButton();
        lblModelo = new javax.swing.JLabel();
        spnModelo = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        spnMes = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        tfAno = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblDiaDoMesAtual = new javax.swing.JLabel();
        lblDiaDaSemanaAtual = new javax.swing.JLabel();
        tfEntrada1 = new javax.swing.JTextField();
        tfSaida1 = new javax.swing.JTextField();
        tfEntrada2 = new javax.swing.JTextField();
        tfSaida2 = new javax.swing.JTextField();
        btnAdiciona = new javax.swing.JButton();
        btnComecaCalculo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calchoras - Cálculo de Horas Extras");
        setSize(new java.awt.Dimension(1000, 1000));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        cbEmpresa.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("Modelo:");

        btnCadastroDeModelo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnCadastroDeModelo.setText("Cadastro de Modelos");
        btnCadastroDeModelo.setToolTipText("Adicionar/editar modelos.");
        btnCadastroDeModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastroDeModeloActionPerformed(evt);
            }
        });

        lblModelo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        spnModelo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        spnModelo.setValue(1);

        jLabel1.setText("Mês:");

        spnMes.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));

        jLabel3.setText("Ano:");

        jPanel1.setVisible(false);

        lblDiaDoMesAtual.setText("-dia mes");

        lblDiaDaSemanaAtual.setText("-dia semana");

        btnAdiciona.setText("Adiciona");
        btnAdiciona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(lblDiaDoMesAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDiaDaSemanaAtual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfEntrada1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfSaida1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfEntrada2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfSaida2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdiciona)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDiaDoMesAtual)
                    .addComponent(lblDiaDaSemanaAtual)
                    .addComponent(tfEntrada1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfSaida1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfEntrada2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfSaida2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdiciona))
                .addContainerGap(68, Short.MAX_VALUE))
        );

        btnComecaCalculo.setText("Começar Cálculo");
        btnComecaCalculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComecaCalculoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCadastroDeModelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnMes, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfAno, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnComecaCalculo))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModelo)
                    .addComponent(btnCadastroDeModelo)
                    .addComponent(spnModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(spnMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnComecaCalculo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(496, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        ImageModel icon = new ImageModel();
        URL iconUrl = icon.getIconUrl();
        setIconImage(new ImageIcon(iconUrl).getImage());
    }//GEN-LAST:event_formWindowOpened

    private void btnCadastroDeModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastroDeModeloActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            new CadastroTemplate().setVisible(true);        
        });
    }//GEN-LAST:event_btnCadastroDeModeloActionPerformed

    private void btnComecaCalculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComecaCalculoActionPerformed
        jPanel1.setVisible(true);
    }//GEN-LAST:event_btnComecaCalculoActionPerformed

    private void btnAdicionaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionaActionPerformed
        
        MesAnoController controller = new MesAnoController();
        
        int mes = (int) spnMes.getValue();
        if(controller.validaAno(tfAno.getText())){
            int ano = Integer.parseInt(tfAno.getText());
        } 
        // tentar usar classe MesAnoModel para validar os campos acima
      
        
    }//GEN-LAST:event_btnAdicionaActionPerformed
    
    public static void carregarEmpresasDoJson(JComboBox<String> comboBox, Map<String, Integer> mapaModelos,  String caminhoArquivo) {
        try {
            //Ler JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode arrayRaiz = objectMapper.readTree(new File(caminhoArquivo));

            //Verifica se é um array
            if (arrayRaiz.isArray()) {
                for (JsonNode node : arrayRaiz) {
                    JsonNode modeloNode = node.get("modelo");
                    JsonNode empresaNode = node.get("empresa");
                    if (modeloNode != null && empresaNode != null) {
                        String empresa = empresaNode.asText();
                        int modelo = modeloNode.asInt();
                        
                        comboBox.addItem(empresa); //Adiciona empresa ao JComboBox
                        
                        mapaModelos.put(empresa, modelo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdiciona;
    private javax.swing.JButton btnCadastroDeModelo;
    private javax.swing.JButton btnComecaCalculo;
    private javax.swing.JComboBox<String> cbEmpresa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDiaDaSemanaAtual;
    private javax.swing.JLabel lblDiaDoMesAtual;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JSpinner spnMes;
    private javax.swing.JSpinner spnModelo;
    private javax.swing.JTextField tfAno;
    private javax.swing.JTextField tfEntrada1;
    private javax.swing.JTextField tfEntrada2;
    private javax.swing.JTextField tfSaida1;
    private javax.swing.JTextField tfSaida2;
    // End of variables declaration//GEN-END:variables
    String strEmpresaSelecionada;
}



