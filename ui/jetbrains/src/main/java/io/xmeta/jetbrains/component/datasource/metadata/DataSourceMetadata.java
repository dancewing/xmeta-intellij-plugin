package io.xmeta.jetbrains.component.datasource.metadata;

import io.xmeta.api.data.Workspace;

import java.util.List;
import java.util.Map;

public interface DataSourceMetadata {

    List<Map<String, String>> getMetadata(String metadataKey);

    boolean isMetadataExists(String metadataKey);

    List<Workspace> getWorkspaces();
}
