package io.xmeta.api.query;

import io.xmeta.api.data.GraphNode;
import io.xmeta.api.data.GraphRelationship;

import java.util.List;

public interface GraphQueryResultRow {

    Object getValue(GraphQueryResultColumn column);

    List<GraphNode> getNodes();

    List<GraphRelationship> getRelationships();
}
