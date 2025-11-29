package com.calchoras.repository.interfaces;

import com.calchoras.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeRepository {

    // Retorna todos os funcionários
    List<Employee> findAll();

    // Retorna um funcionário pelo ID
    Optional<Employee> findById(int id);

    // Retorna todos os funcionários de uma empresa
    List<Employee> findByCompanyId(int companyId);

    // Adiciona um funcionário e retorna o objeto criado com ID definido
    Employee save(Employee employee);

    // Atualiza um funcionário e retorna o objeto atualizado
    Employee update(Employee employee);

    // Remove um funcionário pelo ID e retorna boolean indicando sucesso
    boolean deleteById(int id);

    // Verifica existência por ID
    boolean existsById(int id);

    // Verifica existência por nome
    boolean existsByName(String name);

    // Verifica existência por empresa
    boolean existsByCompanyId(int companyId);
}
