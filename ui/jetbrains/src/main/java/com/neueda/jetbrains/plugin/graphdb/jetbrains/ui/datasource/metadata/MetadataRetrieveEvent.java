package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata;

import com.intellij.util.messages.Topic;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.metadata.DataSourceMetadata;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;

public interface MetadataRetrieveEvent {

    Topic<MetadataRetrieveEvent> METADATA_RETRIEVE_EVENT = Topic.create("GraphDatabaseDataSource.MetadataRetrieve", MetadataRetrieveEvent.class);

    void startMetadataRefresh(DataSource nodeDataSource);

    void metadataRefreshSucceed(DataSource nodeDataSource, DataSourceMetadata metadata);

    void metadataRefreshFailed(DataSource nodeDataSource, Exception exception);
}
