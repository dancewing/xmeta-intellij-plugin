package io.xmeta.visualization.settings;

import io.xmeta.visualization.GraphDisplay;
import io.xmeta.visualization.layouts.CenteredLayout;
import io.xmeta.visualization.layouts.DynamicForceLayout;
import io.xmeta.visualization.layouts.RepaintAndRepositionAction;
import io.xmeta.visualization.services.LookAndFeelService;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.activity.Activity;

import static io.xmeta.visualization.constants.GraphGroups.EDGE_LABEL;
import static io.xmeta.visualization.constants.GraphGroups.GRAPH;
import static io.xmeta.visualization.constants.GraphGroups.NODE_LABEL;

public class LayoutProvider {

    private static final boolean ENFORCE_BOUNDS = false;

    public static ActionList forceLayout(Visualization viz, GraphDisplay display, LookAndFeelService lookAndFeel) {
        ActionList actions = new ActionList(viz);

        actions.add(new DynamicForceLayout(GRAPH, ENFORCE_BOUNDS));
        actions.add(ColorProvider.colors(lookAndFeel));
        actions.add(new RepaintAndRepositionAction(viz, display));

        return actions;
    }

    public static ActionList repaintLayout(LookAndFeelService lookAndFeelService) {
        ActionList repaint = new ActionList(Activity.INFINITY);
        repaint.add(new CenteredLayout(NODE_LABEL));
        repaint.add(new CenteredLayout(EDGE_LABEL));
        repaint.add(ColorProvider.colors(lookAndFeelService));
        repaint.add(new RepaintAction());

        return repaint;
    }
}
