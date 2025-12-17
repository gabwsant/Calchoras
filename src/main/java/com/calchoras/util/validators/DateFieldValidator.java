package com.calchoras.util.validators;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.time.format.DateTimeFormatter;

public class DateFieldValidator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void apply(JTextField field) {
        ((AbstractDocument) field.getDocument())
                .setDocumentFilter(new BaseDocumentFilter() {

                    @Override
                    protected boolean isValid(String text) {
                        if (text.isEmpty()) return true;
                        if (text.matches("^\\d{1,2}$")) return true;
                        if (text.matches("^\\d{1,2}/$")) return true;
                        if (text.matches("^\\d{1,2}/\\d{1,2}$")) return true;
                        if (text.matches("^\\d{1,2}/\\d{1,2}/$")) return true;

                        if (text.matches("^\\d{1,2}/\\d{1,2}/\\d{1,4}$")) {
                            String[] parts = text.split("/");
                            int dd = Integer.parseInt(parts[0]);
                            int mm = Integer.parseInt(parts[1]);
                            int yyyy = Integer.parseInt(parts[2]);

                            if (dd < 0 || dd > 31) return false;
                            if (mm < 0 || mm > 12) return false;
                            if (yyyy < 2000 || yyyy > 3000) return false;

                            return true;
                        }

                        return false;
                    }
                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                            throws BadLocationException {

                        String current = fb.getDocument().getText(0, fb.getDocument().getLength());

                        StringBuilder sb = new StringBuilder(current);
                        sb.replace(offset, offset + length, text);
                        String updated = sb.toString();

                        if (updated.matches("^\\d{2}$")) {
                            updated = updated + "/";
                        }

                        if (updated.matches("^\\d{2}/\\d{2}$")) {
                            updated = updated + "/";
                        }

                        if (isValid(updated)) {
                            super.replace(fb, 0, current.length(), updated, attrs);
                        }
                    }
                });
    }
}