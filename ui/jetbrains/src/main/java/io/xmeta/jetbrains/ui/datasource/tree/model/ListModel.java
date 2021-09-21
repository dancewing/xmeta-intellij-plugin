package io.xmeta.jetbrains.ui.datasource.tree.model;

import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.tree.Neo4jEntityViewNodeType;
import io.xmeta.jetbrains.ui.datasource.tree.NodeType;
import io.xmeta.jetbrains.ui.datasource.tree.TreeNodeModelApi;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ListModel implements TreeNodeModelApi {

    private NodeType type = Neo4jEntityViewNodeType.NODE_MAP;
    private String text;
    private String description = "list";
    private DataSource dataSourceApi;

    public ListModel(String text, DataSource dataSourceApi) {
        this.text = text;
        this.dataSourceApi = dataSourceApi;
    }

    @Override
    public NodeType getType() {
        return type;
    }

    @Override
    public Optional<String> getText() {
        return Optional.of(text);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of(description);
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
