package io.xmeta.api.query;

import io.xmeta.api.data.GraphNode;
import io.xmeta.api.data.GraphRelationship;

import java.util.List;
import java.util.Optional;

public interface GraphQueryResult {

    long getExecutionTimeMs();

    String getResultSummary();

    List<GraphQueryResultColumn> getColumns();

    List<GraphQueryResultRow> getRows();

    List<GraphNode> getNodes();

    List<GraphRelationship> getRelationships();

    List<GraphQueryNotification> getNotifications();

    boolean hasPlan();

    boolean isProfilePlan();

    Optional<GraphQueryPlan> getPlan();
}
