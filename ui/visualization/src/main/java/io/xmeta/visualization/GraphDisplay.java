package io.xmeta.visualization;

import com.intellij.util.ui.UIUtil;
import io.xmeta.api.data.MetaNode;
import io.xmeta.api.data.EntityRelationship;
import io.xmeta.visualization.controls.CustomNeighborHighlightControl;
import io.xmeta.visualization.events.EventType;
import io.xmeta.visualization.events.NodeCallback;
import io.xmeta.visualization.events.RelationshipCallback;
import io.xmeta.visualization.layouts.CustomItemSorter;
import io.xmeta.visualization.listeners.NodeListener;
import io.xmeta.visualization.listeners.RelationshipListener;
import io.xmeta.visualization.services.LookAndFeelService;
import io.xmeta.visualization.settings.LayoutProvider;
import io.xmeta.visualization.settings.SchemaProvider;
import io.xmeta.visualization.util.DisplayUtil;
import io.xmeta.visualization.util.PrefuseUtil;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.DragControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.RendererFactory;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

import java.util.HashMap;
import java.util.Map;

import static io.xmeta.visualization.constants.GraphColumns.*;
import static io.xmeta.visualization.constants.GraphGroups.*;
import static io.xmeta.visualization.settings.RendererProvider.*;
import static prefuse.Constants.*;

public class GraphDisplay extends Display {

    private static final boolean DIRECTED = true;

    private static final String LAYOUT = "layout";
    private static final String REPAINT = "repaint";

    private Graph graph;

    private Map<String, Node> nodeMap = new HashMap<>();
    private Map<String, MetaNode> graphNodeMap = new HashMap<>();
    private Map<String, EntityRelationship> graphRelationshipMap = new HashMap<>();
    private CustomNeighborHighlightControl highlightControl;
    private WheelZoomControl zoomControl;
    private LookAndFeelService lookAndFeel;

    public GraphDisplay(LookAndFeelService lookAndFeel) {
        super(new Visualization());
        this.lookAndFeel = lookAndFeel;

        if (UIUtil.isUnderDarcula()) {
            setBackground(lookAndFeel.getBackgroundColor().darker());
        } else {
            setBackground(lookAndFeel.getBackgroundColor());
        }

        graph = new Graph(DIRECTED);
        graph.addColumn(ID, String.class);
        graph.addColumn(TYPE, String.class);
        graph.addColumn(TITLE, String.class);

        m_vis.addGraph(GRAPH, graph, null, SchemaProvider.provideNodeSchema(), SchemaProvider.provideEdgeSchema());
        m_vis.setInteractive(EDGES, null, false);
        m_vis.setValue(NODES, null, VisualItem.SHAPE, SHAPE_ELLIPSE);

        m_vis.addDecorators(NODE_LABEL, NODES, SchemaProvider.provideFontSchema());
//        m_vis.addDecorators(EDGE_LABEL, EDGES, SchemaProvider.provideFontSchemaWithBackground());

        m_vis.setRendererFactory(setupRenderer());

        m_vis.putAction(LAYOUT, LayoutProvider.forceLayout(m_vis, this, lookAndFeel));
        m_vis.putAction(REPAINT, LayoutProvider.repaintLayout(lookAndFeel));

        setItemSorter(new CustomItemSorter());

        setHighQuality(true);

        addControlListener(new DragControl());
        zoomControl = new WheelZoomControl(lookAndFeel.isGraphViewZoomInverted(), lookAndFeel.isGraphViewZoomInverted());
        addControlListener(zoomControl);
        addControlListener(new ZoomToFitControl());
        addControlListener(new PanControl());
        highlightControl = new CustomNeighborHighlightControl();
        addControlListener(highlightControl);
    }

    public void clearGraph() {
        highlightControl.clear();
        graph.clear();
    }

    public void addNodeListener(EventType type, NodeCallback callback) {
        addControlListener(new NodeListener(type, callback, graphNodeMap));
    }

    public void addEdgeListener(EventType type, RelationshipCallback callback) {
        addControlListener(new RelationshipListener(type, callback, graphRelationshipMap));
    }

    public void addNode(MetaNode graphNode) {
        Node node = graph.addNode();
        node.set(ID, graphNode.getId());
        node.set(TYPE, DisplayUtil.getType(graphNode));
        node.set(TITLE, DisplayUtil.getProperty(graphNode));

        nodeMap.put(graphNode.getId(), node);
        graphNodeMap.put(graphNode.getId(), graphNode);
    }

    public void addRelationship(EntityRelationship graphRelationship) {
        if (graphRelationship.hasStartAndEndNode()) {
            String start = graphRelationship.getStartNode().getId();
            String end = graphRelationship.getEndNode().getId();

            Edge edge = graph.addEdge(nodeMap.get(start), nodeMap.get(end));
            edge.set(ID, graphRelationship.getId());
            edge.set(TITLE, graphRelationship.getId());
            graphRelationshipMap.put(graphRelationship.getId(), graphRelationship);
        }
    }

    private RendererFactory setupRenderer() {
        DefaultRendererFactory rendererFactory = new DefaultRendererFactory(nodeRenderer(), edgeRenderer());
        rendererFactory.add(new InGroupPredicate(NODE_LABEL), labelRenderer());
        rendererFactory.add(new InGroupPredicate(EDGE_LABEL), edgeLabelRenderer());

        return rendererFactory;
    }

    public void startLayout() {
        m_vis.run(LAYOUT);
        m_vis.run(REPAINT);
    }

    public void stopLayout() {
        m_vis.cancel(LAYOUT);
        m_vis.cancel(REPAINT);
    }

    public void zoomAndPanToFit() {
        PrefuseUtil.zoomAndPanToFit(m_vis, this);
    }

    public void updateSettings() {
        removeControlListener(zoomControl);
        zoomControl = new WheelZoomControl(lookAndFeel.isGraphViewZoomInverted(), lookAndFeel.isGraphViewZoomInverted());
        addControlListener(zoomControl);

    }
}
