package com.calchoras.view;

import lombok.Getter;

public class CompanyComboItem {
    @Getter
    private final int id;
    private final String name;

    public CompanyComboItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name; // aparece no combo
    }
}

