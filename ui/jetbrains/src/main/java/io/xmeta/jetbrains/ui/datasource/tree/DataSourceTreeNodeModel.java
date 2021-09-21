package io.xmeta.jetbrains.ui.datasource.tree;

import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.metadata.dto.ContextMenu;
import io.xmeta.jetbrains.ui.datasource.metadata.dto.DataSourceContextMenu;

import javax.swing.*;
import java.util.Optional;

public class DataSourceTreeNodeModel implements TreeNodeModelApi {

    private DataSource dataSourceApi;
    private DataSourceContextMenu dataSourceContextMenu;

    public DataSourceTreeNodeModel(DataSource dataSourceApi) {
        this.dataSourceApi = dataSourceApi;
        this.dataSourceContextMenu = new DataSourceContextMenu(dataSourceApi);
    }

    public Optional<ContextMenu> getContextMenu() {
        return Optional.of(dataSourceContextMenu);
    }

    @Override
    public NodeType getType() {
        return Neo4jTreeNodeType.DATASOURCE;
    }

    @Override
    public Optional<Icon> getIcon() {
        return Optional.ofNullable(dataSourceApi.getDescription().getIcon());
    }

    @Override
    public Optional<String> getText() {
        return Optional.ofNullable(dataSourceApi.getName());
    }

    @Override
    public Optional<Object> getValue() {
        return Optional.empty();
    }

    @Override
    public DataSource getDataSourceApi() {
        return dataSourceApi;
    }
}
