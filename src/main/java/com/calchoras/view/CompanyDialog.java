package com.calchoras.view;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class CompanyDialog extends JDialog {

    private JTextField nameField;
    @Getter
    private JButton saveButton;
    private JButton cancelButton;

    public CompanyDialog(JFrame parent) {
        super(parent, "Cadastrar Empresa", true);

        nameField = new JTextField();

        saveButton = new JButton("Salvar");
        cancelButton = new JButton("Cancelar");

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nome da Empresa:"));
        panel.add(nameField);

        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);

        pack();
        setLocationRelativeTo(parent);

        // Botão cancelar
        cancelButton.addActionListener(e -> dispose());
    }

    public String getCompanyName() {
        return nameField.getText();
    }
}
