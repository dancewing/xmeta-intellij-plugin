package io.xmeta.jetbrains.ui.datasource.tree.model;

import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.tree.Neo4jEntityViewNodeType;
import io.xmeta.jetbrains.ui.datasource.tree.NodeType;

import java.util.Optional;

public class LabelsModel extends RootObjectAwareModel {

    private NodeType type = Neo4jEntityViewNodeType.NODE_LABELS;
    private String text = "labels";
    private String description = "list";

    public LabelsModel(DataSource dataSourceApi, Object rootObject) {
        super(dataSourceApi, rootObject);
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

    @Override
    public String toString() {
        return text;
    }
}
