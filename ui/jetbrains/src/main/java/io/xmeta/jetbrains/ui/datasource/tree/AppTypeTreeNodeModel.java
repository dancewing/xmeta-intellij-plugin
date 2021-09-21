package io.xmeta.jetbrains.ui.datasource.tree;

import io.xmeta.api.data.MetaApp;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import icons.MetaIcons;

import java.util.Optional;

public class AppTypeTreeNodeModel extends MetadataTreeNodeModel<MetaApp> {

    private static final String NAME_WITH_COUNT = "%s (%d)";
    private int count;

    public AppTypeTreeNodeModel(Neo4jTreeNodeType type, DataSource dataSourceApi, MetaApp value, int count) {
        super(type, dataSourceApi, value, MetaIcons.Nodes.APP);
        this.count = count;
    }

    @Override
    public Optional<String> getText() {
        return super.getText()
                .map(text -> String.format(NAME_WITH_COUNT, text, count));
    }
}
