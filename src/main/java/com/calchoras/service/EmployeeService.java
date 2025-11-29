package com.calchoras.service;

import com.calchoras.model.Employee;
import com.calchoras.repository.EmployeeRepository;
import com.calchoras.service.interfaces.ICompanyService;
import com.calchoras.service.interfaces.IEmployeeService;
import com.calchoras.util.LocalDateAdapter;
import com.calchoras.util.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeService implements IEmployeeService {

    private List<Employee> employeeList;
    private EmployeeRepository employeeRepository;
    private final ICompanyService companyService;

    public EmployeeService(EmployeeRepository employeeRepository, ICompanyService companyService) {
        this.employeeRepository = employeeRepository;
        this.employeeList = employeeRepository.getAll();
        this.companyService = companyService;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(int employeeId) {
        return employeeRepository.getEmployee(employeeId);
    }

    @Override
    public List<Employee> getEmployeesByCompany(int companyId) {
        return employeeRepository.getEmployeesByCompany(companyId);
    }

    @Override
    public Employee addEmployee(Employee employee) {

        if (employeeRepository.exists(employee.getName())) {
            throw new IllegalArgumentException("Funcionário já existe.");
        }

        if (!companyService.exists(employee.getCompanyId())) {
            throw new IllegalArgumentException("Empresa não encontrada.");
        }

        int nextId = employeeList.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;

        employee.setId(nextId);

        employeeRepository.add(employee);
        employeeList =  employeeRepository.getAll();

        return employee;
    }

    @Override
    public void updateEmployee(Employee updatedEmployee) {

        employeeRepository.update(updatedEmployee);
        employeeList = employeeRepository.getAll();
    }

    @Override
    public void deleteEmployee(int employeeId) {
        employeeRepository.remove(employeeId);
        employeeList =  employeeRepository.getAll();
    }

}
