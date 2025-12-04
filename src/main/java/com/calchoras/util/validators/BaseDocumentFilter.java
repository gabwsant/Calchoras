package com.calchoras.util.validators;

import javax.swing.text.*;

public abstract class BaseDocumentFilter extends DocumentFilter {

    protected abstract boolean isValid(String newText);

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {

        String current = fb.getDocument().getText(0, fb.getDocument().getLength());
        String updated = new StringBuilder(current)
                .insert(offset, string)
                .toString();

        if (isValid(updated)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {

        String current = fb.getDocument().getText(0, fb.getDocument().getLength());

        StringBuilder sb = new StringBuilder(current);
        sb.replace(offset, offset + length, text);
        String updated = sb.toString();

        if (isValid(updated)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
