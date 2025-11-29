package com.calchoras.service;

import com.calchoras.model.Company;
import com.calchoras.repository.CompanyRepository;
import com.calchoras.service.interfaces.ICompanyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyService implements ICompanyService {

    private List<Company> companiesList;
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        this.companiesList = companyRepository.getAll();
    }

    @Override
    public List<Company> getAllCompanies() {
        return new ArrayList<>(companiesList);
    }

    @Override
    public Optional<Company> getCompanyById(int companyId) {
        return companiesList.stream()
                .filter(c -> c.getId() == companyId)
                .findFirst();
    }

    @Override
    public Company addCompany(Company company) {

        if (companyRepository.exists(company.getName())) {
            throw new IllegalArgumentException("Empresa já existe.");
        }

        int nextId = companiesList.stream()
                .mapToInt(Company::getId)
                .max()
                .orElse(0) + 1;

        company.setId(nextId);

        companyRepository.add(company);
        companiesList = companyRepository.getAll();

        return company;
    }

    @Override
    public void updateCompany(Company updatedCompany) {
        companyRepository.update(updatedCompany);
        companiesList = companyRepository.getAll();
    }

    @Override
    public void deleteCompany(int companyId) {
        companyRepository.remove(companyId);
        companiesList = companyRepository.getAll();
    }

    @Override
    public boolean exists(int companyId) {
        return companiesList.stream()
                .anyMatch(c -> c.getId() == companyId);
    }
}
