package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree;

import com.neueda.jetbrains.plugin.graphdb.database.api.data.Workspace;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSourceApi;
import icons.GraphIcons;

import java.util.Optional;

public class WorkspaceTypeTreeNodeModel extends MetadataTreeNodeModel<Workspace> {

    private static final String NAME_WITH_COUNT = "%s (%d)";
    private int count;

    public WorkspaceTypeTreeNodeModel(Neo4jTreeNodeType type, DataSourceApi dataSourceApi, Workspace value, int count) {
        super(type, dataSourceApi, value,
                GraphIcons.Nodes.WORKSPACE);
        this.count = count;
    }

    @Override
    public Optional<String> getText() {
        return super.getText()
                .map(text -> String.format(NAME_WITH_COUNT, text, count));
    }
}
