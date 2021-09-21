package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.messages.MessageBus;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.IDNameData;

import javax.swing.*;

public abstract class MetadataAction extends AnAction {

    private IDNameData data;
    private String dataSourceUuid;

    MetadataAction(IDNameData data, String dataSourceUuid, String title, String description, Icon icon) {
        super(title, description, icon);
        this.data = data;
        this.dataSourceUuid = dataSourceUuid;
    }

    protected abstract String getQuery(String data);

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getEventProject(e);
        MessageBus messageBus = project.getMessageBus();
        PsiElement[] psiElements = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY);

//        ExecuteQueryEvent executeQueryEvent = messageBus.syncPublisher(ExecuteQueryEvent.EXECUTE_QUERY_TOPIC);
//
//        ExecuteQueryPayload payload = new ExecuteQueryPayload(getQuery(data));
//
//        DataSourcesSettings dataSourcesComponent = project.getComponent(DataSourcesSettings.class);
//        Optional<DataSourceApi> dataSource = dataSourcesComponent.getDataSourceContainer().findDataSource(dataSourceUuid);
//
//        dataSource.ifPresent(dataSourceApi -> executeQueryEvent.executeQuery(dataSourceApi, payload));
//
//        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(CONSOLE_TOOL_WINDOW);
//        if (!toolWindow.isVisible()) {
//            ConsoleToolWindow.ensureOpen(project);
//            messageBus.syncPublisher(OpenTabEvent.OPEN_TAB_TOPIC).openTab(Tabs.TABLE);
//        }
    }
}
