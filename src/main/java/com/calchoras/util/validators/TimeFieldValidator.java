package com.calchoras.util.validators;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class TimeFieldValidator {

    public static void apply(JTextField field) {

        ((AbstractDocument) field.getDocument())
                .setDocumentFilter(new BaseDocumentFilter() {

                    @Override
                    protected boolean isValid(String text) {

                        if (text.isEmpty()) return true;

                        // Só números ou números + ":" (mesma regra que você já tem)
                        if (text.matches("^\\d{1,2}$")) return true;
                        if (text.matches("^\\d{1,2}:$")) return true;

                        // Se tiver formato HH:MM, validar valores reais
                        if (text.matches("^\\d{1,2}:\\d{1,2}$")) {

                            String[] parts = text.split(":");
                            int hh = Integer.parseInt(parts[0]);
                            int mm = Integer.parseInt(parts[1]);

                            // Validação REAL de horário
                            if (hh < 0 || hh > 23) return false;
                            if (mm < 0 || mm > 59) return false;

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
                            updated = updated + ":";
                        }

                        if (isValid(updated)) {
                            super.replace(fb, 0, current.length(), updated, attrs);
                        }
                    }
                });
    }
}


