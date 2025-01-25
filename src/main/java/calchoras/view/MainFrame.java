package calchoras.view;
import calchoras.model.*;
import javax.swing.ImageIcon;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends javax.swing.JFrame {
    
    public MainFrame() {
        initComponents(); 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        lblTitulo = new javax.swing.JLabel();
        lblMes = new javax.swing.JLabel();
        cbMes = new javax.swing.JComboBox<>();
        lblAno = new javax.swing.JLabel();
        lblCargaHorariaDiaria = new javax.swing.JLabel();
        tfCargaHorariaDiaria = new javax.swing.JTextField();
        lblCargaHorariaDiaria1 = new javax.swing.JLabel();
        lblFeriados = new javax.swing.JLabel();
        spFeriados = new javax.swing.JSpinner();
        lblDiasUteis1 = new javax.swing.JLabel();
        lblDiasUteis = new javax.swing.JLabel();
        spAno = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();
        btnComecar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calchoras: Cálculo de Horas Extras");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImages(null);
        setLocation(new java.awt.Point(0, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTitulo.setText("Cálculo de Horas Extras");

        lblMes.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblMes.setText("Mês:");

        cbMes.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" }));
        cbMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMesActionPerformed(evt);
            }
        });

        lblAno.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblAno.setText("Ano:");

        lblCargaHorariaDiaria.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblCargaHorariaDiaria.setText("Carga Horária Diária:");

        tfCargaHorariaDiaria.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tfCargaHorariaDiaria.setText("8");

        lblCargaHorariaDiaria1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblCargaHorariaDiaria1.setText("horas.");

        lblFeriados.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblFeriados.setText("Quantos feriados há no mês:");

        spFeriados.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        spFeriados.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        spFeriados.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spFeriadosStateChanged(evt);
            }
        });

        lblDiasUteis1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblDiasUteis1.setText("Dias Úteis:");

        lblDiasUteis.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        spAno.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        spAno.setModel(new javax.swing.SpinnerNumberModel(1500, 1500, null, 1));
        spAno.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        spAno.setEditor(new javax.swing.JSpinner.NumberEditor(spAno, "0"));
        spAno.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spAnoStateChanged(evt);
            }
        });

        btnComecar.setText("Começar");
        btnComecar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComecarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblMes)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbMes, 0, 0, Short.MAX_VALUE))
                                    .addComponent(lblCargaHorariaDiaria))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblAno)
                                    .addComponent(tfCargaHorariaDiaria, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(spAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblFeriados)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spFeriados, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblDiasUteis1))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblCargaHorariaDiaria1)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnComecar)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDiasUteis, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDiasUteis, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblFeriados)
                        .addComponent(spFeriados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDiasUteis1)
                        .addComponent(lblAno)
                        .addComponent(spAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCargaHorariaDiaria)
                    .addComponent(tfCargaHorariaDiaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCargaHorariaDiaria1)
                    .addComponent(btnComecar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //Define ícone da janela
        URL imageUrl = MainFrame.class.getClassLoader().getResource("calc_icon.png");
        setIconImage(new ImageIcon(imageUrl).getImage());

        // Obtém ano e mês atuais
        Calendar calendario = Calendar.getInstance();
        int anoAtual = calendario.get(Calendar.YEAR);
        int mesAtual = calendario.get(Calendar.MONTH) + 1;

        //Define ano e mês atuais para os campos spAno e cbMes respectivamente
        spAno.setValue(anoAtual);
        cbMes.setSelectedIndex(mesAtual - 1);

        //Define os dias úteis no mês para o lblDiasUteis
        lblDiasUteis.setText(Integer.toString(DiasUteis.calcularDiasUteis(anoAtual, mesAtual)));   
    }//GEN-LAST:event_formWindowOpened

    private void spFeriadosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spFeriadosStateChanged
        //Obtém feriados, ano e mês
        int feriados = (int)spFeriados.getValue();
        int ano = (int)spAno.getValue();
        int mes = cbMes.getSelectedIndex() + 1;
       
        //Atualiza o lblDiasUteis
        lblDiasUteis.setText(Integer.toString(DiasUteis.calcularDiasUteis(ano, mes, feriados)));
    }//GEN-LAST:event_spFeriadosStateChanged

    private void cbMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMesActionPerformed
        //Obtém feriados, ano e mês
        int feriados = (int)spFeriados.getValue();
        int ano = (int)spAno.getValue();
        int mes = cbMes.getSelectedIndex() + 1;

        //Atualiza o rótulo com o novo valor
        lblDiasUteis.setText(Integer.toString(DiasUteis.calcularDiasUteis(ano, mes, feriados)));
    }//GEN-LAST:event_cbMesActionPerformed

    private void spAnoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spAnoStateChanged
        //Obtém feriados, ano e mês
        int feriados = (int)spFeriados.getValue();
        int ano = (int)spAno.getValue();
        int mes = cbMes.getSelectedIndex() + 1;

        //Atualiza o rótulo com o novo valor
        lblDiasUteis.setText(Integer.toString(DiasUteis.calcularDiasUteis(ano, mes, feriados)));
        System.out.print(spAno.getUI());
    }//GEN-LAST:event_spAnoStateChanged

    private void btnComecarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComecarActionPerformed
        
    }//GEN-LAST:event_btnComecarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnComecar;
    private javax.swing.JComboBox<String> cbMes;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblAno;
    private javax.swing.JLabel lblCargaHorariaDiaria;
    private javax.swing.JLabel lblCargaHorariaDiaria1;
    private javax.swing.JLabel lblDiasUteis;
    private javax.swing.JLabel lblDiasUteis1;
    private javax.swing.JLabel lblFeriados;
    private javax.swing.JLabel lblMes;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JSpinner spAno;
    private javax.swing.JSpinner spFeriados;
    private javax.swing.JTextField tfCargaHorariaDiaria;
    // End of variables declaration//GEN-END:variables
}
