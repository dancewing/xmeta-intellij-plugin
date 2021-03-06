package io.xmeta.jetbrains.ui.datasource.tree;

import io.xmeta.api.data.MetaEntity;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import icons.MetaIcons;

import java.util.Optional;

public class EntityTypeTreeNodeModel extends MetadataTreeNodeModel<MetaEntity> {

    private static final String NAME_WITH_COUNT = "%s (%d)";
    private int count;

    public EntityTypeTreeNodeModel(Neo4jTreeNodeType type, DataSource dataSourceApi, MetaEntity value, int count) {
        super(type, dataSourceApi, value, MetaIcons.Nodes.ENTITY);
        this.count = count;
    }

    @Override
    public Optional<String> getText() {
        return super.getText()
                .map(text -> String.format(NAME_WITH_COUNT, text, count));
    }
}
