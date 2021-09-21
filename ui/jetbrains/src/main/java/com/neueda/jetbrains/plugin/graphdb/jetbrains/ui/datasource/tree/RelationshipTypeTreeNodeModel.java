package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree;

import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphRelationship;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;

import java.util.Optional;

public class RelationshipTypeTreeNodeModel extends MetadataTreeNodeModel<GraphRelationship> {

    private static final String NAME_WITH_COUNT = "%s (%d)";
    private Long count;

    public RelationshipTypeTreeNodeModel(Neo4jTreeNodeType type, DataSource dataSourceApi, GraphRelationship value, Long count) {
        super(type, dataSourceApi, value);
        this.count = count;
    }

    @Override
    public Optional<String> getText() {
        return super.getText()
                .map(text -> String.format(NAME_WITH_COUNT, text, count));
    }
}
