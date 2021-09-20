package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata.dto;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.IDNameData;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSourceApi;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.actions.DataSourceActionGroup;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.TreeNodeModelApi;

import java.util.List;

public class DataSourceContextMenu implements ContextMenu {

    private DataSourceApi dataSourceApi;

    public DataSourceContextMenu(DataSourceApi dataSourceApi) {
        this.dataSourceApi = dataSourceApi;
    }

    @Override
    public void showPopup(DataContext dataContext, List<TreeNodeModelApi> selectedData) {
        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(
                dataSourceApi.getName(),
                new DataSourceActionGroup(dataSourceApi),
                dataContext,
                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                true
        );

        popup.showInBestPositionFor(dataContext);
    }
}
