package com.calchoras.repository;

import com.calchoras.model.Company;
import com.calchoras.model.Employee;
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
import java.util.stream.Collectors;

public class EmployeeRepository {
    private String filePath;
    private Gson gson;
    private List<Employee> employeesList;

    public EmployeeRepository(){
        this("funcionarios.json");
    }

    public EmployeeRepository(String filePath){
        this.filePath = filePath;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .setPrettyPrinting()
                .create();
        this.employeesList = new ArrayList<>();
        loadEmployees();
    }

    private void loadEmployees() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            this.employeesList = new ArrayList<>();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Employee>>() {}.getType();
            List<Employee> loaded = gson.fromJson(reader, listType);
            employeesList = (loaded != null) ? loaded : new ArrayList<>();

        } catch (IOException | JsonSyntaxException e) {
            employeesList = new ArrayList<>();
            e.printStackTrace();
        }
    }

    public void save() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(employeesList, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar funcionarios.json");
            e.printStackTrace();
        }
    }

    public List<Employee> getAll() {
        return new ArrayList<>(employeesList);
    }

    public Optional<Employee> getEmployee(int id) {
        return employeesList.stream().filter(e -> e.getId() == id).findFirst();
    }

    public List<Employee> getEmployeesByCompany(int companyId) {
        return employeesList.stream().filter(e -> e.getCompanyId() == companyId).collect(Collectors.toList());
    }

    public void add(Employee employee) {
        employeesList.add(employee);
        save();
    }

    public void update(Employee employee) {
        for (int i = 0; i < employeesList.size(); i++) {
            if (employeesList.get(i).getId() == employee.getId()) {
                employeesList.set(i, employee);
                save();
                return;
            }
        }
    }

    public void remove(int id) {
        employeesList.removeIf(c -> c.getId() == id);
        save();
    }

    public boolean exists(String name) {
        return employeesList.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }
}
