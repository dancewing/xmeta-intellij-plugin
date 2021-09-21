package io.xmeta.jetbrains.ui.datasource.metadata;

import com.intellij.util.messages.Topic;
import io.xmeta.jetbrains.component.datasource.metadata.DataSourceMetadata;
import io.xmeta.jetbrains.component.datasource.state.DataSource;

public interface MetadataRetrieveEvent {

    Topic<MetadataRetrieveEvent> METADATA_RETRIEVE_EVENT = Topic.create("GraphDatabaseDataSource.MetadataRetrieve", MetadataRetrieveEvent.class);

    void startMetadataRefresh(DataSource nodeDataSource);

    void metadataRefreshSucceed(DataSource nodeDataSource, DataSourceMetadata metadata);

    void metadataRefreshFailed(DataSource nodeDataSource, Exception exception);
}
