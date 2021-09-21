package io.xmeta.jetbrains.ui.datasource.metadata.dto;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.actions.DataSourceActionGroup;
import io.xmeta.jetbrains.ui.datasource.tree.TreeNodeModelApi;

import java.util.List;

public class DataSourceContextMenu implements ContextMenu {

    private DataSource dataSourceApi;

    public DataSourceContextMenu(DataSource dataSourceApi) {
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
