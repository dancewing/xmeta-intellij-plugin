package io.xmeta.visualization;

import io.xmeta.api.data.MetaNode;
import io.xmeta.api.data.EntityRelationship;
import io.xmeta.visualization.events.EventType;
import io.xmeta.visualization.events.NodeCallback;
import io.xmeta.visualization.events.RelationshipCallback;

import javax.swing.*;

public interface VisualizationApi {

    JComponent getCanvas();

    void addNode(MetaNode node);

    void addRelation(EntityRelationship relationship);

    void clear();

    void paint();

    void stop();

    void addNodeListener(EventType type, NodeCallback action);

    void addEdgeListener(EventType type, RelationshipCallback action);

    void resetPan();

    void updateSettings();
}
