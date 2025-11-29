package com.calchoras.repository;

import com.calchoras.model.Employee;
import com.calchoras.repository.interfaces.IEmployeeRepository;
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
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeRepository implements IEmployeeRepository {

    private final String filePath;
    private final Gson gson;
    private List<Employee> employeesList;

    public EmployeeRepository() {
        this("funcionarios.json");
    }

    public EmployeeRepository(String filePath) {
        this.filePath = filePath;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .setPrettyPrinting()
                .create();
        load();
    }

    private void load() {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            employeesList = new ArrayList<>();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<ArrayList<Employee>>() {}.getType();
            List<Employee> loaded = gson.fromJson(reader, type);
            employeesList = (loaded != null) ? loaded : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            employeesList = new ArrayList<>();
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(employeesList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employeesList);
    }

    @Override
    public Optional<Employee> findById(int id) {
        return employeesList.stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public List<Employee> findByCompanyId(int companyId) {
        return employeesList.stream()
                .filter(e -> e.getCompanyId() == companyId)
                .collect(Collectors.toList());
    }

    @Override
    public Employee save(Employee employee) {
        int nextId = employeesList.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
        employee.setId(nextId);
        employeesList.add(employee);
        saveToFile();
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        for (int i = 0; i < employeesList.size(); i++) {
            if (employeesList.get(i).getId() == employee.getId()) {
                employeesList.set(i, employee);
                saveToFile();
                return employee;
            }
        }
        throw new NoSuchElementException("Funcionário não encontrado para atualizar.");
    }

    @Override
    public boolean deleteById(int id) {
        boolean removed = employeesList.removeIf(e -> e.getId() == id);
        if (removed) saveToFile();
        return removed;
    }

    @Override
    public boolean existsById(int id) {
        return employeesList.stream().anyMatch(e -> e.getId() == id);
    }

    @Override
    public boolean existsByName(String name) {
        return employeesList.stream().anyMatch(e -> e.getName().equalsIgnoreCase(name));
    }

    @Override
    public boolean existsByCompanyId(int companyId) {
        return employeesList.stream().anyMatch(e -> e.getCompanyId() == companyId);
    }
}
