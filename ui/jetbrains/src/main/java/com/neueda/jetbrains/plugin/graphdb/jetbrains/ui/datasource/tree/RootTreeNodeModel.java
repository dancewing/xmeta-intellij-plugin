package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree;

import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata.dto.ContextMenu;

import javax.swing.*;
import java.util.Optional;

public class RootTreeNodeModel implements TreeNodeModelApi {

    public static final String ROOT_NAME = "treeRoot";

    @Override
    public Optional<ContextMenu> getContextMenu() {
        return Optional.empty();
    }

    @Override
    public NodeType getType() {
        return Neo4jTreeNodeType.ROOT;
    }

    @Override
    public Optional<Icon> getIcon() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getText() {
        return Optional.of(ROOT_NAME);
    }

    @Override
    public Optional<Object> getValue() {
        return Optional.empty();
    }

    @Override
    public DataSource getDataSourceApi() {
        return null;
    }
}
