package io.xmeta.visualization.settings;

import io.xmeta.visualization.constants.VisualizationParameters;
import io.xmeta.visualization.renderers.CustomEdgeRenderer;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.ShapeRenderer;

import static io.xmeta.visualization.constants.GraphColumns.TITLE;
import static io.xmeta.visualization.constants.VisualizationParameters.NODE_DIAMETER;
import static prefuse.Constants.EDGE_TYPE_LINE;

public class RendererProvider {

    private static final int TEXT_OVERLAP = 12;

    public static LabelRenderer labelRenderer() {
        LabelRenderer labelRenderer = new LabelRenderer(TITLE);
        labelRenderer.setMaxTextWidth(NODE_DIAMETER - TEXT_OVERLAP);

        return labelRenderer;
    }

    public static LabelRenderer edgeLabelRenderer() {
        LabelRenderer labelRenderer = new LabelRenderer(TITLE);
        labelRenderer.setMaxTextWidth(NODE_DIAMETER - TEXT_OVERLAP);

        return labelRenderer;
    }

    public static ShapeRenderer nodeRenderer() {
        ShapeRenderer nodeRenderer = new ShapeRenderer();
        nodeRenderer.setBaseSize(NODE_DIAMETER);

        return nodeRenderer;
    }

    public static EdgeRenderer edgeRenderer() {
        EdgeRenderer edgeRenderer = new CustomEdgeRenderer(EDGE_TYPE_LINE);
        edgeRenderer.setDefaultLineWidth(VisualizationParameters.EDGE_THICKNESS);
        edgeRenderer.setArrowHeadSize(10, 15);

        return edgeRenderer;
    }
}
