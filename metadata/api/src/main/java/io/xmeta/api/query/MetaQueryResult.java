package io.xmeta.api.query;

import io.xmeta.api.data.MetaNode;
import io.xmeta.api.data.EntityRelationship;

import java.util.List;
import java.util.Optional;

public interface MetaQueryResult {

    long getExecutionTimeMs();

    String getResultSummary();

    List<MetaQueryResultColumn> getColumns();

    List<MetaQueryResultRow> getRows();

    List<MetaNode> getNodes();

    List<EntityRelationship> getRelationships();

    List<MetaQueryNotification> getNotifications();

    boolean hasPlan();

    boolean isProfilePlan();

    Optional<MetaQueryPlan> getPlan();
}
