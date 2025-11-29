package com.calchoras.repository.interfaces;

import com.calchoras.model.TimeEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITimeEntryRepository {

    //Retorna todas as batidas de ponto
    List<TimeEntry> findAll();

    //Retorna uma batida de ponto pelo ID
    Optional<TimeEntry> findById(int id);

    // Retorna uma lista de batidas de ponto de um funcionário
    List<TimeEntry> findByEmployeeId(int employeeId);

    // Retorna uma batida de ponto de um funcionário em um determinado dia
    Optional<TimeEntry> findByEmployeeIdAndDate(int employeeId, LocalDate date);

    // Adiciona uma nova batida de ponto e retorna o objeto criado com ID definido
    TimeEntry save(TimeEntry entry);

    // Atualiza uma batida de ponto e retorna objeto atualizado
    TimeEntry update(TimeEntry entry);

    // Remove batida pelo ID e retorna boolean indicando sucesso
    boolean deleteById(int id);

    // Remove uma batida de ponto pelo ID do funcionário e data e retorna boolean indicando sucesso
    boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date);

    // Verifica existencia por ID do funcionário e data
    boolean existsByEmployeeIdAndDate(int employeeId, LocalDate date);
}
