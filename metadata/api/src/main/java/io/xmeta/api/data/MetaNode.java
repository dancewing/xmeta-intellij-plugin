package io.xmeta.api.data;

public interface MetaNode extends MetaEntity {

    default String getRepresentation() {
        return "Node[" + getId() + "]";
    }
}
