package com.calchoras.view;

import com.calchoras.dto.CompanyDTO;
import com.calchoras.dto.EmployeeDTO;
import com.calchoras.dto.TimeEntryDTO;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Getter
public class MainFrame extends JFrame {

    // Formatters
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    MaskFormatter dateMask;

    // Employees
    private DefaultListModel<EmployeeListItem> employeeListModel;
    private JList<EmployeeListItem> employeeList;
    private JCheckBox showAllEmployees;
    private JButton addEmployeeButton;
    private JButton updateEmployeeButton;
    private JButton removeEmployeeButton;
    private JButton enableEmployeeButton;

    // Company
    private JComboBox<CompanyDTO> companyComboBox;
    private JButton addCompanyButton;

    // Employee fields
    private JTextField nameField;
    private JTextField shiftInField;
    private JTextField shiftOutField;
    private JTextField lunchBreakMinutesField;

    // Time entry fields
    private JFormattedTextField dateField;
    private JTextField clockInField;
    private JTextField lunchInField;
    private JTextField lunchOutField;
    private JTextField clockOutField;
    private JCheckBox isDayOffCheckBox;

    // Time entry buttons
    private JButton nextEntryButton;
    private JButton previousEntryButton;
    private JButton addTimeEntryButton;
    private JButton removeTimeEntryButton;

    // Main actions
    private JButton resetCalculationButton;
    private JButton calculateButton;
    private JButton printReportButton;

    // Result
    private JTextArea resultArea;

    // Mouse right button actions
    private JPopupMenu popupMenu;
    private JMenuItem toggleStatusItem;
    private JMenuItem removeEmployeeItem;

    public MainFrame() {
        super("Calchoras - Cálculadora de Horas Extras");

        try {
            dateMask = new MaskFormatter("##/##/####");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        initFrame();
        initComponents();
        layoutComponents();
        enableEmployeeFields(false);
        setTimeEntryEditModeEnabled(false);
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
    }

    private void initComponents() {

        // EMPLOYEES LIST
        employeeListModel = new DefaultListModel<>();
        employeeList = new JList<>(employeeListModel);
        employeeList.setCellRenderer(new EmployeeListRenderer());
        showAllEmployees = new JCheckBox("Mostrar tudo");

        // EMPLOYEES BUTTONS
        addEmployeeButton = new JButton("Cadastrar Funcionário");
        updateEmployeeButton = new JButton("Salvar alterações");
        removeEmployeeButton = new JButton("Remover Funcionário");
        enableEmployeeButton = new JButton("Habilitar");

        // COMPANY
        companyComboBox = new JComboBox<>();
        addCompanyButton = new JButton("Cadastrar Empresa");

        // EMPLOYEE FIELDS
        nameField = new JTextField();
        shiftInField = new JTextField();
        shiftOutField = new JTextField();
        lunchBreakMinutesField = new JTextField();

        // TIME ENTRY FIELDS
        dateField = new JFormattedTextField(dateMask);
        dateField.setColumns(10);
        clockInField = new JTextField();
        lunchInField = new JTextField();
        lunchOutField = new JTextField();
        clockOutField = new JTextField();
        isDayOffCheckBox = new JCheckBox("Dia de Folga (Ignorar Batidas)");

        previousEntryButton = new JButton("Anterior");
        addTimeEntryButton = new JButton("Salvar");
        removeTimeEntryButton = new JButton("Remover");
        nextEntryButton = new JButton("Próximo");

        resetCalculationButton = new JButton("Reiniciar");
        calculateButton = new JButton("Calcular");
        printReportButton = new JButton("Gerar Relatório");

        resultArea = new JTextArea();

        popupMenu = new JPopupMenu();
        toggleStatusItem = new JMenuItem();
        removeEmployeeItem = new JMenuItem("Excluir");
        popupMenu.add(toggleStatusItem);
        popupMenu.add(removeEmployeeItem);
    }

    private void layoutComponents() {
        this.setLayout(new BorderLayout());

        // ================================
        // LEFT PANEL — LIST
        // ================================
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Funcionários"));
        leftPanel.add(new JScrollPane(employeeList), BorderLayout.CENTER);
        leftPanel.add(showAllEmployees, BorderLayout.SOUTH);

        // ================================
        // CENTER PANEL — FORMS
        // ================================

        JPanel centerFormPanel = new JPanel();
        centerFormPanel.setLayout(new MigLayout("wrap 1, fillx, insets 10", "[grow, fill]", "[]10[]"));

        // --- INSERTION PANEL (COMPANY/EMPLOYEE) ---
        JPanel insertPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 0", "[][grow, fill]", "[]5[]"));

        // UPPER BUTTONS
        JPanel topButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topButtonsPanel.add(addCompanyButton);
        topButtonsPanel.add(Box.createHorizontalStrut(5));
        topButtonsPanel.add(addEmployeeButton);

        insertPanel.add(topButtonsPanel, "span 2, wrap");
        insertPanel.add(new JLabel("Empresa:"));
        insertPanel.add(companyComboBox);

        centerFormPanel.add(insertPanel);

        // --- EMPLOYEE DATA ---
        JPanel employeeInfoPanel = new JPanel(new MigLayout("wrap 4, fillx, insets 5", "[right][grow, fill][right][grow, fill]", "[]5[]"));
        employeeInfoPanel.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));

        employeeInfoPanel.add(new JLabel("Nome:"));
        employeeInfoPanel.add(nameField, "span 3");

        employeeInfoPanel.add(new JLabel("Início jornada:"));
        employeeInfoPanel.add(shiftInField);

        employeeInfoPanel.add(new JLabel("Fim jornada:"));
        employeeInfoPanel.add(shiftOutField);

        employeeInfoPanel.add(new JLabel("Almoço (min):"));
        employeeInfoPanel.add(lunchBreakMinutesField, "wrap");

        //Employee action buttons
        employeeInfoPanel.add(updateEmployeeButton, "span 4, split 2, center, width 200!");
        employeeInfoPanel.add(enableEmployeeButton, "width 200!");

        centerFormPanel.add(employeeInfoPanel);

        // --- TIME ENTRIES ---
        JPanel timeEntryPanel = new JPanel(new MigLayout("wrap 4, fillx, insets 5", "[right][grow, fill][right][grow, fill]", "[]5[]"));
        timeEntryPanel.setBorder(BorderFactory.createTitledBorder("Registro de Ponto"));

        // Nav buttons
        timeEntryPanel.add(previousEntryButton, "span 4, split 2, center, width 200!");
        timeEntryPanel.add(nextEntryButton, "width 200!");

        timeEntryPanel.add(new JLabel("Data:"));
        timeEntryPanel.add(dateField);

        timeEntryPanel.add(isDayOffCheckBox, "span 2");

        timeEntryPanel.add(new JLabel("Entrada:"));
        timeEntryPanel.add(clockInField);

        timeEntryPanel.add(new JLabel("Início Almoço:"));
        timeEntryPanel.add(lunchInField);

        timeEntryPanel.add(new JLabel("Fim Almoço:"));
        timeEntryPanel.add(lunchOutField);

        timeEntryPanel.add(new JLabel("Saída:"));
        timeEntryPanel.add(clockOutField);

        // Time entry action buttons
        timeEntryPanel.add(addTimeEntryButton, "span 4, split 2, center, width 200!");
        timeEntryPanel.add(removeTimeEntryButton, "width 200!");

        centerFormPanel.add(timeEntryPanel);

        // ================================
        // CENTER SCROLL PANE
        // ================================
        JScrollPane centerScrollPane = new JScrollPane(centerFormPanel);
        centerScrollPane.setBorder(null);
        centerScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // ================================
        // SPLIT PANE
        // ================================
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, centerScrollPane);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.0);

        // ================================
        // BOTTOM PANEL — ACTION AND RESULTS
        // ================================
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JPanel mainActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainActionsPanel.add(resetCalculationButton);
        mainActionsPanel.add(calculateButton);
        mainActionsPanel.add(printReportButton);

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Resultados / Relatório"));
        resultScrollPane.setPreferredSize(new Dimension(0, 150));

        southPanel.add(mainActionsPanel, BorderLayout.NORTH);
        southPanel.add(resultScrollPane, BorderLayout.CENTER);

        this.add(splitPane, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
    }

    // ===================================================================================
    // DATA UPDATE METHODS (MODEL -> VIEW)
    // ===================================================================================

    public void updateEmployeeList(List<EmployeeDTO> employees) {
        employeeListModel.clear();
        for (EmployeeDTO emp : employees) {
            employeeListModel.addElement(
                    new EmployeeListItem(emp.id(), emp.name(), emp.active())
            );
        }
    }

    public void updateCompanyList(List<CompanyDTO> companies) {
        companyComboBox.removeAllItems();
        for (CompanyDTO company : companies) {
            companyComboBox.addItem(company);
        }
    }

    public void displayEmployeeInfo(EmployeeDTO employeeDTO) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < companyComboBox.getItemCount(); i++) {
            CompanyDTO dto = companyComboBox.getItemAt(i);
            if (dto.id() == employeeDTO.companyId()) {
                companyComboBox.setSelectedIndex(i);
                break;
            }
        }

        nameField.setText(employeeDTO.name());

        if (employeeDTO.shiftIn() != null)
            shiftInField.setText(employeeDTO.shiftIn().format(timeFormatter));
        else
            shiftInField.setText("");

        if (employeeDTO.shiftOut() != null)
            shiftOutField.setText(employeeDTO.shiftOut().format(timeFormatter));
        else
            shiftOutField.setText("");

        lunchBreakMinutesField.setText(String.valueOf(employeeDTO.lunchBreakMinutes()));

        if (employeeDTO.active()) {
            enableEmployeeButton.setText("Desabilitar");
        } else {
            enableEmployeeButton.setText("Habilitar");
        }
    }

    public void displayTimeEntry(TimeEntryDTO timeEntryDTO) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (timeEntryDTO.clockIn() != null) {
            clockInField.setText(timeEntryDTO.clockIn().format(timeFormatter));
        }
        if (timeEntryDTO.lunchIn() != null) {
            lunchInField.setText(timeEntryDTO.lunchIn().format(timeFormatter));
        }
        if (timeEntryDTO.lunchOut() != null) {
            lunchOutField.setText(timeEntryDTO.lunchOut().format(timeFormatter));
        }
        if (timeEntryDTO.clockOut() != null) {
            clockOutField.setText(timeEntryDTO.clockOut().format(timeFormatter));
        }

        isDayOffCheckBox.setSelected(timeEntryDTO.dayOff());
    }

    public void resetDateField() {
        dateField.setValue(
                LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1)
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }

    public void plusEntryDate(int plusDays) {
        dateField.setValue(getEntryDate().plusDays(plusDays).format(DATE_FORMATTER));
    }

    public void clearTimeEntryFields() {
        clockInField.setText("");
        lunchInField.setText("");
        lunchOutField.setText("");
        clockOutField.setText("");
        isDayOffCheckBox.setSelected(false);
    }

    public void clearEmployeeInfoFields() {
        nameField.setText("");
        shiftInField.setText("");
        shiftOutField.setText("");
        lunchBreakMinutesField.setText("");
    }

    // ===================================================================================
    // DATA RETRIEVAL METHODS (VIEW -> CONTROLLER)
    // ===================================================================================

    public int getSelectedCompanyId() {
        CompanyDTO item = (CompanyDTO) companyComboBox.getSelectedItem();
        return (item != null) ? item.id() : -1;
    }

    public int getSelectedEmployeeId() {
        EmployeeListItem selected = employeeList.getSelectedValue();
        return (selected != null) ? selected.getId() : -1;
    }

    public EmployeeListItem getSelectedEmployeeItem() {
        return employeeList.getSelectedValue();
    }

    public boolean hasSelectedEmployee() {
        return !employeeList.isSelectionEmpty();
    }

    public boolean isSelectedEmployeeActive() {
        EmployeeListItem selected = employeeList.getSelectedValue();
        return (selected != null) && selected.isActive();
    }

    public LocalDate getEntryDate() {
        try {
            return LocalDate.parse(dateField.getText(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public LocalTime getClockIn() {
        return parseTime(clockInField.getText());
    }

    public LocalTime getLunchIn() {
        return parseTime(lunchInField.getText());
    }

    public LocalTime getLunchOut() {
        return parseTime(lunchOutField.getText());
    }

    public LocalTime getClockOut() {
        return parseTime(clockOutField.getText());
    }

    public boolean isDayOffSelected() {
        return isDayOffCheckBox.isSelected();
    }

    // ===================================================================================
    // UI STATE & BEHAVIOR METHODS
    // ===================================================================================

    public void setTimeEntryEditModeEnabled(boolean enable) {
        dateField.setEnabled(enable);
        clockInField.setEnabled(enable);
        lunchInField.setEnabled(enable);
        lunchOutField.setEnabled(enable);
        clockOutField.setEnabled(enable);
        isDayOffCheckBox.setEnabled(enable);
        previousEntryButton.setEnabled(enable);
        addTimeEntryButton.setEnabled(enable);
        removeTimeEntryButton.setEnabled(enable);
        nextEntryButton.setEnabled(enable);
    }

    public void setClockingFieldsEnabled(boolean enable) {
        clockInField.setEnabled(enable);
        lunchInField.setEnabled(enable);
        lunchOutField.setEnabled(enable);
        clockOutField.setEnabled(enable);
    }

    public void enableEmployeeFields(boolean enable) {
        nameField.setEnabled(enable);
        shiftInField.setEnabled(enable);
        shiftOutField.setEnabled(enable);
        lunchBreakMinutesField.setEnabled(enable);
        updateEmployeeButton.setEnabled(enable);
        removeEmployeeButton.setEnabled(enable);
        enableEmployeeButton.setEnabled(enable);
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

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public boolean showConfirmationDialog(String message) {
        Object[] options = {"Sim", "Não"};
        int decision = JOptionPane.showOptionDialog(this, message, "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        return decision == JOptionPane.YES_OPTION;
    }

    // ===================================================================================
    // PRIVATE HELPERS
    // ===================================================================================

    private LocalTime parseTime(String text) {
        if (text == null || text.trim().isEmpty() || text.contains("_")) {
            return null;
        }
        try {
            return LocalTime.parse(text, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}