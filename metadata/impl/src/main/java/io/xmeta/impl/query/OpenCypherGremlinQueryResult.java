package io.xmeta.impl.query;

import io.xmeta.api.data.EntityRelationship;
import io.xmeta.api.data.MetaNode;
import io.xmeta.api.query.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OpenCypherGremlinQueryResult implements MetaQueryResult {
    private final long executionTimeMs;
    private List<MetaQueryResultColumn> columns;
    private List<MetaQueryResultRow> rows;
    private List<MetaNode> nodes;
    private List<EntityRelationship> relationships;

    public OpenCypherGremlinQueryResult(long executionTimeMs,
                                        List<MetaQueryResultColumn> columns,
                                        List<MetaQueryResultRow> rows,
                                        List<MetaNode> nodes,
                                        List<EntityRelationship> relationships) {
        this.executionTimeMs = executionTimeMs;
        this.columns = columns;
        this.rows = rows;
        this.nodes = nodes;
        this.relationships = relationships;
    }


    @Override
    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    @Override
    public String getResultSummary() {
        return "";
    }

    @Override
    public List<MetaQueryResultColumn> getColumns() {
        return columns;
    }

    @Override
    public List<MetaQueryResultRow> getRows() {
        return rows;
    }

    @Override
    public List<MetaNode> getNodes() {
        return nodes;
    }

    @Override
    public List<EntityRelationship> getRelationships() {
        return relationships;
    }

    @Override
    public List<MetaQueryNotification> getNotifications() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasPlan() {
        return false;
    }

    @Override
    public boolean isProfilePlan() {
        return false;
    }

    @Override
    public Optional<MetaQueryPlan> getPlan() {
        return Optional.empty();
    }
}
