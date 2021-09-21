package io.xmeta.api.query;

import io.xmeta.api.data.MetaNode;
import io.xmeta.api.data.EntityRelationship;

import java.util.List;

public interface MetaQueryResultRow {

    Object getValue(MetaQueryResultColumn column);

    List<MetaNode> getNodes();

    List<EntityRelationship> getRelationships();
}
