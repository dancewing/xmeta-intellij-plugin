package io.xmeta.visualization.events;

import io.xmeta.api.data.MetaNode;
import prefuse.visual.VisualItem;

import java.awt.event.MouseEvent;

@FunctionalInterface
public interface NodeCallback {
    void accept(MetaNode node, VisualItem item, MouseEvent e);
}
