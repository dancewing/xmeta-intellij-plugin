package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.database.DiffService;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.interactions.EditEntityDialog;

public class CreateNodeAction extends AnAction {
    private final DataSource dataSourceApi;

    public CreateNodeAction(String title, DataSource dataSourceApi) {
        super(title, null, null);
        this.dataSourceApi = dataSourceApi;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        EditEntityDialog dialog = new EditEntityDialog(e.getProject(), null);
        if (dialog.showAndGet()) {
            DiffService diffService = new DiffService(e.getProject());
            diffService.saveNewNode(dataSourceApi, dialog.getUpdatedEntity());
        }
    }
}
