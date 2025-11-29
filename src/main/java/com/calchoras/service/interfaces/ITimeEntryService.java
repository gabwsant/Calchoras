package com.calchoras.service.interfaces;

import com.calchoras.model.TimeEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITimeEntryService {

    // Retorna todas as batidas de ponto de um funcionário
    List<TimeEntry> findByEmployeeId(int employeeId);

    // Retorna uma batida de ponto pelo ID
    Optional<TimeEntry> findById(int id);

    // Retorna uma batida de ponto de um funcionário em uma data específica
    Optional<TimeEntry> findByEmployeeIdAndDate(int employeeId, LocalDate date);

    // Adiciona uma batida de ponto e retorna o objeto criado com ID definido
    TimeEntry save(TimeEntry timeEntry);

    // Atualiza uma batida de ponto e retorna o objeto atualizado
    TimeEntry update(TimeEntry timeEntry);

    // Remove uma batida de ponto pelo ID do funcionário e data e retorna boolean indicando sucesso
    boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date);
}
