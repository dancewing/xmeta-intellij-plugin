package io.xmeta.api.data;

public interface EntityRelationship extends MetaEntity {

    boolean hasStartAndEndNode();

    String getStartNodeId();

    String getEndNodeId();

    MetaNode getStartNode();

    MetaNode getEndNode();

    default String getRepresentation() {
        return "Relationship[" + getId() + "]";
    }
}
