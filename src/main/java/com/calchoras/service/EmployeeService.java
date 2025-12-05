package com.calchoras.service;

import com.calchoras.model.Employee;
import com.calchoras.repository.interfaces.IEmployeeRepository;
import com.calchoras.service.interfaces.ICompanyService;
import com.calchoras.service.interfaces.IEmployeeService;

import java.util.List;
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
        return employeeRepository.findByCompanyId(companyId);
    }

    @Override
    public Employee save(Employee employee) {
        if (employeeRepository.existsByName(employee.getName()) &&
                employeeRepository.existsByCompanyId(employee.getCompanyId())) {
            throw new IllegalArgumentException("Funcionário já existe para esta empresa.");
        }

        if (!companyService.existsById(employee.getCompanyId())) {
            throw new IllegalArgumentException("Empresa não encontrada.");
        }

        int nextId = employeeRepository.findAll().stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
        employee.setId(nextId);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        boolean existsDuplicate = employeeRepository.findByCompanyId(employee.getCompanyId()).stream()
                .anyMatch(e -> e.getName().equalsIgnoreCase(employee.getName()) && e.getId() != employee.getId());

        if (existsDuplicate) {
            throw new IllegalArgumentException("Outro funcionário com esse nome já existe para a empresa.");
        }
        return employeeRepository.update(employee);
    }

    @Override
    public boolean deleteById(int employeeId) {
        return employeeRepository.deleteById(employeeId);
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
