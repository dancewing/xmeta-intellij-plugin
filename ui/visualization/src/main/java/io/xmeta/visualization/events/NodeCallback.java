package io.xmeta.visualization.events;

import io.xmeta.api.data.GraphNode;
import prefuse.visual.VisualItem;

import java.awt.event.MouseEvent;

@FunctionalInterface
public interface NodeCallback {
    void accept(GraphNode node, VisualItem item, MouseEvent e);
}
