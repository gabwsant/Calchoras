package com.calchoras.view;

import lombok.Getter;

public class EmployeeListItem {
    @Getter
    private final int id;
    private final String displayName;

    public EmployeeListItem(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

