package com.calchoras.view;

import com.calchoras.model.Company;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import lombok.Getter;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class MainFrame extends JFrame {

    MaskFormatter dateMask;

    // Employees
    private DefaultListModel<EmployeeListItem> employeeListModel;
    private JList<EmployeeListItem> employeeList;
    private JButton addEmployeeButton;
    private JButton updateEmployeeButton;
    private JButton removeEmployeeButton;

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
        enableTimeEntryFields(false);
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 520);
        setMinimumSize(new Dimension(500, 520));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {

        // EMPLOYEES LIST
        employeeListModel = new DefaultListModel<>();
        employeeList = new JList<>(employeeListModel);

        // EMPLOYEES BUTTONS
        addEmployeeButton = new JButton("Cadastrar Funcionário");
        updateEmployeeButton = new JButton("Salvar Funcionário");
        removeEmployeeButton = new JButton("Remover Funcionário");

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

        this.setLayout(new BorderLayout(10, 10));
        ((JPanel) this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ================================
        // LEFT PANEL — LIST
        // ================================
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Funcionários"));
        leftPanel.setPreferredSize(new Dimension(180, 300));

        leftPanel.add(new JScrollPane(employeeList), BorderLayout.CENTER);

        // ================================
        // CENTER PANEL — REGISTER COMPANY + REGISTER EMPLOYEE + TIME ENTRY
        // ================================

        // --- REGISTER COMPANY AND EMPLOYEE ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // --- EMPLOYEE DATA ---
        JPanel employeeInfoPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        employeeInfoPanel.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));

        JPanel insertPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        insertPanel.add(addCompanyButton);
        insertPanel.add(addEmployeeButton);
        centerPanel.add(insertPanel, BorderLayout.NORTH);

        employeeInfoPanel.add(new JLabel("Empresa:"));
        employeeInfoPanel.add(companyComboBox);

        employeeInfoPanel.add(new JLabel("Nome:"));
        employeeInfoPanel.add(nameField);

        employeeInfoPanel.add(new JLabel("Início da Jornada:"));
        employeeInfoPanel.add(shiftInField);

        employeeInfoPanel.add(new JLabel("Fim da Jornada:"));
        employeeInfoPanel.add(shiftOutField);

        employeeInfoPanel.add(new JLabel("Almoço (min):"));
        employeeInfoPanel.add(lunchBreakMinutesField);

        // Employee buttons
        JPanel employeeActionButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        employeeActionButtons.add(updateEmployeeButton);
        employeeActionButtons.add(removeEmployeeButton);

        // --- TIME ENTRY ---
        JPanel timeEntryPanel = new JPanel(new GridLayout(6, 2, 5, 5)); // <-- 6, 2
        timeEntryPanel.setBorder(BorderFactory.createTitledBorder("Registro de Ponto"));

        timeEntryPanel.add(new JLabel("Data:"));
        timeEntryPanel.add(dateField);

        timeEntryPanel.add(new JLabel("Entrada:"));
        timeEntryPanel.add(clockInField);

        timeEntryPanel.add(new JLabel("Saída Almoço:"));
        timeEntryPanel.add(lunchInField);

        timeEntryPanel.add(new JLabel("Volta Almoço:"));
        timeEntryPanel.add(lunchOutField);

        timeEntryPanel.add(new JLabel("Saída:"));
        timeEntryPanel.add(clockOutField);

        timeEntryPanel.add(isDayOffCheckBox);

        JPanel entryActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        entryActionsPanel.add(previousEntryButton);
        entryActionsPanel.add(addTimeEntryButton);
        entryActionsPanel.add(removeTimeEntryButton);
        entryActionsPanel.add(nextEntryButton);

        centerPanel.add(employeeInfoPanel);
        centerPanel.add(employeeActionButtons);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(timeEntryPanel);
        centerPanel.add(entryActionsPanel);

        // ================================
        // BOTTOM PANEL — MAIN ACTIONS
        // ================================
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));

        JPanel mainActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainActionsPanel.add(resetCalculationButton);
        mainActionsPanel.add(calculateButton);
        mainActionsPanel.add(printReportButton);

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Resultados / Relatório"));
        resultScrollPane.setPreferredSize(new Dimension(100, 150));

        southPanel.add(mainActionsPanel, BorderLayout.NORTH);
        southPanel.add(resultScrollPane, BorderLayout.CENTER);

        this.add(leftPanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);

        this.pack();
        this.setMinimumSize(this.getSize());
    }

    public void updateEmployeeList(List<Employee> employees) {
        employeeListModel.clear();
        for (Employee emp : employees) {
            employeeListModel.addElement(
                    new EmployeeListItem(emp.getId(), emp.getName())
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
    }

    public void displayTimeEntry(TimeEntry timeEntry) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dateField.setValue(timeEntry.getEntryDate().format(dateFormatter));
        clockInField.setText(timeEntry.getClockIn().format(timeFormatter));
        lunchOutField.setText(timeEntry.getLunchOut().format(timeFormatter));
        lunchInField.setText(timeEntry.getLunchIn().format(timeFormatter));
        clockOutField.setText(timeEntry.getClockOut().format(timeFormatter));
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
    }

    public boolean showConfirmationDialog(String message) {
        Object[] options = {"Sim", "Não"};
        int decision = JOptionPane.showOptionDialog(this, message, "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);

        if  (decision == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

}
