package com.calchoras.repository;

import com.calchoras.model.Company;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyRepository {
    private final String filePath;
    private final Gson gson;

    private List<Company> companiesList;

    public CompanyRepository() {
        this("empresas.json");
    }

    public CompanyRepository(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.companiesList = new ArrayList<>();
        loadCompanies();
    }

    public void loadCompanies() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            this.companiesList = new ArrayList<>();
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

    public void save() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(companiesList, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar empresas.json");
            e.printStackTrace();
        }
    }

    public List<Company> getAll() {
        return new ArrayList<>(companiesList);
    }

    public Optional<Company> getCompany(int id) {
        return companiesList.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }


    public void add(Company company) {
        companiesList.add(company);
        save();
    }

    public void update(Company company) {
        for (int i = 0; i < companiesList.size(); i++) {
            if (companiesList.get(i).getId() == company.getId()) {
                companiesList.set(i, company);
                save();
                return;
            }
        }
    }

    public void remove(int id) {
        companiesList.removeIf(c -> c.getId() == id);
        save();
    }

    public boolean exists(String name) {
        return companiesList.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }
}
