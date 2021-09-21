package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree;

import com.neueda.jetbrains.plugin.graphdb.database.api.data.IDNameData;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata.dto.ContextMenu;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata.dto.MetadataContextMenu;

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
