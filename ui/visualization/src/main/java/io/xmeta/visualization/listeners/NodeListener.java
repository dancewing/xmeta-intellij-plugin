package io.xmeta.visualization.listeners;

import io.xmeta.api.data.MetaNode;
import io.xmeta.visualization.events.EventType;
import io.xmeta.visualization.events.NodeCallback;
import prefuse.controls.ControlAdapter;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

import java.awt.event.MouseEvent;
import java.util.Map;

public class NodeListener extends ControlAdapter {

    private EventType type;
    private NodeCallback callback;
    private Map<String, MetaNode> graphNodeMap;

    public NodeListener(EventType type, NodeCallback callback, Map<String, MetaNode> graphNodeMap) {
        this.type = type;
        this.callback = callback;
        this.graphNodeMap = graphNodeMap;
    }

    @Override
    public void itemEntered(VisualItem item, MouseEvent e) {
        if (type == EventType.HOVER_START && item instanceof NodeItem) {
            callback.accept(graphNodeMap.get(item.get("id")), item, e);
        }
    }

    @Override
    public void itemExited(VisualItem item, MouseEvent e) {
        if (type == EventType.HOVER_END && item instanceof NodeItem) {
            callback.accept(graphNodeMap.get(item.get("id")), item, e);
        }
    }

    @Override
    public void itemClicked(VisualItem item, MouseEvent e) {
        if (type == EventType.CLICK && item instanceof NodeItem) {
            callback.accept(graphNodeMap.get(item.get("id")), item, e);
        }
    }

}
