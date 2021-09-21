package io.xmeta.api.data;

public interface GraphNode extends GraphEntity {

    default String getRepresentation() {
        return "Node[" + getId() + "]";
    }
}
