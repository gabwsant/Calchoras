package com.calchoras.view;

import com.calchoras.dto.CompanyDTO;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class CompanyDialog extends JDialog {

    private final JTextField nameField;

    @Getter
    private Integer companyId = null;

    @Getter
    private final JButton saveButton;
    @Getter
    private final JButton deleteButton;
    @Getter
    private final JButton cancelButton;

    public CompanyDialog(Frame parent) {
        this(parent, null);
    }

    public CompanyDialog(Frame parent, CompanyDTO companyToEdit) {
        super(parent, true);

        setTitle(companyToEdit == null ? "Calchoras - Nova Empresa" : "Calchoras - Editar Empresa");

        nameField = new JTextField(20);

        if (companyToEdit != null) {
            this.companyId = companyToEdit.id();
            nameField.setText(companyToEdit.name());
        }

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome da Empresa:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);

        saveButton = new JButton(companyToEdit == null ? "Salvar" : "Atualizar");
        deleteButton = new JButton("Excluir");
        cancelButton = new JButton("Cancelar");

        deleteButton.setForeground(Color.RED);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        if (companyToEdit != null) {
            buttonPanel.add(deleteButton);
        }
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setResizable(false);

        cancelButton.addActionListener(e -> dispose());
    }

    public String getCompanyName() {
        return nameField.getText().trim();
    }
}