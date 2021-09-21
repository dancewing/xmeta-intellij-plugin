package io.xmeta.jetbrains.ui.datasource.tree.model;

import io.xmeta.api.data.MetaNode;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.metadata.dto.ContextMenu;
import io.xmeta.jetbrains.ui.datasource.tree.Neo4jEntityViewNodeType;
import io.xmeta.jetbrains.ui.datasource.tree.NodeType;
import io.xmeta.jetbrains.ui.datasource.tree.TreeNodeModelApi;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Optional;

public class NodeModel implements TreeNodeModelApi {

    private NodeType type = Neo4jEntityViewNodeType.NODE;
    private MetaNode node;
    private String text;
    private String description = "node";
    private DataSource dataSourceApi;

    public NodeModel(MetaNode node, String text, DataSource dataSourceApi) {
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
