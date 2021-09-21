package com.neueda.jetbrains.plugin.graphdb.jetbrains.util;

import com.intellij.openapi.ui.ValidationInfo;

import javax.swing.*;

public class Validation {

    public static ValidationInfo validation(String message, JComponent component) {
        return new ValidationInfo(message, component);
    }

    public static ValidationInfo warning(String message, JComponent component) {
        return new ValidationInfo(message, component).asWarning().withOKEnabled();
    }
}
