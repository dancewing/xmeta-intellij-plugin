package io.xmeta.jetbrains.ui.datasource.tree;

import io.xmeta.api.data.IDNameData;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.metadata.dto.ContextMenu;
import io.xmeta.jetbrains.ui.datasource.metadata.dto.MetadataContextMenu;

import javax.swing.*;
import java.util.Optional;

public abstract class MetadataTreeNodeModel<T extends IDNameData> implements TreeNodeModelApi<T> {

    private MetadataContextMenu metadataContextMenu;
    private NodeType type;
    private Icon icon;
    private T value;
    private DataSource dataSourceApi;

    public MetadataTreeNodeModel(Neo4jTreeNodeType type, DataSource dataSourceApi, T value) {
        this(type, dataSourceApi, value, null);
    }

    public MetadataTreeNodeModel(Neo4jTreeNodeType type, DataSource dataSourceApi, T value, Icon icon) {
        this.type = type;
        this.value = value;
        this.dataSourceApi = dataSourceApi;
        this.icon = icon;
        prepareContextMenu();
    }

    private void prepareContextMenu() {
        if (type == Neo4jTreeNodeType.APP
                || type == Neo4jTreeNodeType.ENTITY) {
            metadataContextMenu = new MetadataContextMenu(type, getDataSourceApi(), value);
        }
    }

    public Optional<ContextMenu> getContextMenu() {
        return Optional.ofNullable(metadataContextMenu);
    }

    @Override
    public NodeType getType() {
        return type;
    }

    public void setType(Neo4jTreeNodeType type) {
        this.type = type;
    }

    @Override
    public Optional<Icon> getIcon() {
        return Optional.ofNullable(icon);
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public Optional<String> getText() {
        return Optional.ofNullable(value.getName());
    }

    @Override
    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public DataSource getDataSourceApi() {
        return dataSourceApi;
    }
}
