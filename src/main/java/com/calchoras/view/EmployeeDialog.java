package com.calchoras.view;

import com.calchoras.dto.CompanyDTO;
import com.calchoras.model.Company;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class EmployeeDialog extends JDialog {

    private final JComboBox<CompanyDTO> companyComboBox;
    private final JTextField nameField;
    @Getter
    private final JTextField shiftIn;
    @Getter
    private final JTextField shiftOut;
    private final JTextField lunchBreak;
    @Getter
    private final JButton saveButton;

    public EmployeeDialog(JFrame parent) {
        super(parent, "Calchoras - Cadastrar Funcionário", true);

        companyComboBox = new JComboBox<>();
        nameField = new JTextField();
        shiftIn = new JTextField();
        shiftOut = new JTextField();
        lunchBreak = new JTextField();

        saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Empresa:"));
        panel.add(companyComboBox);

        panel.add(new JLabel("Nome do Funcionário:"));
        panel.add(nameField);

        panel.add(new JLabel("Início da Jornada:"));
        panel.add(shiftIn);

        panel.add(new JLabel("Fim da Jornada:"));
        panel.add(shiftOut);

        panel.add(new JLabel("Almoço(min):"));
        panel.add(lunchBreak);

        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);

        pack();
        setLocationRelativeTo(parent);

        cancelButton.addActionListener(e -> dispose());
    }

    public void updateCompanyList(List<CompanyDTO> companies) {
        companyComboBox.removeAllItems();
        for (CompanyDTO company : companies) {
            companyComboBox.addItem(company);
        }
    }

    public int getCompanyId() throws IllegalArgumentException {
        if (companyComboBox.getSelectedItem() == null) {
            throw new IllegalArgumentException("Selecione uma empresa!");
        } else {
            return ((CompanyDTO) companyComboBox.getSelectedItem()).id();
        }
    }

    public String getName() throws IllegalArgumentException {
        if(nameField.getText().isBlank()){
            throw new IllegalArgumentException("Nome é obrigatório!");
        } else {
            return nameField.getText();
        }
    }

    public String getShiftInStr() throws IllegalArgumentException {
        if(shiftIn.getText().isBlank()){
            throw new IllegalArgumentException("Início da jornada é obrigatório!");
        } else {
            return shiftIn.getText();
        }
    }

    public String getShiftOutStr() throws IllegalArgumentException {
        if(shiftOut.getText().isBlank()){
            throw new IllegalArgumentException("Fim da jornada é obrigatório!");
        } else {
            return shiftOut.getText();
        }
    }

    public String getLunchBreak() throws IllegalArgumentException {
        if(lunchBreak.getText().isBlank()){
            throw new IllegalArgumentException("Almoço é obrigatório!");
        } else {
            return lunchBreak.getText();
        }
    }

    public void setSelectedCompany(int companyId) {
        for (int i = 0; i < companyComboBox.getItemCount(); i++) {
            CompanyDTO item = companyComboBox.getItemAt(i);
            if (item != null && item.id() == companyId) {
                companyComboBox.setSelectedItem(item);
                return;
            }
        }
    }

    public void addAutoAdvanceToField(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void check() {
                if (field.getText().trim().length() == 5) {
                    SwingUtilities.invokeLater(() -> {
                        if (field.isFocusOwner()) {
                            field.transferFocus();
                        }
                    });
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) { check(); }
            @Override
            public void removeUpdate(DocumentEvent e) { }
            @Override
            public void changedUpdate(DocumentEvent e) { check(); }
        });
    }
}