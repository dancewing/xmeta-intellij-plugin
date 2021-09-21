package io.xmeta.visualization.events;

import io.xmeta.api.data.GraphRelationship;
import prefuse.visual.VisualItem;

import java.awt.event.MouseEvent;

@FunctionalInterface
public interface RelationshipCallback {
    void accept(GraphRelationship relationship, VisualItem item, MouseEvent e);
}
