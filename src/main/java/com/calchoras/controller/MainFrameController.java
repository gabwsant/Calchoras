package com.calchoras.controller;

import com.calchoras.model.Company;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.*;
import com.calchoras.util.validators.TimeFieldValidator;
import com.calchoras.view.*;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class MainFrameController {
    private final IDailyCalculationService dailyCalculationService;
    private final IEmployeeService employeeService;
    private final ICompanyService companyService;
    private final IReportService reportService;
    private final ITimeEntryService timeEntryService;
    private final MainFrame view;
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

        initAutoAdvance();

        view.getAddCompanyButton().addActionListener(e -> openCompanyDialog());

        view.getAddEmployeeButton().addActionListener(e -> openEmployeeDialog());

        view.getCompanyComboBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                CompanyComboItem item = (CompanyComboItem) e.getItem();
                if (item != null) {
                    handleCompanySelectionChange(item.getId());
                }
            }
        });

        view.getUpdateEmployeeButton().addActionListener(e -> handleUpdateEmployeeAction());

        view.getRemoveEmployeeButton().addActionListener(e -> handleRemoveEmployeeAction());

        view.getEmployeeList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                EmployeeListItem item = view.getEmployeeList().getSelectedValue();
                if (item != null) {
                    view.enableEmployeeFields(true);
                    view.enableTimeEntryFields(true);
                    handleEmployeeSelection(item.getId());
                } else {
                    view.clearEmployeeInfoFields();
                    view.clearTimeEntryFields();
                    view.enableEmployeeFields(false);
                    view.enableTimeEntryFields(false);
                }
            }
        });

        view.getAddTimeEntryButton().addActionListener(e -> {handleAddTimeEntryAction();});

        loadInitalData();
    }

    private void handleUpdateEmployeeAction() {
        EmployeeListItem selectedEmployee = view.getEmployeeList().getSelectedValue();

        if (selectedEmployee == null) {
            view.showError("Selecione um funcionário para atualizar!");
            return;
        }

        try {
            int employeeId = selectedEmployee.getId();
            int companyId = view.getSelectedCompanyId();
            String emplooyeName = view.getNameField().getText();
            String shiftInStr = view.getShiftInField().getText();
            String shiftOutStr = view.getShiftOutField().getText();
            String lunchBreakMinutesStr = view.getLunchBreakMinutesField().getText();

            if (!employeeService.existsById(employeeId)) {
                throw new IllegalArgumentException("Funcionário não encontrado na base de dados!");
            }

            if (!companyService.existsById(companyId)) {
                throw new IllegalArgumentException("Empresa inválida!");
            }

            LocalTime shiftIn = LocalTime.parse(shiftInStr);
            LocalTime shiftOut = LocalTime.parse(shiftOutStr);
            int lunchBreakMinutes = Integer.parseInt(lunchBreakMinutesStr);

            Employee employeeToUpdate = new Employee(
                    employeeId,
                    companyId,
                    emplooyeName,
                    shiftIn,
                    shiftOut,
                    lunchBreakMinutes
            );

            employeeService.update(employeeToUpdate);

            view.clearEmployeeInfoFields();
            handleCompanySelectionChange(companyId);
            view.showSuccess("Funcionário salvo com sucesso!");

        } catch (java.time.format.DateTimeParseException | NumberFormatException e) {
            view.showError("Erro no formato de Hora (HH:mm) ou Minutos (numérico).");
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError("Erro ao salvar: " + e.getMessage());
        }
    }

    private void handleAddTimeEntryAction() {
        EmployeeListItem selectedEmployee = view.getEmployeeList().getSelectedValue();

        if (selectedEmployee == null) {
            view.showError("Selecione um funcionário antes de adicionar um ponto.");
            return;
        }
        try {
            int employeeId = selectedEmployee.getId();

            LocalDate date = LocalDate.parse(view.getDateField().getText(), DATE_FORMATTER);

            boolean isDayOff = view.getIsDayOffCheckBox().isSelected();

            LocalTime clockIn = parseTimeField(view.getClockInField().getText());
            LocalTime lunchIn = parseTimeField(view.getLunchInField().getText());
            LocalTime lunchOut = parseTimeField(view.getLunchOutField().getText());
            LocalTime clockOut = parseTimeField(view.getClockOutField().getText());

            if (!isDayOff && clockIn == null && clockOut == null && lunchIn == null && lunchOut == null) {
                throw new IllegalArgumentException("Selecione 'Dia de Folga' ou insira pelo menos um ponto de batida.");
            }

            TimeEntry timeEntry = new TimeEntry();
            timeEntry.setEmployeeId(employeeId);
            timeEntry.setEntryDate(date);
            timeEntry.setClockIn(clockIn);
            timeEntry.setLunchIn(lunchIn);
            timeEntry.setLunchOut(lunchOut);
            timeEntry.setClockOut(clockOut);
            timeEntry.setDayOff(isDayOff);

            timeEntryService.save(timeEntry);

            view.getDateField().setValue(date.plusDays(1).format(DATE_FORMATTER));
            view.clearTimeEntryFields();
            view.getResultArea().append("\nBatida adicionada para " + timeEntry.getEntryDate().format(DATE_FORMATTER));

        }catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Erro de formato de Data (DD/MM/AAAA) ou Hora (HH:MM).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar o ponto: " + e.getMessage(), "Erro Interno", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalTime parseTimeField(String timeStr) throws java.time.format.DateTimeParseException {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(timeStr.trim());
    }

    private void initAutoAdvance() {
        addAutoAdvanceToField(view.getClockInField());
        addAutoAdvanceToField(view.getLunchInField());
        addAutoAdvanceToField(view.getLunchOutField());
        addAutoAdvanceToField(view.getLunchInField());
    }

    private void initValidators() {
        TimeFieldValidator.apply(view.getShiftInField());
        TimeFieldValidator.apply(view.getShiftOutField());
        TimeFieldValidator.apply(view.getClockInField());
        TimeFieldValidator.apply(view.getLunchInField());
        TimeFieldValidator.apply(view.getLunchOutField());
        TimeFieldValidator.apply(view.getClockOutField());
    }

    private void handleEmployeeSelection(int employeeId) {
        employeeService.findById(employeeId)
                .ifPresent(view::displayEmployeeInfo);
    }

    private void handleAddEmployeeAction(
            int companyId,
            String name,
            String shiftInStr,
            String shiftOutStr,
            String lunchBreakMinutesStr
    ) {
        try {
            if (!companyService.existsById(companyId)) {
                throw new IllegalArgumentException("Selecione uma empresa válida.");
            }

            // mandatory fields
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
            if (!view.showConfirmationDialog("Deseja realmente remover o funcionário selecionado?")) {
                return;
            }

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
            view.getDateField().setValue(
                    LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1)
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
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

    private void openEmployeeDialog() {
        EmployeeDialog dialog = new EmployeeDialog(view);
        List<Company> companies = companyService.findAll();
        dialog.updateCompanyList(companies);

        //validators
        TimeFieldValidator.apply(dialog.getShiftIn());
        TimeFieldValidator.apply(dialog.getShiftOut());

        dialog.getSaveButton().addActionListener(e -> {
            try {
                int companyId = dialog.getCompanyId();
                String name = dialog.getName();
                String shiftIn = dialog.getShiftInStr();
                String shiftOut = dialog.getShiftOutStr();
                String lunchBreak = dialog.getLunchBreak();

                handleAddEmployeeAction(companyId, name, shiftIn, shiftOut, lunchBreak);

                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                view.showError(ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

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

    public void handleCompanySelectionChange(int companyId) {
        List<Employee> employees = employeeService.findByCompanyId(companyId);
        employees.sort(Comparator.comparing(Employee::getName));
        view.updateEmployeeList(employees);
        view.clearEmployeeInfoFields();
    }

    private void addAutoAdvanceToField (JTextField field) {
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (field.getText().length() == 5) {
                    field.transferFocus();
                }
            }
        });
    }

}
