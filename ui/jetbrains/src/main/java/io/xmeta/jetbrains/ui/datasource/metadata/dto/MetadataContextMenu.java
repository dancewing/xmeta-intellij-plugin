package io.xmeta.jetbrains.ui.datasource.metadata.dto;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import io.xmeta.api.data.IDNameData;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.metadata.actions.MetadataActionGroup;
import io.xmeta.jetbrains.ui.datasource.tree.NodeType;
import io.xmeta.jetbrains.ui.datasource.tree.TreeNodeModelApi;

import java.util.List;

public class MetadataContextMenu implements ContextMenu {

    private NodeType metadataType;
    private DataSource dataSourceApi;
    private IDNameData data;

    public MetadataContextMenu(NodeType metadataType, DataSource dataSourceApi, IDNameData data) {
        this.metadataType = metadataType;
        this.dataSourceApi = dataSourceApi;
        this.data = data;
    }

    @Override
    public void showPopup(DataContext dataContext, List<TreeNodeModelApi> selectedData) {
        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(
                data.getName(),
                new MetadataActionGroup(metadataType, data, dataSourceApi.getUUID(), selectedData),
                dataContext,
                JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                true
        );

        popup.showInBestPositionFor(dataContext);
    }

    public NodeType getMetadataType() {
        return metadataType;
    }

    public DataSource getDataSourceApi() {
        return dataSourceApi;
    }

    public String getData() {
        return data.getName();
    }
}
