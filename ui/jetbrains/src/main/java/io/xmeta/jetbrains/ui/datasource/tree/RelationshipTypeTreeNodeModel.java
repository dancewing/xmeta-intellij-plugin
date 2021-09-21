package io.xmeta.jetbrains.ui.datasource.tree;

import io.xmeta.api.data.EntityRelationship;
import io.xmeta.jetbrains.component.datasource.state.DataSource;

import java.util.Optional;

public class RelationshipTypeTreeNodeModel extends MetadataTreeNodeModel<EntityRelationship> {

    private static final String NAME_WITH_COUNT = "%s (%d)";
    private Long count;

    public RelationshipTypeTreeNodeModel(Neo4jTreeNodeType type, DataSource dataSourceApi, EntityRelationship value, Long count) {
        super(type, dataSourceApi, value);
        this.count = count;
    }

    @Override
    public Optional<String> getText() {
        return super.getText()
                .map(text -> String.format(NAME_WITH_COUNT, text, count));
    }
}
