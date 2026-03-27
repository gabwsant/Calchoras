package com.calchoras.service;

import com.calchoras.dto.TimeEntryDTO;
import com.calchoras.mapper.TimeEntryMapper;
import com.calchoras.model.TimeEntry;
import com.calchoras.repository.interfaces.ITimeEntryRepository;
import com.calchoras.service.interfaces.IEmployeeService;
import com.calchoras.service.interfaces.ITimeEntryService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TimeEntryService implements ITimeEntryService {

    private final ITimeEntryRepository repository;
    private final IEmployeeService employeeService;

    public TimeEntryService(ITimeEntryRepository repository, IEmployeeService employeeService) {
        this.repository = repository;
        this.employeeService = employeeService;
    }

    @Override
    public Optional<TimeEntry> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<TimeEntry> findByEmployeeId(int employeeId) {
        return repository.findByEmployeeId(employeeId)
                .stream()
                .toList();
    }

    @Override
    public Optional<TimeEntry> findByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return repository.findByEmployeeIdAndDate(employeeId, date);
    }

    @Override
    public List<TimeEntry> findByEmployeeIdAndRange(int employeeId, LocalDate dateFrom, LocalDate dateTo) {
        return repository.findByEmployeeIdAndRange(employeeId, dateFrom, dateTo)
                .stream()
                .toList();
    }

    @Override
    public TimeEntry save(TimeEntry entry) {
        if (!employeeService.existsById(entry.getEmployeeId())) {
            throw new IllegalArgumentException("Funcionário não encontrado.");
        }
        if (repository.findByEmployeeIdAndDate(entry.getEmployeeId(), entry.getEntryDate()).isPresent()) {
            throw new IllegalArgumentException("Já existe um lançamento para esse funcionário nessa data.");
        }
        return repository.save(entry);
    }


    @Override
    public TimeEntry update(TimeEntry entry) {
        if (!repository.existsById(entry.getId())) {
            throw new IllegalArgumentException("Registro de ponto com ID " + entry.getId() + " não encontrado.");
        }
        return repository.update(entry);
    }

    @Override
    public boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return repository.deleteByEmployeeIdAndDate(employeeId, date);
    }

    public boolean existsByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return repository.existsByEmployeeIdAndDate(employeeId, date);
    }
}
