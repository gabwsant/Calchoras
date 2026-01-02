package com.calchoras.controller;

import com.calchoras.model.Company;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.interfaces.*;
import com.calchoras.util.validators.TimeFieldValidator;
import com.calchoras.view.*;

import javax.swing.*;
import java.awt.event.*;
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

        view.getEnableEmployeeButton().addActionListener(e -> handleEnableEmployeeAction());

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

        view.getPreviousEntryButton().addActionListener(e -> handlePreviousEntryButton());

        view.getShowAllEmployees().addActionListener(e -> handleShowAllEmployeesChange());

        view.getToggleStatusItem().addActionListener(e -> handleEnableEmployeeAction());

        view.getRemoveEmployeeItem().addActionListener(e -> handleRemoveEmployeeAction());

        view.getNextEntryButton().addActionListener(e -> handleNextEntryAction());

        view.getDateField().addActionListener(e -> handleDateChangeAction());

        view.getDateField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                handleDateChangeAction();
            }
        });

        addHandleMouseRightClick();

        loadInitalData();
    }

    private void handleDateChangeAction() {
        EmployeeListItem selectedEmployee = view.getEmployeeList().getSelectedValue();

        if (selectedEmployee == null) {
            return;
        }

        int employeeId = selectedEmployee.getId();
        LocalDate date = LocalDate.parse(view.getDateField().getText(), DATE_FORMATTER);

        loadTimeEntry(employeeId, date);

        view.getIsDayOffCheckBox().requestFocus();
    }

    private void handleNextEntryAction() {
        EmployeeListItem selectedEmployee = view.getEmployeeList().getSelectedValue();

        if (selectedEmployee == null) {
            return;
        }

        int employeeId = selectedEmployee.getId();
        LocalDate date = LocalDate.parse(view.getDateField().getText(), DATE_FORMATTER).plusDays(1);
        view.getDateField().setText(date.format(DATE_FORMATTER));

        loadTimeEntry(employeeId, date);
    }

    private void addHandleMouseRightClick() {
        JList<EmployeeListItem> list = view.getEmployeeList();

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handlePopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handlePopup(e);
            }

            private void handlePopup(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = list.locationToIndex(e.getPoint());

                    if (index != -1 && list.getCellBounds(index, index).contains(e.getPoint())) {
                        list.setSelectedIndex(index);
                        EmployeeListItem item = list.getSelectedValue();

                        if (item != null) {
                            view.getToggleStatusItem().setText(item.isActive() ?
                                    "Desabilitar Funcionário" : "Habilitar Funcionário");

                            view.getPopupMenu().show(list, e.getX(), e.getY());
                        }
                    }
                }
            }
        });
    }

    private void handleShowAllEmployeesChange() {
        int companyId = view.getSelectedCompanyId();
        handleCompanySelectionChange(companyId);
    }

    private void handleEnableEmployeeAction() {
        EmployeeListItem selectedEmployee = view.getEmployeeList().getSelectedValue();

        if (selectedEmployee == null) {
            view.showError("Selecione um funcionário para realizar a ação!");
            return;
        }
        try {
            int employeeId = selectedEmployee.getId();
            int companyId = view.getSelectedCompanyId();

            if (!employeeService.existsById(employeeId)) {
                throw new IllegalArgumentException("Funcionário não encontrado na base de dados!");
            }

            if(selectedEmployee.isActive()){
                employeeService.disableById(selectedEmployee.getId());
            } else  {
                employeeService.enableById(selectedEmployee.getId());
            }

            view.clearEmployeeInfoFields();
            handleCompanySelectionChange(companyId);
            view.showSuccess("Ação realizada com sucesso!");

        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError("Erro ao salvar: " + e.getMessage());
        }
    }

    private void handlePreviousEntryButton() {
        EmployeeListItem selectedEmployee = view.getEmployeeList().getSelectedValue();

        if (selectedEmployee == null) {
            return;
        }

        int employeeId = selectedEmployee.getId();
        LocalDate date = LocalDate.parse(view.getDateField().getText(), DATE_FORMATTER).minusDays(1);
        view.getDateField().setText(date.format(DATE_FORMATTER));

        loadTimeEntry(employeeId, date);
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
            boolean active = view.getEmployeeList().getSelectedValue().isActive();

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
                    lunchBreakMinutes,
                    active
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
        view.addAutoAdvanceToField(view.getClockInField());
        view.addAutoAdvanceToField(view.getLunchInField());
        view.addAutoAdvanceToField(view.getLunchOutField());
        view.addAutoAdvanceToField(view.getLunchInField());
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
        loadTimeEntry(employeeId, LocalDate.parse(view.getDateField().getText(), DATE_FORMATTER));
    }

    private void loadTimeEntry(int timeEntryId){
        view.clearTimeEntryFields();
        timeEntryService.findById(timeEntryId).ifPresent(view::displayTimeEntry);
    }

    private void loadTimeEntry(int employeeId, LocalDate date) {
        view.clearTimeEntryFields();
        timeEntryService.findByEmployeeIdAndDate(employeeId, date)
                .ifPresent(view::displayTimeEntry);
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
            if (!view.showConfirmationDialog(
                    "O funcionário e todos os seus registros de ponto " +
                    "serão excluídos da base de dados. O recomendado é " +
                    "desabilitar o funcionário. Deseja prosseguir com a " +
                    "exclusão mesmo assim?")) {
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
            view.resetDateField();
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
        List<Employee> employees;
        if (view.getShowAllEmployees().isSelected()) {
            employees = employeeService.findByCompanyId(companyId);
        } else {
            employees = employeeService.findActivesByCompanyId(companyId);
        }

        employees.sort(
                Comparator.comparing(Employee::isActive, Comparator.reverseOrder())
                        .thenComparing(Employee::getName)
        );
        view.updateEmployeeList(employees);
        view.clearEmployeeInfoFields();
        view.resetDateField();
    }

}
