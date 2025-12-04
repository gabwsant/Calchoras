package com.calchoras.controller;

import com.calchoras.model.Company;
import com.calchoras.model.Employee;
import com.calchoras.service.interfaces.*;
import com.calchoras.util.validators.DateFieldValidator;
import com.calchoras.util.validators.TimeFieldValidator;
import com.calchoras.view.CompanyComboItem;
import com.calchoras.view.CompanyDialog;
import com.calchoras.view.EmployeeListItem;
import com.calchoras.view.MainFrame;

import java.awt.event.ItemEvent;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class MainFrameController {
    private final IDailyCalculationService dailyCalculationService;
    private final IEmployeeService employeeService;
    private final ICompanyService companyService;
    private final IReportService reportService;
    private final ITimeEntryService timeEntryService;
    private final MainFrame view;

    public MainFrameController(
            MainFrame view,
            ICompanyService companyService,
            IDailyCalculationService dailyCalculationService,
            IEmployeeService employeeService,
            IReportService reportService,
            ITimeEntryService timeEntryService
    ) {
        this.view = view;
        this.dailyCalculationService = dailyCalculationService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.reportService = reportService;
        this.timeEntryService = timeEntryService;

        initValidators();

        view.getAddCompanyButton().addActionListener(e -> openCompanyDialog());

        view.getCompanyComboBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                CompanyComboItem item = (CompanyComboItem) e.getItem();
                if (item != null) {
                    handleCompanySelectionChange(item.getId());
                }
            }
        });

        view.getAddEmployeeButton().addActionListener(e -> handleAddEmployeeAction());

        view.getRemoveEmployeeButton().addActionListener(e -> handleRemoveEmployeeAction());

        view.getEmployeeList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                EmployeeListItem item = view.getEmployeeList().getSelectedValue();
                if (item != null) {
                    view.enableTimeEntryFields(true);
                    handleEmployeeSelection(item.getId());
                } else {
                    view.clearTimeEntryFields();
                    view.enableTimeEntryFields(false);
                }
            }
        });

        loadInitalData();
    }

    private void initValidators() {
        DateFieldValidator.apply(view.getDateField());
        TimeFieldValidator.apply(view.getClockInField());
        TimeFieldValidator.apply(view.getLunchInField());
        TimeFieldValidator.apply(view.getLunchOutField());
        TimeFieldValidator.apply(view.getClockOutField());
    }

    private void handleEmployeeSelection(int employeeId) {
        employeeService.findById(employeeId)
                .ifPresent(view::displayEmployeeInfo);
    }

    private void handleAddEmployeeAction() {
        int companyId = view.getSelectedCompanyId();
        String name = view.getNameField().getText();
        String shiftInStr = view.getShiftInField().getText();
        String shiftOutStr = view.getShiftOutField().getText();
        String lunchBreakMinutesStr = view.getLunchBreakMinutesField().getText();

        try {
            if (companyId <= 0) {
                throw new IllegalArgumentException("Selecione uma empresa válida.");
            }

            // campos obrigatórios
            if (name.isBlank() || shiftInStr.isBlank() || shiftOutStr.isBlank() || lunchBreakMinutesStr.isBlank()) {
                throw new IllegalArgumentException("Todos os campos de funcionário são obrigatórios.");
            }

            LocalTime shiftIn = LocalTime.parse(shiftInStr);
            LocalTime shiftOut = LocalTime.parse(shiftOutStr);
            int lunchBreakMinutes = Integer.parseInt(lunchBreakMinutesStr);

            Employee employeeToAdd = new Employee(
                    companyId,
                    name,
                    shiftIn,
                    shiftOut,
                    lunchBreakMinutes
            );

            employeeService.save(employeeToAdd);

            view.clearEmployeeInfoFields();

            handleCompanySelectionChange(companyId);

            view.showSuccess("Funcionário salvo com sucesso!");

        } catch (java.time.format.DateTimeParseException | NumberFormatException e) {
            view.showError("Erro no formato de Hora (HH:mm) ou Minutos (numérico).");
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError( "Erro ao salvar: " + e.getMessage());
        }
    }

    private void handleRemoveEmployeeAction() {
        try {
            int employeeId = view.getEmployeeList().getSelectedValue().getId();
            int companyId = view.getSelectedCompanyId();

            employeeService.deleteById(employeeId);

            view.clearEmployeeInfoFields();
            handleCompanySelectionChange(companyId);
        } catch (Exception e) {
            view.showError( "Erro ao deletar: " + e.getMessage());
        }
    }

    private void loadInitalData() {
        try{
            List<Company> companies = companyService.findAll();
            view.updateCompanyList(companies);
            view.clearTimeEntryFields();
        }
        catch (Exception e) {
            view.showError( "Erro ao carregar dados iniciais de empresas: " + e.getMessage());
        }
    }

    private void openCompanyDialog() {
        CompanyDialog dialog = new CompanyDialog(view);

        dialog.getSaveButton().addActionListener(e -> {
            String name = dialog.getCompanyName();

            if (name.isBlank()) {
                view.showError("Nome da empresa é obrigatório!");
                return;
            }

            handleAddCompanyAction(name);

            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    // Método de negócio (Controller chama o Service)
    private void handleAddCompanyAction(String name) {
        try {
            Company companyToSave = new Company(name);

            Company newCompany = companyService.save(companyToSave);

            view.getCompanyComboBox().addItem(
                    new CompanyComboItem(newCompany.getId(), newCompany.getName())
            );

        } catch (IllegalArgumentException ex) {
            view.showError( "Empresa já existe.");
        } catch (Exception ex) {
            view.showError("Erro ao salvar a empresa: " + ex.getMessage());
        }
    }

    // Método de ação que será chamado quando a seleção mudar
    public void handleCompanySelectionChange(int companyId) {
        List<Employee> employees = employeeService.findByCompanyId(companyId);
        employees.sort(Comparator.comparing(Employee::getName));
        view.updateEmployeeList(employees);
        view.clearEmployeeInfoFields();
    }

}
