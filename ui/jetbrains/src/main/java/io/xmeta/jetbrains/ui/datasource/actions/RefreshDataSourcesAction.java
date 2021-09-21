package io.xmeta.jetbrains.ui.datasource.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import io.xmeta.jetbrains.ui.datasource.DataSourcesView;

public class RefreshDataSourcesAction extends AnActionButton {

    private final DataSourcesView dataSourcesView;

    public RefreshDataSourcesAction(DataSourcesView dataSourcesView) {
        super("Refresh", "Refresh all data sources", AllIcons.Actions.Refresh);
        this.dataSourcesView = dataSourcesView;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        dataSourcesView.refreshDataSourcesMetadata();
    }
}
