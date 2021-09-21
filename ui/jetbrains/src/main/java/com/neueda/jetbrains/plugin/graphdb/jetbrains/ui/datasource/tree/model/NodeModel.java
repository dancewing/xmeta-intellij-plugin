package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.model;

import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphNode;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata.dto.ContextMenu;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.Neo4jEntityViewNodeType;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.NodeType;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.TreeNodeModelApi;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Optional;

public class NodeModel implements TreeNodeModelApi {

    private NodeType type = Neo4jEntityViewNodeType.NODE;
    private GraphNode node;
    private String text;
    private String description = "node";
    private DataSource dataSourceApi;

    public NodeModel(GraphNode node, String text, DataSource dataSourceApi) {
        this.node = node;
        this.text = text;
        this.dataSourceApi = dataSourceApi;
    }

    @Override
    public Optional<ContextMenu> getContextMenu() {
//        return Optional.of(new EntityContextMenu(dataSourceApi, node));
        return Optional.empty();
    }

    @Override
    public NodeType getType() {
        return type;
    }

    @Override
    public Optional<Icon> getIcon() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getText() {
        return Optional.of(text);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of(description);
    }

    @Override
    public Optional<Object> getValue() {
        return Optional.of(node);
    }

    @Nullable
    @Override
    public DataSource getDataSourceApi() {
        return dataSourceApi;
    }

    @Override
    public String toString() {
        return text;
    }
}
