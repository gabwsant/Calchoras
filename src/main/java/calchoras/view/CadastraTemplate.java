package calchoras.view;

import calchoras.model.*;
import calchoras.controller.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CadastraTemplate extends javax.swing.JFrame {
    
    private static final Logger logger = Logger.getLogger(CadastraTemplate.class.getName());
    
    public CadastraTemplate() {
        controller = new ValidaHorarioController();
        
        initComponents();
        adicionaValidador(Grid, controller);
        initLogger();
    }

    private static void initLogger() {
        try {
            FileHandler fh = new FileHandler("logs.txt", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao configurar o Logger", e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        spnModelo = new javax.swing.JSpinner();
        Grid = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        tfEntrada1 = new javax.swing.JTextField();
        tfAlmoco1 = new javax.swing.JTextField();
        tfVolta1 = new javax.swing.JTextField();
        tfSaida1 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        tfEntrada2 = new javax.swing.JTextField();
        tfAlmoco2 = new javax.swing.JTextField();
        tfVolta2 = new javax.swing.JTextField();
        tfSaida2 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        tfEntrada3 = new javax.swing.JTextField();
        tfAlmoco3 = new javax.swing.JTextField();
        tfVolta3 = new javax.swing.JTextField();
        tfSaida3 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        tfEntrada4 = new javax.swing.JTextField();
        tfAlmoco4 = new javax.swing.JTextField();
        tfVolta4 = new javax.swing.JTextField();
        tfSaida4 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        tfEntrada5 = new javax.swing.JTextField();
        tfAlmoco5 = new javax.swing.JTextField();
        tfVolta5 = new javax.swing.JTextField();
        tfSaida5 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        tfEntrada6 = new javax.swing.JTextField();
        tfAlmoco6 = new javax.swing.JTextField();
        tfVolta6 = new javax.swing.JTextField();
        tfSaida6 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfEntrada7 = new javax.swing.JTextField();
        tfAlmoco7 = new javax.swing.JTextField();
        tfVolta7 = new javax.swing.JTextField();
        tfSaida7 = new javax.swing.JTextField();
        tfModelo = new javax.swing.JTextField();
        btnCadastraModelo = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblTitulo.setText("Cálculo de Horas Extras");

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Modelo");

        spnModelo.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        spnModelo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnModeloStateChanged(evt);
            }
        });

        Grid.setLayout(new java.awt.GridLayout(8, 5, 15, 14));
        Grid.add(jLabel19);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Entrada");
        Grid.add(jLabel20);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Almoço");
        Grid.add(jLabel21);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Volta almoço");
        Grid.add(jLabel22);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Saída");
        Grid.add(jLabel23);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Domingo");
        jLabel24.setMaximumSize(null);
        jLabel24.setMinimumSize(null);
        Grid.add(jLabel24);

        tfEntrada1.setMaximumSize(null);
        tfEntrada1.setMinimumSize(null);
        Grid.add(tfEntrada1);

        tfAlmoco1.setMaximumSize(null);
        tfAlmoco1.setMinimumSize(null);
        Grid.add(tfAlmoco1);

        tfVolta1.setMaximumSize(null);
        tfVolta1.setMinimumSize(null);
        Grid.add(tfVolta1);

        tfSaida1.setMaximumSize(null);
        tfSaida1.setMinimumSize(null);
        Grid.add(tfSaida1);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Segunda");
        jLabel25.setMaximumSize(null);
        jLabel25.setMinimumSize(null);
        Grid.add(jLabel25);

        tfEntrada2.setMaximumSize(null);
        tfEntrada2.setMinimumSize(null);
        Grid.add(tfEntrada2);

        tfAlmoco2.setMaximumSize(null);
        tfAlmoco2.setMinimumSize(null);
        Grid.add(tfAlmoco2);

        tfVolta2.setMaximumSize(null);
        tfVolta2.setMinimumSize(null);
        Grid.add(tfVolta2);

        tfSaida2.setMaximumSize(null);
        tfSaida2.setMinimumSize(null);
        Grid.add(tfSaida2);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Terça");
        Grid.add(jLabel26);

        tfEntrada3.setMaximumSize(null);
        tfEntrada3.setMinimumSize(null);
        Grid.add(tfEntrada3);

        tfAlmoco3.setMaximumSize(null);
        tfAlmoco3.setMinimumSize(null);
        Grid.add(tfAlmoco3);

        tfVolta3.setMaximumSize(null);
        tfVolta3.setMinimumSize(null);
        Grid.add(tfVolta3);

        tfSaida3.setMaximumSize(null);
        tfSaida3.setMinimumSize(null);
        Grid.add(tfSaida3);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Quarta");
        jLabel27.setMaximumSize(null);
        jLabel27.setMinimumSize(null);
        Grid.add(jLabel27);

        tfEntrada4.setMaximumSize(null);
        tfEntrada4.setMinimumSize(null);
        Grid.add(tfEntrada4);

        tfAlmoco4.setMaximumSize(null);
        tfAlmoco4.setMinimumSize(null);
        Grid.add(tfAlmoco4);

        tfVolta4.setMaximumSize(null);
        tfVolta4.setMinimumSize(null);
        Grid.add(tfVolta4);

        tfSaida4.setMaximumSize(null);
        tfSaida4.setMinimumSize(null);
        Grid.add(tfSaida4);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Quinta");
        Grid.add(jLabel28);

        tfEntrada5.setMaximumSize(null);
        tfEntrada5.setMinimumSize(null);
        Grid.add(tfEntrada5);

        tfAlmoco5.setMaximumSize(null);
        tfAlmoco5.setMinimumSize(null);
        Grid.add(tfAlmoco5);

        tfVolta5.setMaximumSize(null);
        tfVolta5.setMinimumSize(null);
        Grid.add(tfVolta5);

        tfSaida5.setMaximumSize(null);
        tfSaida5.setMinimumSize(null);
        Grid.add(tfSaida5);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Sexta");
        Grid.add(jLabel29);

        tfEntrada6.setMaximumSize(null);
        tfEntrada6.setMinimumSize(null);
        Grid.add(tfEntrada6);

        tfAlmoco6.setMaximumSize(null);
        tfAlmoco6.setMinimumSize(null);
        Grid.add(tfAlmoco6);

        tfVolta6.setMaximumSize(null);
        tfVolta6.setMinimumSize(null);
        Grid.add(tfVolta6);

        tfSaida6.setMaximumSize(null);
        tfSaida6.setMinimumSize(null);
        Grid.add(tfSaida6);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Sábado");
        Grid.add(jLabel2);

        tfEntrada7.setMaximumSize(null);
        tfEntrada7.setMinimumSize(null);
        Grid.add(tfEntrada7);

        tfAlmoco7.setMaximumSize(null);
        tfAlmoco7.setMinimumSize(null);
        Grid.add(tfAlmoco7);

        tfVolta7.setMaximumSize(null);
        tfVolta7.setMinimumSize(null);
        Grid.add(tfVolta7);

        tfSaida7.setMaximumSize(null);
        tfSaida7.setMinimumSize(null);
        Grid.add(tfSaida7);

        btnCadastraModelo.setText("Cadastra");
        btnCadastraModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastraModeloActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTitulo)
                    .addComponent(Grid, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(spnModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCadastraModelo))
                            .addComponent(jButton1))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(spnModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCadastraModelo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Grid, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   private static void adicionaValidador(Container container, ValidaHorarioController controller){
        for(Component comp : container.getComponents()) {
            if(comp instanceof JTextField) {
                JTextField campo = (JTextField) comp;
                campo.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        controller.validarCampo(campo);
                    }
                });
            }
        }
    }

    private void btnCadastraModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastraModeloActionPerformed

        int modelo     = (int) spnModelo.getValue();
        String empresa = tfModelo.getText();
        String[][] horarios = getHorariosDigitados();
        
        TemplateController controller = new TemplateController();
        controller.salvarTemplate(modelo, empresa, horarios);
        
    }//GEN-LAST:event_btnCadastraModeloActionPerformed
    
    private String[][] getHorariosDigitados(){

        List<String[]> horariosList = new ArrayList<>();
        List<String> linhaAtual = new ArrayList<>();
    
        for(Component c : Grid.getComponents()){
            if(c instanceof JTextField){
                JTextField campo = (JTextField) c;
                linhaAtual.add(campo.getText().trim());

                if(linhaAtual.size() == 4){
                    horariosList.add(linhaAtual.toArray(new String[0]));
                    linhaAtual.clear();
                }
            }   
        }
        return horariosList.toArray(new String[0][0]);
}
    
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //Define ícone da janela
        URL imageUrl = CadastraTemplate.class.getClassLoader().getResource("calc_icon.png");
        setIconImage(new ImageIcon(imageUrl).getImage());
    }//GEN-LAST:event_formWindowOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        TemplateModel template = TemplateModel.buscarTemplatePorModelo(1);
        System.out.print(template.getEmpresa());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void spnModeloStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnModeloStateChanged
        
        try{
            int modeloSelecionado = (int) spnModelo.getValue();
            TemplateModel templateAtual = TemplateController.buscarTemplate(modeloSelecionado);
        
            if (templateAtual == null) {
                System.out.println("Não existe modelo salvo com o número " + modeloSelecionado);
                return;
            }
        
            tfModelo.setText(templateAtual.getEmpresa());
            String[][] horarios = TemplateController.getHorarios(templateAtual);
        
            Component[] componentes = Grid.getComponents();
            int index = 0;
        
            for (Component c : componentes) {
                if (c instanceof JTextField) {
                    int linha = index / 4;  // Cada linha tem 4 campos (entrada, almoço, volta, saída)
                    int coluna = index % 4; // Define qual tipo de horário está sendo preenchido
                
                    if (linha < horarios.length && coluna < horarios[linha].length) {
                        ((JTextField) c).setText(horarios[linha][coluna]);
                    }
                    index++; 
                }
            }
        
        } catch (Exception e) {
            System.out.println("Erro ao buscar modelo: " + e.getMessage());
        }
    }//GEN-LAST:event_spnModeloStateChanged
           
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Grid;
    private javax.swing.JButton btnCadastraModelo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JSpinner spnModelo;
    private javax.swing.JTextField tfAlmoco1;
    private javax.swing.JTextField tfAlmoco2;
    private javax.swing.JTextField tfAlmoco3;
    private javax.swing.JTextField tfAlmoco4;
    private javax.swing.JTextField tfAlmoco5;
    private javax.swing.JTextField tfAlmoco6;
    private javax.swing.JTextField tfAlmoco7;
    private javax.swing.JTextField tfEntrada1;
    private javax.swing.JTextField tfEntrada2;
    private javax.swing.JTextField tfEntrada3;
    private javax.swing.JTextField tfEntrada4;
    private javax.swing.JTextField tfEntrada5;
    private javax.swing.JTextField tfEntrada6;
    private javax.swing.JTextField tfEntrada7;
    private javax.swing.JTextField tfModelo;
    private javax.swing.JTextField tfSaida1;
    private javax.swing.JTextField tfSaida2;
    private javax.swing.JTextField tfSaida3;
    private javax.swing.JTextField tfSaida4;
    private javax.swing.JTextField tfSaida5;
    private javax.swing.JTextField tfSaida6;
    private javax.swing.JTextField tfSaida7;
    private javax.swing.JTextField tfVolta1;
    private javax.swing.JTextField tfVolta2;
    private javax.swing.JTextField tfVolta3;
    private javax.swing.JTextField tfVolta4;
    private javax.swing.JTextField tfVolta5;
    private javax.swing.JTextField tfVolta6;
    private javax.swing.JTextField tfVolta7;
    // End of variables declaration//GEN-END:variables
    private ValidaHorarioController controller;
}
