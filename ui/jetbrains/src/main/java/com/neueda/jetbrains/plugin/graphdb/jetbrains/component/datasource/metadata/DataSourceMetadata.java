package com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.metadata;

import com.neueda.jetbrains.plugin.graphdb.database.api.data.Workspace;
import com.neueda.jetbrains.plugin.graphdb.database.opencypher.gremlin.WorkspaceDomain;

import java.util.List;
import java.util.Map;

public interface DataSourceMetadata {

    List<Map<String, String>> getMetadata(String metadataKey);

    boolean isMetadataExists(String metadataKey);

    List<Workspace> getWorkspaces();
}
