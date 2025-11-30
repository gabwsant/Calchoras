package com.calchoras.controller;

import com.calchoras.model.Company;
import com.calchoras.model.Employee;
import com.calchoras.service.interfaces.*;
import com.calchoras.view.CompanyDialog;
import com.calchoras.view.MainFrame;

import javax.swing.*;
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

        view.getAddCompanyButton().addActionListener(e -> openCompanyDialog());

        view.getCompanyComboBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Company selectedCompany = (Company) e.getItem();
                handleCompanySelectionChange(selectedCompany);
            }
        });

        view.getAddEmployeeButton().addActionListener(e -> handleAddEmployeeAction());

        view.getRemoveEmployeeButton().addActionListener(e -> handleRemoveEmployeeAction());

        loadInitalData();
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

            Company currentCompany = (Company) view.getCompanyComboBox().getSelectedItem();
            handleCompanySelectionChange(currentCompany);

            JOptionPane.showMessageDialog(view, "Funcionário salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (java.time.format.DateTimeParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Erro no formato de Hora (HH:mm) ou Minutos (numérico).",
                    "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRemoveEmployeeAction() {
        try {
            Employee employeeToRemove = view.getEmployeeList().getSelectedValue();
            employeeService.deleteById(employeeToRemove.getId());
            view.clearEmployeeInfoFields();

            // atualiza lista de funcionários
            Company currentCompany = (Company) view.getCompanyComboBox().getSelectedItem();
            handleCompanySelectionChange(currentCompany);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao deletar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadInitalData() {
        try{
            List<Company> companies = companyService.findAll();
            view.updateCompanyList(companies);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao carregar dados iniciais de empresas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openCompanyDialog() {
        CompanyDialog dialog = new CompanyDialog(view);

        dialog.getSaveButton().addActionListener(e -> {
            String name = dialog.getCompanyName();

            if (name.isBlank()) {
                JOptionPane.showMessageDialog(view,
                        "Nome da empresa é obrigatório!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
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

            view.getCompanyComboBox().addItem(newCompany);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, "Empresa já existe.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Erro ao salvar a empresa: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método de ação que será chamado quando a seleção mudar
    public void handleCompanySelectionChange(Company selectedCompany) {
        if (selectedCompany != null) {
            int companyId = selectedCompany.getId();
            System.out.println("Empresa selecionada. ID: " + companyId);

            List<Employee> employees = employeeService.findByCompanyId(companyId);

            employees.sort(Comparator.comparing(Employee::getName));

            view.updateEmployeeList(employees);

        } else {
            //view.updateEmployeeList(null);
        }
    }

}
