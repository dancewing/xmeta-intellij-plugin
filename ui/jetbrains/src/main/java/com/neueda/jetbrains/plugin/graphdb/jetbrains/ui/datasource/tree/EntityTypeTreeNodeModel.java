package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree;

import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphEntity;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;
import icons.GraphIcons;

import java.util.Optional;

public class EntityTypeTreeNodeModel extends MetadataTreeNodeModel<GraphEntity> {

    private static final String NAME_WITH_COUNT = "%s (%d)";
    private int count;

    public EntityTypeTreeNodeModel(Neo4jTreeNodeType type, DataSource dataSourceApi, GraphEntity value, int count) {
        super(type, dataSourceApi, value, GraphIcons.Nodes.ENTITY);
        this.count = count;
    }

    @Override
    public Optional<String> getText() {
        return super.getText()
                .map(text -> String.format(NAME_WITH_COUNT, text, count));
    }
}
