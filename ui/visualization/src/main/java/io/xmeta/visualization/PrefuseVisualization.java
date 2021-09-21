package io.xmeta.visualization;

import io.xmeta.api.data.MetaNode;
import io.xmeta.api.data.EntityRelationship;
import io.xmeta.visualization.events.EventType;
import io.xmeta.visualization.events.NodeCallback;
import io.xmeta.visualization.events.RelationshipCallback;
import io.xmeta.visualization.services.LookAndFeelService;

import javax.swing.*;

public class PrefuseVisualization implements VisualizationApi {

    private GraphDisplay display;

    public PrefuseVisualization(LookAndFeelService lookAndFeelService) {
        this.display = new GraphDisplay(lookAndFeelService);
    }

    @Override
    public void addNode(MetaNode node) {
        display.addNode(node);
    }

    @Override
    public void addRelation(EntityRelationship relationship) {
        display.addRelationship(relationship);
    }

    @Override
    public void clear() {
        display.clearGraph();
    }

    @Override
    public void paint() {
        display.startLayout();
    }

    @Override
    public void stop() {
        display.stopLayout();
    }

    @Override
    public JComponent getCanvas() {
        return display;
    }

    @Override
    public void addNodeListener(EventType type, NodeCallback action) {
        display.addNodeListener(type, action);
    }

    @Override
    public void addEdgeListener(EventType type, RelationshipCallback action) {
        display.addEdgeListener(type, action);
    }

    @Override
    public void resetPan() {
        display.zoomAndPanToFit();
    }

    @Override
    public void updateSettings() {
        display.updateSettings();
    }
}
