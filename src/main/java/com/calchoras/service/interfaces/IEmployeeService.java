package com.calchoras.service.interfaces;

import com.calchoras.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {

    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(int employeeId);
    Employee addEmployee(Employee employee);
    void updateEmployee(Employee employee);
    void deleteEmployee(int employeeId);
}