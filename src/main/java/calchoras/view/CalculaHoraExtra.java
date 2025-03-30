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

public class CalculaHoraExtra extends javax.swing.JFrame {

    public CalculaHoraExtra() {
        initComponents();
        
        Map<String, Integer> mapaModelos = new HashMap<>();
        
        carregarEmpresasDoJson(cbEmpresa, mapaModelos, "src/main/resources/templates.json");
        
        cbEmpresa.addActionListener(e -> {
             empresaSelecionada = (String) cbEmpresa.getSelectedItem();
             if (empresaSelecionada != null && mapaModelos.containsKey(empresaSelecionada)) {
                int modelo = mapaModelos.get(empresaSelecionada);
                spnModelo.setValue(modelo); //Atualiza o spinner com o modelo da empresa selecionada
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(spnModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCadastroDeModelo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
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
                .addContainerGap(659, Short.MAX_VALUE))
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
            new CadastraTemplate().setVisible(true);        
        });
    }//GEN-LAST:event_btnCadastroDeModeloActionPerformed
    
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
    private javax.swing.JButton btnCadastroDeModelo;
    private javax.swing.JComboBox<String> cbEmpresa;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JSpinner spnModelo;
    // End of variables declaration//GEN-END:variables
    String empresaSelecionada;
}



