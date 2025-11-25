package com.calchoras.service.interfaces;

import com.calchoras.model.TimeEntry;
import java.util.List;

public interface ITimeEntryService {

    List<TimeEntry> getTimeEntriesForEmployee(int employeeId);
    void addTimeEntry(TimeEntry timeEntry);
    void deleteEntriesForEmployee(int employeeId);
    // void updateTimeEntry(TimeEntry timeEntry);
    // void deleteTimeEntry(int entryId);
}