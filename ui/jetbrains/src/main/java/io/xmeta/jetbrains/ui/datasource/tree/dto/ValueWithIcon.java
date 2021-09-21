package io.xmeta.jetbrains.ui.datasource.tree.dto;


import javax.swing.*;

public class ValueWithIcon {

    private final Icon icon;
    private final String value;

    public ValueWithIcon(Icon icon, String value) {
        this.icon = icon;
        this.value = value;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getValue() {
        return value;
    }
}
