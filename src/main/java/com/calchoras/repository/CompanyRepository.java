package com.calchoras.repository;

import com.calchoras.model.Company;
import com.calchoras.repository.interfaces.ICompanyRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyRepository implements ICompanyRepository {

    private final String filePath;
    private final Gson gson;
    private List<Company> companiesList;

    public CompanyRepository() {
        this("empresas.json");
    }

    public CompanyRepository(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        load();
    }

    private void load() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            companiesList = new ArrayList<>();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Company>>() {}.getType();
            List<Company> loaded = gson.fromJson(reader, listType);
            companiesList = (loaded != null) ? loaded : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            companiesList = new ArrayList<>();
            e.printStackTrace();
        }
    }

    private void persist() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(companiesList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Company> findAll() {
        return new ArrayList<>(companiesList);
    }

    @Override
    public Optional<Company> findById(int id) {
        return companiesList.stream().filter(c -> c.getId() == id).findFirst();
    }

    @Override
    public Company save(Company company) {
        int nextId = companiesList.stream()
                .mapToInt(Company::getId)
                .max()
                .orElse(0) + 1;
        company.setId(nextId);

        companiesList.add(company);
        persist();
        return company;
    }

    @Override
    public Company update(Company company) {
        for (int i = 0; i < companiesList.size(); i++) {
            if (companiesList.get(i).getId() == company.getId()) {
                companiesList.set(i, company);
                persist();
                return company;
            }
        }
        throw new IllegalArgumentException("Empresa não encontrada para atualização: " + company.getId());
    }

    @Override
    public boolean deleteById(int id) {
        boolean removed = companiesList.removeIf(c -> c.getId() == id);
        if (removed) {
            persist();
        }
        return removed;
    }

    @Override
    public boolean existsById(int id) {
        return companiesList.stream().anyMatch(c -> c.getId() == id);
    }

    @Override
    public boolean existsByName(String name) {
        return companiesList.stream().anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }
}
