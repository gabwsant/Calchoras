package com.calchoras.service;

import com.calchoras.dto.EmployeeDTO;
import com.calchoras.mapper.EmployeeMapper;
import com.calchoras.model.Employee;
import com.calchoras.repository.interfaces.IEmployeeRepository;
import com.calchoras.service.interfaces.ICompanyService;
import com.calchoras.service.interfaces.IEmployeeService;

import java.util.Comparator;
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
    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<EmployeeDTO> findById(int employeeId) {
        return employeeRepository.findById(employeeId)
                .map(EmployeeMapper::toDTO);
    }

    @Override
    public List<EmployeeDTO> findByCompanyId(int companyId) {
        return employeeRepository.findByCompanyId(companyId)
                .stream()
                .map(EmployeeMapper::toDTO)
                .sorted(
                        Comparator.comparing(EmployeeDTO::active, Comparator.reverseOrder())
                                .thenComparing(EmployeeDTO::name))
                .toList();
    }

    @Override
    public List<EmployeeDTO> findActivesByCompanyId(int companyId) {
        return employeeRepository.findActivesByCompanyId(companyId)
                .stream()
                .map(EmployeeMapper::toDTO)
                .sorted(
                        Comparator.comparing(EmployeeDTO::active, Comparator.reverseOrder())
                                .thenComparing(EmployeeDTO::name))
                .toList();
    }

    @Override
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        if (
            employeeRepository.existsByName(employeeDTO.name()) &&
            employeeRepository.existsByCompanyId(employeeDTO.companyId())
        ) {
            throw new IllegalArgumentException("Funcionário já existe para esta empresa.");
        }

        if (!companyService.existsById(employeeDTO.companyId())) {
            throw new IllegalArgumentException("Empresa não encontrada.");
        }

        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.toDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO update(EmployeeDTO employeeDTO) {

        if (!existsById(employeeDTO.id())) {
            throw new IllegalArgumentException("Funcionário não encontrado na base de dados!");
        }

        if (!companyService.existsById(employeeDTO.companyId())) {
            throw new IllegalArgumentException("Empresa inválida ou não encontrada!");
        }

        boolean existsDuplicate = employeeRepository.findByCompanyId(employeeDTO.companyId())
                .stream()
                .anyMatch(e -> e.getName().equalsIgnoreCase(employeeDTO.name()) &&
                        e.getId() != employeeDTO.id());

        if (existsDuplicate) {
            throw new IllegalArgumentException("Já existe outro funcionário com este nome nesta empresa.");
        }

        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        Employee updatedEmployee = employeeRepository.update(employee);

        return EmployeeMapper.toDTO(updatedEmployee);
    }

    @Override
    public boolean deleteById(int employeeId) {
        return employeeRepository.deleteById(employeeId);
    }

    @Override
    public boolean disableById(int employeeId) { return employeeRepository.disableById(employeeId); }

    @Override
    public boolean enableById(int employeeId) { return employeeRepository.enableById(employeeId); }

    @Override
    public boolean existsById(int employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    @Override
    public boolean existsByName(String name) {
        return employeeRepository.existsByName(name);
    }
}
