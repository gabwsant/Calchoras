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
    public Optional<TimeEntryDTO> findById(int id) {
        return repository.findById(id)
                .map(TimeEntryMapper::toDTO);
    }

    @Override
    public List<TimeEntryDTO> findByEmployeeId(int employeeId) {
        return repository.findByEmployeeId(employeeId)
                .stream()
                .map(TimeEntryMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<TimeEntryDTO> findByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return repository.findByEmployeeIdAndDate(employeeId, date)
                .map(TimeEntryMapper::toDTO);
    }

    @Override
    public TimeEntryDTO save(TimeEntryDTO entryDTO) {

        if (!employeeService.existsById(entryDTO.employeeId())) {
            throw new IllegalArgumentException("Funcionário não encontrado.");
        }

        if (repository.findByEmployeeIdAndDate(entryDTO.employeeId(), entryDTO.entryDate()).isPresent()) {
            throw new IllegalArgumentException("Já existe um lançamento para esse funcionário nessa data.");
        }

        TimeEntry timeEntry = TimeEntryMapper.toEntity(entryDTO);
        TimeEntry savedTimeEntry = repository.save(timeEntry);

        return TimeEntryMapper.toDTO(savedTimeEntry);
    }


    @Override
    public TimeEntryDTO update(TimeEntryDTO entryDTO) {
        if (!repository.existsById(entryDTO.id())) {
            throw new IllegalArgumentException("Registro de ponto com ID " + entryDTO.id() + " não encontrado.");
        }

        TimeEntry entity = TimeEntryMapper.toEntity(entryDTO);
        TimeEntry updatedEntity = repository.update(entity);

        return TimeEntryMapper.toDTO(updatedEntity);
    }

    public TimeEntryDTO saveOrUpdate(TimeEntryDTO dto) {
        Optional<TimeEntry> existing = repository.findByEmployeeIdAndDate(dto.employeeId(), dto.entryDate());

        TimeEntry entity = TimeEntryMapper.toEntity(dto);

        if (existing.isPresent()) {
            entity.setId(existing.get().getId());
            repository.update(entity);
        } else {
            repository.save(entity);
        }
        return TimeEntryMapper.toDTO(entity);
    }

    @Override
    public boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return repository.deleteByEmployeeIdAndDate(employeeId, date);
    }

    public boolean existsByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return repository.existsByEmployeeIdAndDate(employeeId, date);
    }
}
