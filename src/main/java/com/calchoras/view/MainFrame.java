package com.calchoras.view;

import com.calchoras.model.Company;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class MainFrame extends JFrame {

    // Funcionários
    private DefaultListModel<Employee> employeeListModel;
    private JList<Employee> employeeList;
    private JButton addEmployeeButton;
    private JButton removeEmployeeButton;

    // Empresa
    private JComboBox<Company> companyComboBox;
    private JButton addCompanyButton;

    // Campos do funcionário
    private JTextField nameField;
    private JTextField shiftInField;
    private JTextField shiftOutField;
    private JTextField lunchBreakMinutesField;

    // Campos de ponto
    private JFormattedTextField dateField;
    private JTextField clockInField;
    private JTextField lunchInField;
    private JTextField lunchOutField;
    private JTextField clockOutField;
    private JCheckBox isDayOffCheckBox;

    // Botões de ponto
    private JButton nextEntryButton;
    private JButton previousEntryButton;
    private JButton addTimeEntryButton;
    private JButton removeTimeEntryButton;

    // Ações principais
    private JButton resetCalculationButton;
    private JButton calculateButton;
    private JButton printReportButton;

    // Resultado
    private JTextArea resultArea;

    public MainFrame() {
        super("Calchoras - Cálculadora de Horas Extras");

        initFrame();
        initComponents();
        layoutComponents();
    }

    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 520);
        setMinimumSize(new Dimension(500, 520));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {

        // LISTA DE FUNCIONÁRIOS
        employeeListModel = new DefaultListModel<>();
        employeeList = new JList<>(employeeListModel);

        addEmployeeButton = new JButton("Salvar Funcionário");
        removeEmployeeButton = new JButton("Remover Funcionário");

        // EMPRESA
        companyComboBox = new JComboBox<>();
        addCompanyButton = new JButton("Cadastrar Empresa");

        // CAMPOS DO FUNCIONÁRIO
        nameField = new JTextField();
        shiftInField = new JTextField();
        shiftOutField = new JTextField();
        lunchBreakMinutesField = new JTextField();

        // CAMPOS DE PONTO
        dateField = new JFormattedTextField();
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
        // PAINEL ESQUERDO — LISTA
        // ================================
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Funcionários"));
        leftPanel.setPreferredSize(new Dimension(180, 300));

        leftPanel.add(new JScrollPane(employeeList), BorderLayout.CENTER);

        // ================================
        // PAINEL CENTRAL — CADASTRAR EMPRESA + FUNCIONÁRIO + PONTO
        // ================================

        // --- CADASTRAR EMPRESA ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // --- DADOS DO FUNCIONÁRIO ---
        JPanel employeeInfoPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        employeeInfoPanel.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));

        JPanel companyPanel = new JPanel(new GridLayout(1, 1));
        companyPanel.add(addCompanyButton);
        centerPanel.add(companyPanel, BorderLayout.NORTH);

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

        // Botões do funcionário (agora logo após os campos)
        JPanel employeeActionButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        employeeActionButtons.add(addEmployeeButton);
        employeeActionButtons.add(removeEmployeeButton);

        // --- REGISTRO DE PONTO ---
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
        // PAINEL INFERIOR — AÇÕES GERAIS
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

        // Adiciona tudo ao frame
        this.add(leftPanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);

        this.pack();
        this.setMinimumSize(this.getSize());
    }

    // ================================
    // MÉTODOS DE ATUALIZAÇÃO
    // ================================
    public void updateEmployeeList(List<Employee> employees) {
        employeeListModel.clear();
        employees.forEach(employeeListModel::addElement);
    }

    public void updateCompanyList(List<Company> companies) {
        companyComboBox.removeAllItems();
        for (Company company : companies) {
            companyComboBox.addItem(company);
        }
    }

    public int getSelectedCompanyId() {
        Company selectedCompany = (Company) companyComboBox.getSelectedItem();
        if (selectedCompany != null) {
            return selectedCompany.getId();
        }
        // Retornar um valor padrão/erro ou lançar exceção se nada estiver selecionado
        return -1;
    }

    public void displayEmployeeInfo(Employee employee) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        companyComboBox.setSelectedItem(employee.getName());
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
        dateField.setValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        clockInField.setText("");
        lunchOutField.setText("");
        lunchInField.setText("");
        clockOutField.setText("");
    }

    public void clearEmployeeInfoFields() {
        nameField.setText("");
        shiftInField.setText("");
        shiftOutField.setText("");
        lunchBreakMinutesField.setText("");
    }

}
