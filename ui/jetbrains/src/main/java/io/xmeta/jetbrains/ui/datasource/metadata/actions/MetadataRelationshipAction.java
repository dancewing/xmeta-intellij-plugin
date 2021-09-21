package io.xmeta.jetbrains.ui.datasource.metadata.actions;

import io.xmeta.api.data.IDNameData;

import javax.swing.*;

public class MetadataRelationshipAction extends MetadataAction {

    private static final String QUERY = "MATCH p=()-[r:`%s`]->() RETURN p LIMIT 25";

    MetadataRelationshipAction(IDNameData data, String dataSourceUuid, String title, String description, Icon icon) {
        super(data, dataSourceUuid, title, description, icon);
    }

    @Override
    protected String getQuery(String relationship) {
        return String.format(QUERY, relationship);
    }
}
