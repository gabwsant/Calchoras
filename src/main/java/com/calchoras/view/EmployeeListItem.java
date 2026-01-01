package com.calchoras.view;

import lombok.Getter;

public class EmployeeListItem {
    @Getter
    private final int id;
    private final String displayName;
    @Getter
    private final boolean active;

    public EmployeeListItem(int id, String displayName, boolean active) {
        this.id = id;
        this.displayName = displayName;
        this.active = active;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

