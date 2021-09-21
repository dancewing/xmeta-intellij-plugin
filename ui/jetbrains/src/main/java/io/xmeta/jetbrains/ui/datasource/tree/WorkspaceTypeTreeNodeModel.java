package io.xmeta.jetbrains.ui.datasource.tree;

import io.xmeta.api.data.MetaWorkspace;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import icons.MetaIcons;

import java.util.Optional;

public class WorkspaceTypeTreeNodeModel extends MetadataTreeNodeModel<MetaWorkspace> {

    private static final String NAME_WITH_COUNT = "%s (%d)";
    private int count;

    public WorkspaceTypeTreeNodeModel(Neo4jTreeNodeType type, DataSource dataSourceApi, MetaWorkspace value, int count) {
        super(type, dataSourceApi, value,
                MetaIcons.Nodes.WORKSPACE);
        this.count = count;
    }

    @Override
    public Optional<String> getText() {
        return super.getText()
                .map(text -> String.format(NAME_WITH_COUNT, text, count));
    }
}
