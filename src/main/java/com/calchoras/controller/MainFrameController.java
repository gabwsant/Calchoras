package com.calchoras.controller;

import com.calchoras.dto.CompanyDTO;
import com.calchoras.dto.EmployeeDTO;
import com.calchoras.dto.TimeEntryDTO;
import com.calchoras.mapper.CompanyMapper;
import com.calchoras.mapper.EmployeeMapper;
import com.calchoras.mapper.TimeEntryMapper;
import com.calchoras.model.Company;
import com.calchoras.model.Employee;
import com.calchoras.model.PeriodCalculationResult;
import com.calchoras.model.TimeEntry;
import com.calchoras.service.ReportService;
import com.calchoras.service.TimeEntryService;
import com.calchoras.service.interfaces.*;
import com.calchoras.util.validators.TimeFieldValidator;
import com.calchoras.view.*;
import org.springframework.format.datetime.standard.DurationFormatter;

import javax.swing.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
        view.getAddCompanyButton().addActionListener(e -> openCompanyDialog(null));
        view.getUpdateCompanyButton().addActionListener(e -> {
            Object selected = view.getCompanyComboBox().getSelectedItem();

            if (selected instanceof CompanyDTO selectedCompany) {
                openCompanyDialog(selectedCompany);
            } else {
                view.showError("Selecione uma empresa válida para editar.");
            }
        });
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
                    view.enableComputationFields(true);
                    view.setTimeEntryEditModeEnabled(true);
                    handleEmployeeSelection(item.getId());
                } else {
                    view.clearEmployeeInfoFields();
                    view.clearTimeEntryFields();
                    view.enableEmployeeFields(false);
                    view.enableComputationFields(false);
                    view.setTimeEntryEditModeEnabled(false);
                }
            }
        });

        // Time Entry
        view.getAddTimeEntryButton().addActionListener(e -> handleAddTimeEntryAction());
        view.getRemoveTimeEntryButton().addActionListener(e -> handleRemoveTimeEntryAction());
        view.getPreviousEntryButton().addActionListener(e -> handlePreviousEntryButton());
        view.getNextEntryButton().addActionListener(e -> handleNextEntryAction());
        view.getIsDayOffCheckBox().addActionListener(e -> handleIsDayOffCheckBoxChange());

        // Date Handling
        view.getDateField().addActionListener(e -> handleDateChangeAction());

        // Computation
        view.getCalculateButton().addActionListener(e -> handleCalculateAction());

        // Mouse Listeners
        addHandleMouseRightClick();
    }

    private void handleRemoveTimeEntryAction() {
        if (!view.hasSelectedEmployee()) {
            view.showError("Selecione um funcionário antes de remover um ponto.");
        }

        try {
            TimeEntry timeEntryToDelete = buildTimeEntryFromView();

            boolean exists = timeEntryService.existsByEmployeeIdAndDate(
                    timeEntryToDelete.getEmployeeId(),
                    timeEntryToDelete.getEntryDate()
            );

            if (!exists) {
                view.showError("Não existe um ponto registrado para a data: " + timeEntryToDelete.getEntryDate().format(DATE_FORMATTER));
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Tem certeza que deseja remover a batida de " + timeEntryToDelete.getEntryDate().format(DATE_FORMATTER) + "?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            boolean isDeleted = timeEntryService.deleteByEmployeeIdAndDate(
                    timeEntryToDelete.getEmployeeId(),
                    timeEntryToDelete.getEntryDate()
            );

            if (isDeleted) {
                view.clearTimeEntryFields();
                view.getResultArea().append("\nBatida removida para " + timeEntryToDelete.getEntryDate().format(DATE_FORMATTER));
                handleIsDayOffCheckBoxChange();
                view.getClockInField().requestFocus();
            } else {
                view.showError("Erro ao deletar batida.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao deletar o ponto: " + e.getMessage(), "Erro Interno", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initAutoAdvance() {
        view.addAutoAdvanceToField(view.getClockInField());
        view.addAutoAdvanceToField(view.getLunchInField());
        view.addAutoAdvanceToField(view.getLunchOutField());
        view.addAutoAdvanceToField(view.getClockOutField());
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
            List<Company> companies = companyService.findAll();
            List<CompanyDTO> companyDTOs = CompanyMapper.toDTO(companies);

            view.updateCompanyList(companyDTOs);
            view.clearTimeEntryFields();
            view.resetDateField();
        } catch (Exception e) {
            view.showError("Erro ao carregar dados iniciais de empresas: " + e.getMessage());
        }
    }

    // ===================================================================================
    // COMPANY ACTIONS
    // ===================================================================================

    private void openCompanyDialog(CompanyDTO companyToEdit) {
        CompanyDialog dialog = new CompanyDialog(view, companyToEdit);

        dialog.getSaveButton().addActionListener(e -> {
            String name = dialog.getCompanyName();

            if (name.isBlank()) {
                view.showError("Nome da empresa é obrigatório!");
                return;
            }

            if (companyToEdit == null) {
                handleAddCompanyAction(name);
            } else {
                handleUpdateCompanyAction(companyToEdit.id(), name);
            }
            dialog.dispose();
        });

        if (companyToEdit != null) {
            dialog.getDeleteButton().addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        dialog,
                        "Tem certeza que deseja excluir a empresa '" + companyToEdit.name() + "'?\n" +
                                "Todos os funcionários da empresa e seus registros de ponto serão excluídos permanentemente.",
                        "Excluir Empresa",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    handleDeleteCompanyAction(companyToEdit.id());
                    dialog.dispose();
                }
            });
        }

        dialog.setVisible(true);
    }

    private void handleAddCompanyAction(String name) {
        if (name == null || name.isBlank()) {
            view.showError("O nome da empresa não pode estar vazio.");
            return;
        }

        try {
            Company companyToSave = new Company(0, name);
            Company savedCompany = companyService.save(companyToSave);

            CompanyDTO newCompanyDTO = CompanyMapper.toDTO(savedCompany);

            view.getCompanyComboBox().addItem(newCompanyDTO);
            view.getCompanyComboBox().setSelectedItem(newCompanyDTO);
            view.showSuccess("Empresa '" + newCompanyDTO.name() + "' cadastrada com sucesso!");

        } catch (IllegalArgumentException ex) {
            view.showError(ex.getMessage());
        } catch (Exception ex) {
            view.showError("Erro inesperado ao salvar a empresa: " + ex.getMessage());
        }
    }

    public void handleCompanySelectionChange(int companyId) {
        List<Employee> employees;
        if (view.getShowAllEmployees().isSelected()) {
            employees = employeeService.findByCompanyId(companyId);
        } else {
            employees = employeeService.findActivesByCompanyId(companyId);
        }

        List<EmployeeDTO> employeeDTOs = EmployeeMapper.toDTO(employees);
        view.updateEmployeeList(employeeDTOs);

        view.clearEmployeeInfoFields();
        view.resetDateField();
    }

    private void handleUpdateCompanyAction(int id, String newName) {
        try {
            Company company = new Company(id, newName);
            companyService.update(company);

            loadInitialData();
            view.showSuccess("Empresa atualizada com sucesso!");
        } catch (Exception ex) {
            view.showError("Erro ao atualizar empresa: " + ex.getMessage());
        }
    }

    private void handleDeleteCompanyAction(int id) {
        try {
            companyService.deleteById(id);

            loadInitialData();
            view.clearEmployeeInfoFields();
            view.clearTimeEntryFields();

            CompanyDTO currentCompany = (CompanyDTO) view.getCompanyComboBox().getSelectedItem();
            if (currentCompany != null) {
                handleCompanySelectionChange(currentCompany.id());
            } else {
                view.updateEmployeeList(java.util.Collections.emptyList());
            }
            view.showSuccess("Empresa excluída com sucesso!");
        } catch (Exception ex) {
            view.showError("Erro ao excluir empresa: " + ex.getMessage());
        }
    }

    // ===================================================================================
    // EMPLOYEE ACTIONS
    // ===================================================================================

    private void openEmployeeDialog() {
        EmployeeDialog dialog = new EmployeeDialog(view);

        List<Company> companies = companyService.findAll();
        dialog.updateCompanyList(CompanyMapper.toDTO(companies));
        dialog.setSelectedCompany(view.getSelectedCompanyId());
        dialog.addAutoAdvanceToField(dialog.getShiftIn());
        dialog.addAutoAdvanceToField(dialog.getShiftOut());

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

            Employee employee = new Employee(
                    0, companyId, name, shiftIn, shiftOut, lunchBreakMinutes, true
            );

            employeeService.save(employee);

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
            Employee employeeToUpdate = new Employee(
                    selectedEmployee.getId(),
                    view.getSelectedCompanyId(),
                    view.getNameField().getText(),
                    LocalTime.parse(view.getShiftInField().getText()),
                    LocalTime.parse(view.getShiftOutField().getText()),
                    Long.parseLong(view.getLunchBreakMinutesField().getText()),
                    view.isSelectedEmployeeActive()
            );

            employeeService.update(employeeToUpdate);

            view.clearEmployeeInfoFields();
            handleCompanySelectionChange(employeeToUpdate.getCompanyId());
            view.showSuccess("Funcionário atualizado com sucesso!");

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
        employeeService.findById(employeeId).ifPresent(employee -> {
            EmployeeDTO dto = EmployeeMapper.toDTO(employee);
            view.displayEmployeeInfo(dto);
        });

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
            return;
        }

        try {
            TimeEntry newEntry = buildTimeEntryFromView();

            if (!newEntry.isDayOff()
                    && newEntry.getClockIn() == null
                    && newEntry.getClockOut() == null) {
                throw new IllegalArgumentException(
                        "Selecione 'Dia de Folga' ou insira os horários de entrada e saída."
                );
            }

            LocalDate entryDate = LocalDate.parse(view.getDateField().getText(), DATE_FORMATTER);

            Optional<TimeEntry> existingEntryOpt =
                    timeEntryService.findByEmployeeIdAndDate(
                            view.getSelectedEmployeeId(), entryDate
                    );

            TimeEntry persistedEntry;

            if (existingEntryOpt.isPresent()) {
                TimeEntry existingEntry = existingEntryOpt.get();
                newEntry.setId(existingEntry.getId());

                persistedEntry = timeEntryService.update(newEntry);
            } else {
                persistedEntry = timeEntryService.save(newEntry);
            }

            view.getResultArea().append("\nBatida adicionada para " + persistedEntry.getEntryDate().format(DATE_FORMATTER));
            view.getClockInField().requestFocus();
            handleNextEntryAction();

        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    view,
                    "Erro de formato de Data (DD/MM/AAAA) ou Hora (HH:MM).",
                    "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError("Erro ao salvar o ponto: " + e.getMessage());
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
        boolean isDayOff = view.getIsDayOffCheckBox().isSelected();
        view.setClockingFieldsEnabled(!isDayOff);
    }

    private void loadTimeEntry(int employeeId, LocalDate date) {
        view.clearTimeEntryFields();
        timeEntryService.findByEmployeeIdAndDate(employeeId, date).ifPresent(entry -> {
            TimeEntryDTO dto = TimeEntryMapper.toDTO(entry);
            view.displayTimeEntry(dto);
        });
        handleIsDayOffCheckBoxChange();
    }

    private TimeEntry buildTimeEntryFromView() {
        return new TimeEntry(
                0,
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
    // CALCULATION ACTIONS
    // ===================================================================================

    private void handleCalculateAction() {
        if (!view.hasSelectedEmployee()) {
            view.showError("Selecione um funcionário antes de calcular.");
            return;
        }

        try {
            int employeeId = view.getEmployeeList().getSelectedValue().getId();

            Employee employeeToCalculate = employeeService.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

            LocalDate initialDate = LocalDate.parse(view.getCompInitialDateField().getText(), DATE_FORMATTER);
            LocalDate finalDate = LocalDate.parse(view.getCompFinalDateField().getText(), DATE_FORMATTER);

            List<TimeEntry> timeEntries = timeEntryService.findByEmployeeIdAndRange(employeeId, initialDate, finalDate);

            PeriodCalculationResult result = reportService.calculatePeriodBalance(employeeToCalculate, timeEntries);

            long overtime = result.totalOvertimeAccumulated().getSeconds();
            long negative = result.totalNegativeHoursAccumulated().getSeconds();
            long balance = result.finalBalance().getSeconds();

            view.getResultArea().append(
                    "\nTotal de horas positivas: " + String.format("%d:%02d", overtime / 3600, (overtime % 3600) / 60)
                    + "\nTotal de horas negativas: " + String.format("%d:%02d", negative / 3600, (negative % 3600) / 60)
                    + "\nBalanço final: " + String.format("%d:%02d", balance / 3600, Math.abs((balance % 3600) / 60))
            );

        } catch (Exception e) {
            view.showError("Erro ao calcular: " + e.getMessage());
        }
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