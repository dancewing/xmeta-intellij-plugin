package io.xmeta.visualization.events;

import io.xmeta.api.data.EntityRelationship;
import prefuse.visual.VisualItem;

import java.awt.event.MouseEvent;

@FunctionalInterface
public interface RelationshipCallback {
    void accept(EntityRelationship relationship, VisualItem item, MouseEvent e);
}
