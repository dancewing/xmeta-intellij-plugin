package io.xmeta.jetbrains.ui.datasource.tree;

import io.xmeta.api.data.EntityField;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import icons.MetaIcons;

import java.util.Optional;

public class FieldTypeTreeNodeModel extends MetadataTreeNodeModel<EntityField> {

    private static final String NAME_WITH_COUNT = "%s";

    public FieldTypeTreeNodeModel(Neo4jTreeNodeType type, DataSource dataSourceApi, EntityField value) {
        super(type, dataSourceApi, value, MetaIcons.Nodes.PROPERTY_KEY);
    }

    @Override
    public Optional<String> getText() {
        return super.getText()
                .map(text -> String.format(NAME_WITH_COUNT, text));
    }
}
