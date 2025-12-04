package com.calchoras.util.validators;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class DateFieldValidator {

    public static void apply(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new BaseDocumentFilter() {

            @Override
            protected boolean isValid(String text) {
                return text.matches("^\\d{0,2}/?\\d{0,2}/?\\d{0,4}$");
            }
        });
    }
}
