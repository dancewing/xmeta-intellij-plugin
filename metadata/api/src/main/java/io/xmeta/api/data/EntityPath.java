package io.xmeta.api.data;

import java.util.List;

public interface EntityPath extends NoIdEntity {

    /**
     * Return nodes and relationships.
     */
    List<Object> getComponents();

    default boolean isTypesSingle() {
        return true;
    }

    default String getRepresentation() {
        return "Path";
    }
}
