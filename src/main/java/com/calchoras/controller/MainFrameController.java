package com.calchoras.controller;

import com.calchoras.dto.CompanyDTO;
import com.calchoras.dto.EmployeeDTO;
import com.calchoras.dto.TimeEntryDTO;
import com.calchoras.service.interfaces.*;
import com.calchoras.util.validators.TimeFieldValidator;
import com.calchoras.view.*;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrameController {

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final MainFrame view;

    private final ICompanyService companyService;
    private final IEmployeeService employeeService;
    private final ITimeEntryService timeEntryService;
    private final IDailyCalculationService dailyCalculationService;
    private final IReportService reportService;


    public MainFrameController(
            MainFrame view,
            ICompanyService companyService,
            IEmployeeService employeeService,
            ITimeEntryService timeEntryService,
            IDailyCalculationService dailyCalculationService,
            IReportService reportService
    ) {
        this.view = view;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.timeEntryService = timeEntryService;
        this.dailyCalculationService = dailyCalculationService;
        this.reportService = reportService;

        initValidators();
        initAutoAdvance();
        initListeners();
        loadInitialData();
    }

    // ===================================================================================
    // INITIALIZATION METHODS
    // ===================================================================================

    private void initListeners() {
        // Company
        view.getAddCompanyButton().addActionListener(e -> openCompanyDialog());
        view.getCompanyComboBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                CompanyDTO item = (CompanyDTO) e.getItem();
                if (item != null) {
                    handleCompanySelectionChange(item.id());
                }
            }
        });

        // Employee
        view.getAddEmployeeButton().addActionListener(e -> openEmployeeDialog());
        view.getUpdateEmployeeButton().addActionListener(e -> handleUpdateEmployeeAction());
        view.getEnableEmployeeButton().addActionListener(e -> handleEnableEmployeeAction());
        view.getRemoveEmployeeButton().addActionListener(e -> handleRemoveEmployeeAction());
        view.getShowAllEmployees().addActionListener(e -> handleShowAllEmployeesChange());
        view.getToggleStatusItem().addActionListener(e -> handleEnableEmployeeAction());
        view.getRemoveEmployeeItem().addActionListener(e -> handleRemoveEmployeeAction());

        view.getEmployeeList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                EmployeeListItem item = view.getEmployeeList().getSelectedValue();
                if (item != null) {
                    view.enableEmployeeFields(true);
                    view.setTimeEntryEditModeEnabled(true);
                    handleEmployeeSelection(item.getId());
                } else {
                    view.clearEmployeeInfoFields();
                    view.clearTimeEntryFields();
                    view.enableEmployeeFields(false);
                    view.setTimeEntryEditModeEnabled(false);
                }
            }
        });

        // Time Entry
        view.getAddTimeEntryButton().addActionListener(e -> handleAddTimeEntryAction());
        view.getPreviousEntryButton().addActionListener(e -> handlePreviousEntryButton());
        view.getNextEntryButton().addActionListener(e -> handleNextEntryAction());
        view.getIsDayOffCheckBox().addActionListener(e -> handleIsDayOffCheckBoxChange());

        // Date Handling
        view.getDateField().addActionListener(e -> handleDateChangeAction());
        view.getDateField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                handleDateChangeAction();
            }
        });

        // Mouse Listeners
        addHandleMouseRightClick();
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

    private void loadInitialData() {
        try {
            List<CompanyDTO> companies = companyService.findAll();
            view.updateCompanyList(companies);
            view.clearTimeEntryFields();
            view.resetDateField();
        } catch (Exception e) {
            view.showError("Erro ao carregar dados iniciais de empresas: " + e.getMessage());
        }
    }

    // ===================================================================================
    // COMPANY ACTIONS
    // ===================================================================================

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

    private void handleAddCompanyAction(String name) {
        if (name == null || name.isBlank()) {
            view.showError("O nome da empresa não pode estar vazio.");
            return;
        }

        try {
            CompanyDTO companyToSave = new CompanyDTO(0, name, 0);
            CompanyDTO newCompany = companyService.save(companyToSave);

            view.getCompanyComboBox().addItem(newCompany);
            view.getCompanyComboBox().setSelectedItem(newCompany);
            view.showSuccess("Empresa '" + newCompany.name() + "' cadastrada com sucesso!");

        } catch (IllegalArgumentException ex) {
            view.showError(ex.getMessage());
        } catch (Exception ex) {
            view.showError("Erro inesperado ao salvar a empresa: " + ex.getMessage());
        }
    }

    public void handleCompanySelectionChange(int companyId) {
        List<EmployeeDTO> employees;
        if (view.getShowAllEmployees().isSelected()) {
            employees = employeeService.findByCompanyId(companyId);
        } else {
            employees = employeeService.findActivesByCompanyId(companyId);
        }

        view.updateEmployeeList(employees);
        view.clearEmployeeInfoFields();
        view.resetDateField();
    }

    // ===================================================================================
    // EMPLOYEE ACTIONS
    // ===================================================================================

    private void openEmployeeDialog() {
        EmployeeDialog dialog = new EmployeeDialog(view);

        List<CompanyDTO> companies = companyService.findAll();
        dialog.updateCompanyList(companies);

        TimeFieldValidator.apply(dialog.getShiftIn());
        TimeFieldValidator.apply(dialog.getShiftOut());

        dialog.getSaveButton().addActionListener(e -> {
            try {
                handleAddEmployeeAction(
                        dialog.getCompanyId(),
                        dialog.getName(),
                        dialog.getShiftInStr(),
                        dialog.getShiftOutStr(),
                        dialog.getLunchBreak()
                );
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                view.showError(ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    private void handleAddEmployeeAction(
            int companyId,
            String name,
            String shiftInStr,
            String shiftOutStr,
            String lunchBreakMinutesStr
    ) {
        try {
            if (name.isBlank() || shiftInStr.isBlank() || shiftOutStr.isBlank() || lunchBreakMinutesStr.isBlank()) {
                throw new IllegalArgumentException("Todos os campos são obrigatórios.");
            }

            LocalTime shiftIn = LocalTime.parse(shiftInStr);
            LocalTime shiftOut = LocalTime.parse(shiftOutStr);
            long lunchBreakMinutes = Long.parseLong(lunchBreakMinutesStr);

            EmployeeDTO employeeDto = new EmployeeDTO(
                    0,
                    companyId,
                    name,
                    shiftIn,
                    shiftOut,
                    lunchBreakMinutes,
                    true // new employee starts active
            );

            employeeService.save(employeeDto);

            view.clearEmployeeInfoFields();
            handleCompanySelectionChange(companyId);
            view.showSuccess("Funcionário salvo com sucesso!");

        } catch (java.time.format.DateTimeParseException | NumberFormatException e) {
            view.showError("Formato inválido: Hora deve ser HH:mm e minutos devem ser numéricos.");
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError("Erro inesperado: " + e.getMessage());
        }
    }

    private void handleUpdateEmployeeAction() {
        EmployeeListItem selectedEmployee = view.getEmployeeList().getSelectedValue();

        if (selectedEmployee == null) {
            view.showError("Selecione um funcionário para atualizar!");
            return;
        }

        try {
            EmployeeDTO employeeToUpdate = buildEmployeeDTOFromView(selectedEmployee.getId());

            employeeService.update(employeeToUpdate);

            view.clearEmployeeInfoFields();
            handleCompanySelectionChange(employeeToUpdate.companyId());
            view.showSuccess("Funcionário salvo com sucesso!");

        } catch (java.time.format.DateTimeParseException | NumberFormatException e) {
            view.showError("Erro no formato de Hora (HH:mm) ou Minutos (numérico).");
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError("Erro ao salvar: " + e.getMessage());
        }
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

            if (selectedEmployee.isActive()) {
                employeeService.disableById(selectedEmployee.getId());
            } else {
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

    private void handleRemoveEmployeeAction() {
        try {
            if (!view.showConfirmationDialog(
                    "O funcionário e todos os seus registros de ponto " +
                            "serão excluídos da base de dados. O recomendado é " +
                            "desabilitar o funcionário. Deseja prosseguir com a " +
                            "exclusão mesmo assim?")) {
                return;
            }

            int employeeId = view.getSelectedEmployeeId();
            int companyId = view.getSelectedCompanyId();

            boolean deleted = employeeService.deleteById(employeeId);

            if (deleted) {
                view.showSuccess("Funcionário deletado com sucesso!");
            } else {
                view.showError("Nenhum funcionário foi deletado!");
            }

            view.clearEmployeeInfoFields();
            handleCompanySelectionChange(companyId);
        } catch (Exception e) {
            view.showError("Erro ao deletar: " + e.getMessage());
        }
    }

    private void handleShowAllEmployeesChange() {
        int companyId = view.getSelectedCompanyId();
        handleCompanySelectionChange(companyId);
    }

    private void handleEmployeeSelection(int employeeId) {
        employeeService.findById(employeeId)
                .ifPresent(view::displayEmployeeInfo);
        loadTimeEntry(employeeId, LocalDate.parse(view.getDateField().getText(), DATE_FORMATTER));
    }

    private EmployeeDTO buildEmployeeDTOFromView(int employeeId) {
        return new EmployeeDTO(
                employeeId,
                view.getSelectedCompanyId(),
                view.getNameField().getText(),
                LocalTime.parse(view.getShiftInField().getText()),
                LocalTime.parse(view.getShiftOutField().getText()),
                Integer.parseInt(view.getLunchBreakMinutesField().getText()),
                view.isSelectedEmployeeActive()
        );
    }

    // ===================================================================================
    // TIME ENTRY ACTIONS
    // ===================================================================================

    private void handleAddTimeEntryAction() {
        if (!view.hasSelectedEmployee()) {
            view.showError("Selecione um funcionário antes de adicionar um ponto.");
        }

        try {
            TimeEntryDTO timeEntryToInsertOrUpdate = buildTimeEntryDTOFromView();

            if (
                    !timeEntryToInsertOrUpdate.dayOff() &&
                            timeEntryToInsertOrUpdate.clockIn() == null &&
                            timeEntryToInsertOrUpdate.clockOut() == null
            ) {
                throw new IllegalArgumentException("Selecione 'Dia de Folga' ou insira os horários de entrada e saída.");
            }

            TimeEntryDTO timeEntry = timeEntryService.saveOrUpdate(timeEntryToInsertOrUpdate);

            view.plusEntryDate(1);
            view.clearTimeEntryFields();
            view.getResultArea().append("\nBatida adicionada para " + timeEntry.entryDate());

        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Erro de formato de Data (DD/MM/AAAA) ou Hora (HH:MM).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar o ponto: " + e.getMessage(), "Erro Interno", JOptionPane.ERROR_MESSAGE);
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

    private void handleIsDayOffCheckBoxChange() {
        view.setClockingFieldsEnabled(!view.getIsDayOffCheckBox().isSelected());
    }

    private void loadTimeEntry(int employeeId, LocalDate date) {
        view.clearTimeEntryFields();
        timeEntryService.findByEmployeeIdAndDate(employeeId, date)
                .ifPresent(view::displayTimeEntry);
    }

    private TimeEntryDTO buildTimeEntryDTOFromView() {
        return new TimeEntryDTO(
                0, // it's defined by the repo
                view.getSelectedEmployeeId(),
                view.getEntryDate(),
                view.getClockIn(),
                view.getLunchIn(),
                view.getLunchOut(),
                view.getClockOut(),
                view.isDayOffSelected()
        );
    }

    // ===================================================================================
    // UI HELPERS
    // ===================================================================================

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
}