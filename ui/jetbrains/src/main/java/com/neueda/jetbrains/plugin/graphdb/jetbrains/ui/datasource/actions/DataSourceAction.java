package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.util.FileUtil;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.util.Notifier;

import javax.swing.*;
import java.io.IOException;

public class DataSourceAction extends AnAction {

    private DataSource dataSource;

    DataSourceAction(String title, String description, Icon icon, DataSource dataSource) {
        super(title, description, icon);
        this.dataSource = dataSource;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getEventProject(e);
        try {
            FileUtil.openFile(project, FileUtil.getDataSourceFile(project, dataSource));
        } catch (IOException exception) {
            Notifier.error("Open editor error", exception.getMessage());
        }
    }
}
