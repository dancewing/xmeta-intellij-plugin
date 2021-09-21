package io.xmeta.jetbrains.ui.datasource.metadata.actions;

import io.xmeta.api.data.IDNameData;

import javax.swing.*;

public class MetadataLabelFromAction extends MetadataAction {

    private static final String QUERY = "MATCH (n:`%s`)-[r]->() RETURN type(r), r LIMIT 25";

    MetadataLabelFromAction(IDNameData data, String dataSourceUuid, String title, String description, Icon icon) {
        super(data, dataSourceUuid, title, description, icon);
    }

    @Override
    protected String getQuery(String data) {
        return String.format(QUERY, data);
    }
}
