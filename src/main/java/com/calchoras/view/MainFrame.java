package com.calchoras.view;

import com.calchoras.model.Company;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class MainFrame extends JFrame {

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
    private JComboBox<CompanyComboItem> companyComboBox;
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

    public MainFrame() {
        super("Calchoras - Cálculadora de Horas Extras");

        try {
            dateMask = new MaskFormatter("##/##/####");
        } catch (ParseException e){
            throw new RuntimeException(e);
        }

        initFrame();
        initComponents();
        layoutComponents();
        enableEmployeeFields(false);
        enableTimeEntryFields(false);
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
        updateEmployeeButton = new JButton("Salvar Funcionário");
        removeEmployeeButton = new JButton("Remover Funcionário");
        enableEmployeeButton = new JButton("Habilitar Funcionário");

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
        addTimeEntryButton = new JButton("Adicionar");
        removeTimeEntryButton = new JButton("Remover");
        nextEntryButton = new JButton("Próximo");

        resetCalculationButton = new JButton("Reiniciar");
        calculateButton = new JButton("Calcular");
        printReportButton = new JButton("Gerar Relatório");

        resultArea = new JTextArea();
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

        // Employee action buttons
        JPanel employeeActionButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        employeeActionButtons.add(updateEmployeeButton);
        //employeeActionButtons.add(removeEmployeeButton);
        employeeActionButtons.add(enableEmployeeButton);

        centerFormPanel.add(employeeInfoPanel);
        centerFormPanel.add(employeeActionButtons, "growx");

        // --- TIME ENTRIES ---
        JPanel timeEntryPanel = new JPanel(new MigLayout("wrap 4, fillx, insets 5", "[right][grow, fill][right][grow, fill]", "[]5[]"));
        timeEntryPanel.setBorder(BorderFactory.createTitledBorder("Registro de Ponto"));

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

        centerFormPanel.add(timeEntryPanel);

        // Nav buttons + time entry action buttons
        JPanel entryActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        entryActionsPanel.add(previousEntryButton);
        entryActionsPanel.add(addTimeEntryButton);
        entryActionsPanel.add(removeTimeEntryButton);
        entryActionsPanel.add(nextEntryButton);

        centerFormPanel.add(entryActionsPanel, "growx");

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

    public void updateEmployeeList(List<Employee> employees) {
        employeeListModel.clear();
        for (Employee emp : employees) {
            employeeListModel.addElement(
                    new EmployeeListItem(emp.getId(), emp.getName(), emp.isActive())
            );
        }
    }

    public void updateCompanyList(List<Company> companies) {
        companyComboBox.removeAllItems();
        for (Company company : companies) {
            companyComboBox.addItem(
                    new CompanyComboItem(company.getId(), company.getName())
            );
        }
    }

    public int getSelectedCompanyId() {
        CompanyComboItem item = (CompanyComboItem) companyComboBox.getSelectedItem();
        return (item != null) ? item.getId() : -1;
    }

    public void displayEmployeeInfo(Employee employee) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        companyComboBox.setSelectedItem(employee.getName());
        nameField.setText(employee.getName());
        shiftInField.setText(employee.getShiftIn().format(timeFormatter));
        shiftOutField.setText(employee.getShiftOut().format(timeFormatter));
        lunchBreakMinutesField.setText(String.valueOf(employee.getLunchBreakMinutes()));

        if (employee.isActive()) {
            enableEmployeeButton.setText("Desabilitar Funcionário");
        } else {
            enableEmployeeButton.setText("Habilitar Funcionário");
        }

    }

    public void displayTimeEntry(TimeEntry timeEntry) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dateField.setValue(timeEntry.getEntryDate().format(dateFormatter));

        if (timeEntry.getClockIn() != null) {
            clockInField.setText(timeEntry.getClockIn().format(timeFormatter));
        }
        if (timeEntry.getLunchIn() != null) {
            lunchInField.setText(timeEntry.getLunchIn().format(timeFormatter));
        }
        if (timeEntry.getLunchOut() != null) {
            lunchOutField.setText(timeEntry.getLunchOut().format(timeFormatter));
        }

        if (timeEntry.getClockOut() != null) {
            clockOutField.setText(timeEntry.getClockOut().format(timeFormatter));
        }

        isDayOffCheckBox.setSelected(timeEntry.isDayOff());
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

    public void resetDateField() {
        dateField.setValue(
                LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1)
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void enableTimeEntryFields(boolean enable) {
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

    public void enableEmployeeFields(boolean enable) {
        nameField.setEnabled(enable);
        shiftInField.setEnabled(enable);
        shiftOutField.setEnabled(enable);
        lunchBreakMinutesField.setEnabled(enable);
        updateEmployeeButton.setEnabled(enable);
        removeEmployeeButton.setEnabled(enable);
        enableEmployeeButton.setEnabled(enable);
    }

    public boolean showConfirmationDialog(String message) {
        Object[] options = {"Sim", "Não"};
        int decision = JOptionPane.showOptionDialog(this, message, "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);

        return decision == JOptionPane.YES_OPTION;
    }
    public void addAutoAdvanceToField(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {

            private void check() {
                if (field.getText().length() == 5) {
                    field.transferFocus();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }
}
