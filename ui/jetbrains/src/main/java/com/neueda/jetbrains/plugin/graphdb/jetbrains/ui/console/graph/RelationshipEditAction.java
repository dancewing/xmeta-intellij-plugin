package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.console.graph;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphRelationship;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSourceApi;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.database.DiffService;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.interactions.EditEntityDialog;

import javax.swing.*;

public class RelationshipEditAction extends AnAction {

    private DataSourceApi dataSource;
    private GraphRelationship relationship;

    RelationshipEditAction(String title, String description, Icon icon, DataSourceApi dataSource, GraphRelationship relationship) {
        super(title, description, icon);
        this.dataSource = dataSource;
        this.relationship = relationship;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getEventProject(e);
        EditEntityDialog dialog = new EditEntityDialog(project, relationship);
        if (dialog.showAndGet()) {
            DiffService diffService = new DiffService(project);
            diffService.updateRelationShip(dataSource, relationship, dialog.getUpdatedEntity());
        }
    }
}
