package com.calchoras.view;

import javax.swing.*;
import java.awt.*;

public class EmployeeListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
                                                  int index, boolean isSelected,
                                                  boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof EmployeeListItem item) {
            if (!item.isActive()) {
                setForeground(Color.LIGHT_GRAY);
                setFont(getFont().deriveFont(Font.ITALIC));
            } else {
                if (!isSelected) {
                    setForeground(list.getForeground());
                    setFont(list.getFont());
                }
            }
        }
        return this;
    }
}