package com.calchoras.service;

import com.calchoras.model.Employee;
import com.calchoras.repository.interfaces.IEmployeeRepository;
import com.calchoras.service.interfaces.ICompanyService;
import com.calchoras.service.interfaces.IEmployeeService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EmployeeService implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;
    private final ICompanyService companyService;

    public EmployeeService(IEmployeeRepository employeeRepository, ICompanyService companyService) {
        this.employeeRepository = employeeRepository;
        this.companyService = companyService;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(int employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @Override
    public List<Employee> findByCompanyId(int companyId) {
        return employeeRepository.findByCompanyId(companyId)
                .stream()
                .sorted(Comparator.comparing(Employee::isActive, Comparator.reverseOrder())
                        .thenComparing(Employee::getName))
                .toList();
    }

    @Override
    public List<Employee> findActivesByCompanyId(int companyId) {
        return employeeRepository.findActivesByCompanyId(companyId)
                .stream()
                .sorted(Comparator.comparing(Employee::isActive, Comparator.reverseOrder())
                        .thenComparing(Employee::getName))
                .toList();
    }

    @Override
    public Employee save(Employee employee) {
        Objects.requireNonNull(employee, "O funcionário não pode ser nulo.");
        Objects.requireNonNull(employee.getName(), "O nome do funcionário é obrigatório.");

        if (!companyService.existsById(employee.getCompanyId())) {
            throw new IllegalArgumentException("Empresa não encontrada.");
        }

        if (employeeRepository.existsByNameAndCompanyId(employee.getName(), employee.getCompanyId())) {
            throw new IllegalArgumentException("Já existe um funcionário com este nome para esta empresa.");
        }

        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        Objects.requireNonNull(employee, "O funcionário não pode ser nulo.");
        Objects.requireNonNull(employee.getName(), "O nome do funcionário é obrigatório.");

        if (!existsById(employee.getId())) {
            throw new IllegalArgumentException("Funcionário não encontrado na base de dados.");
        }

        if (!companyService.existsById(employee.getCompanyId())) {
            throw new IllegalArgumentException("Empresa inválida ou não encontrada.");
        }

        if (employeeRepository.existsByNameAndCompanyIdAndIdNot(employee.getName(), employee.getCompanyId(), employee.getId())) {
            throw new IllegalArgumentException("Já existe outro funcionário com este nome nesta empresa.");
        }

        return employeeRepository.update(employee);
    }

    @Override
    public boolean deleteById(int employeeId) {
        return employeeRepository.deleteById(employeeId);
    }

    @Override
    public boolean disableById(int employeeId) {
        return employeeRepository.disableById(employeeId);
    }

    @Override
    public boolean enableById(int employeeId) {
        return employeeRepository.enableById(employeeId);
    }

    @Override
    public boolean existsById(int employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    @Override
    public boolean existsByName(String name) {
        return employeeRepository.existsByName(name);
    }
}