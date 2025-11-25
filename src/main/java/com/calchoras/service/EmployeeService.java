package com.calchoras.service;

import com.calchoras.model.Employee;
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

    private final String filePath;
    private List<Employee> employeeList;
    private final Gson gson;

    public EmployeeService() {
        this("funcionarios.json"); // Chama o outro construtor com o nome padrão
    }

    /**
     * Construtor para testes, permitindo especificar um arquivo diferente.
     * @param filePath O caminho para o arquivo JSON a ser usado.
     */
    public EmployeeService(String filePath) {
        this.filePath = filePath;
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());

        this.gson = gsonBuilder.setPrettyPrinting().create();
        loadEmployeesFromFile();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(this.employeeList);
    }

    @Override
    public Optional<Employee> getEmployeeById(int employeeId) {
        return employeeList.stream()
                .filter(e -> e.getId() == employeeId)
                .findFirst();
    }

    @Override
    public Employee addEmployee(Employee employee) {
        int nextId = employeeList.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;

        employee.setId(nextId);

        employeeList.add(employee);
        saveEmployeesToFile();
        return employee;
    }

    @Override
    public void updateEmployee(Employee updatedEmployee) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getId() == updatedEmployee.getId()) {
                employeeList.set(i, updatedEmployee);
                break;
            }
        }
        saveEmployeesToFile();
    }

    @Override
    public void deleteEmployee(int employeeId) {
        employeeList.removeIf(employee -> employee.getId() == employeeId);
        saveEmployeesToFile();
    }

    private void loadEmployeesFromFile() {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            this.employeeList = new ArrayList<>();
            System.out.println("Arquivo de funcionários não encontrado ou vazio. Iniciando com lista nova.");
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Employee>>() {}.getType();
            this.employeeList = gson.fromJson(reader, listType);

            if (this.employeeList == null) {
                this.employeeList = new ArrayList<>();
            }
        } catch (IOException | JsonSyntaxException e) {
            this.employeeList = new ArrayList<>();
            System.err.println("ERRO: Falha ao ler ou interpretar o arquivo de funcionários. Iniciando com lista vazia para segurança.");
            e.printStackTrace();
        }
    }

    private void saveEmployeesToFile() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(this.employeeList, writer);
        } catch (IOException e) {
            System.err.println("ERRO: Falha ao salvar o arquivo de funcionários.");
            e.printStackTrace();
        }
    }
}