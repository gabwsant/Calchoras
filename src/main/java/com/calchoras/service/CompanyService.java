package com.calchoras.service;

import com.calchoras.model.Company;
import com.calchoras.service.interfaces.ICompanyService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyService implements ICompanyService {

    private final String filePath;
    private final Gson gson;
    private List<Company> companiesList;

    public CompanyService() {
        this("companies.json");
    }

    public CompanyService(String filePath) {
        this.filePath = filePath;

        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.setPrettyPrinting().create();

        loadCompaniesFromFile();
    }

    @Override
    public List<Company> getAllCompanies() {
        return new ArrayList<>(this.companiesList);
    }

    @Override
    public Optional<Company> getCompanyById(int companyId) {
        return companiesList.stream()
                .filter(c -> c.getId() == companyId)
                .findFirst();
    }

    @Override
    public Company addCompany(Company company) {
        int nextId = companiesList.stream()
                .mapToInt(Company::getId)
                .max()
                .orElse(0) + 1;

        company.setId(nextId);

        companiesList.add(company);
        saveCompaniesToFile();
        return company;
    }

    @Override
    public void updateCompany(Company updatedCompany) {
        for (int i = 0; i < companiesList.size(); i++) {
            if (companiesList.get(i).getId() == updatedCompany.getId()) {
                companiesList.set(i, updatedCompany);
                saveCompaniesToFile();
                return;
            }
        }
    }

    @Override
    public void deleteCompany(int companyId) {
        companiesList.removeIf(company -> company.getId() == companyId);
        saveCompaniesToFile();
    }

    private void loadCompaniesFromFile() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            this.companiesList = new ArrayList<>();
            System.out.println("Arquivo de empresas não encontrado ou vazio. Iniciando com lista nova.");
            return;
        }

        try (Reader reader = new FileReader(file)) {

            // CORREÇÃO: TypeToken correto para Company, não Employee
            Type listType = new TypeToken<ArrayList<Company>>() {}.getType();
            this.companiesList = gson.fromJson(reader, listType);

            if (this.companiesList == null) {
                this.companiesList = new ArrayList<>();
            }

        } catch (IOException | JsonSyntaxException e) {
            this.companiesList = new ArrayList<>();
            System.err.println("ERRO: Falha ao ler ou interpretar o arquivo de empresas. Iniciando com lista vazia para segurança.");
            e.printStackTrace();
        }
    }

    private void saveCompaniesToFile() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(this.companiesList, writer);
        } catch (IOException e) {
            System.err.println("ERRO: Falha ao salvar o arquivo de empresas.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(int companyId) {
        return companiesList.stream()
                .anyMatch(c -> c.getId() == companyId);
    }
}
