package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree;

import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphField;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSourceApi;
import icons.GraphIcons;

import java.util.Optional;

public class FieldTypeTreeNodeModel extends MetadataTreeNodeModel<GraphField> {

    private static final String NAME_WITH_COUNT = "%s";

    public FieldTypeTreeNodeModel(Neo4jTreeNodeType type, DataSourceApi dataSourceApi, GraphField value) {
        super(type, dataSourceApi, value, GraphIcons.Nodes.PROPERTY_KEY);
    }

    @Override
    public Optional<String> getText() {
        return super.getText()
                .map(text -> String.format(NAME_WITH_COUNT, text));
    }
}
