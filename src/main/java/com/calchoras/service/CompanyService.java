package com.calchoras.service;

import com.calchoras.model.Company;
import com.calchoras.repository.interfaces.ICompanyRepository;
import com.calchoras.service.interfaces.ICompanyService;

import java.util.List;
import java.util.Optional;

public class CompanyService implements ICompanyService {

    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> findById(int companyId) {
        return companyRepository.findById(companyId);
    }

    @Override
    public Company save(Company company) {
        if (companyRepository.existsByName(company.getName())) {
            throw new IllegalArgumentException("Empresa já existe.");
        }

        // Geração do próximo ID
        int nextId = companyRepository.findAll().stream()
                .mapToInt(Company::getId)
                .max()
                .orElse(0) + 1;
        company.setId(nextId);

        return companyRepository.save(company);
    }

    @Override
    public Company update(Company company) {
        if (!companyRepository.existsById(company.getId())) {
            throw new IllegalArgumentException("Empresa não encontrada para atualização.");
        }
        return companyRepository.update(company);
    }

    @Override
    public boolean deleteById(int companyId) {
        if (!companyRepository.existsById(companyId)) {
            return false;
        }
        return companyRepository.deleteById(companyId);
    }

    @Override
    public boolean existsById(int companyId) {
        return companyRepository.existsById(companyId);
    }

    @Override
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }
}
