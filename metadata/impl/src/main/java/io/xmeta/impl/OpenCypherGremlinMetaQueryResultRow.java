package io.xmeta.impl;

import io.xmeta.api.data.EntityRelationship;
import io.xmeta.api.data.MetaNode;
import io.xmeta.api.query.MetaQueryResultColumn;
import io.xmeta.api.query.MetaQueryResultRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OpenCypherGremlinMetaQueryResultRow implements MetaQueryResultRow {
    private Map<String, Object> results;
    private Set<MetaNode> nodes;
    private Set<EntityRelationship> relationships;

    public OpenCypherGremlinMetaQueryResultRow(Map<String, Object> results, Set<MetaNode> nodes, Set<EntityRelationship> relationships) {
        this.results = results;
        this.nodes = nodes;
        this.relationships = relationships;
    }

    @Override
    public Object getValue(MetaQueryResultColumn column) {
        return results.get(column.getName());
    }

    @Override
    public List<MetaNode> getNodes() {
        return new ArrayList<>(nodes);
    }

    @Override
    public List<EntityRelationship> getRelationships() {
        return new ArrayList<>(relationships);
    }
}
