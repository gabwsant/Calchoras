package com.calchoras.service;

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
        return repository.findByEmployeeId(employeeId);
    }

    @Override
    public Optional<TimeEntry> findByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return repository.findByEmployeeIdAndDate(employeeId, date);
    }

    @Override
    public TimeEntry save(TimeEntry entry) {

        if (!employeeService.existsById(entry.getEmployeeId())) {
            throw new IllegalArgumentException("Funcionário não encontrado.");
        }

        if (repository.findByEmployeeIdAndDate(entry.getEmployeeId(), entry.getEntryDate()).isPresent()) {
            throw new IllegalArgumentException("Já existe um lançamento para esse funcionário nessa data.");
        }

        int nextId = repository.findAll().stream()
                .mapToInt(TimeEntry::getId)
                .max()
                .orElse(0) + 1;

        entry.setId(nextId);
        repository.save(entry);
        return entry;
    }


    @Override
    public TimeEntry update(TimeEntry entry) {
        if (repository.findById(entry.getId()).isPresent()) {
            return repository.update(entry);
        } else {
            throw new IllegalArgumentException("Registro não encontrado para atualização.");
        }
    }

    @Override
    public boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return repository.deleteByEmployeeIdAndDate(employeeId, date);
    }
}
