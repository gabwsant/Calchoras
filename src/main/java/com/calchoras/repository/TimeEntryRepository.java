package com.calchoras.repository;

import com.calchoras.model.TimeEntry;
import com.calchoras.repository.interfaces.ITimeEntryRepository;
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

public class TimeEntryRepository implements ITimeEntryRepository {

    private final String filePath;
    private final Gson gson;
    private List<TimeEntry> timeEntryList;

    public TimeEntryRepository() {
        this("batidas.json");
    }

    public TimeEntryRepository(String filePath) {
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
            timeEntryList = new ArrayList<>();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<ArrayList<TimeEntry>>() {}.getType();
            List<TimeEntry> loaded = gson.fromJson(reader, type);
            timeEntryList = (loaded != null) ? loaded : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            timeEntryList = new ArrayList<>();
            e.printStackTrace();
        }
    }

    private void persist() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(timeEntryList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TimeEntry> findAll() {
        return new ArrayList<>(timeEntryList);
    }

    @Override
    public Optional<TimeEntry> findById(int id) {
        return timeEntryList.stream().filter(te -> te.getId() == id).findFirst();
    }

    @Override
    public List<TimeEntry> findByEmployeeId(int employeeId) {
        return timeEntryList.stream()
                .filter(te -> te.getEmployeeId() == employeeId)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TimeEntry> findByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return timeEntryList.stream()
                .filter(te -> te.getEmployeeId() == employeeId && date.equals(te.getEntryDate()))
                .findFirst();
    }

    @Override
    public TimeEntry save(TimeEntry entry) {
        int nextId = timeEntryList.stream()
                .mapToInt(TimeEntry::getId)
                .max()
                .orElse(0) + 1;
        entry.setId(nextId);
        timeEntryList.add(entry);
        persist();
        return entry;
    }

    @Override
    public TimeEntry update(TimeEntry entry) {
        for (int i = 0; i < timeEntryList.size(); i++) {
            if (timeEntryList.get(i).getId() == entry.getId()) {
                timeEntryList.set(i, entry);
                persist();
                return entry;
            }
        }
        throw new NoSuchElementException("TimeEntry não encontrada com ID: " + entry.getId());
    }

    @Override
    public boolean deleteById(int id) {
        boolean removed = timeEntryList.removeIf(e -> e.getId() == id);
        if (removed) persist();
        return removed;
    }


    @Override
    public boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date) {
        boolean removed = timeEntryList.removeIf(e -> e.getEmployeeId() == employeeId && e.getEntryDate().equals(date));
        if (removed) persist();
        return removed;
    }

    @Override
    public boolean existsById(int id) {
        return timeEntryList.stream().anyMatch(te -> te.getId() == id);
    }

    @Override
    public boolean existsByEmployeeIdAndDate(int employeeId, LocalDate date) {
        return timeEntryList.stream().anyMatch(te -> te.getEmployeeId() == employeeId && date.equals(te.getEntryDate()));
    }
}
