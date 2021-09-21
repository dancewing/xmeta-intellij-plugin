package io.xmeta.visualization.layouts;

import io.xmeta.visualization.GraphDisplay;
import io.xmeta.visualization.util.PrefuseUtil;
import prefuse.Visualization;
import prefuse.action.RepaintAction;

public class RepaintAndRepositionAction extends RepaintAction {

    private Visualization visualization;
    private GraphDisplay display;

    public RepaintAndRepositionAction(Visualization visualization, GraphDisplay display) {
        super(visualization);
        this.visualization = visualization;
        this.display = display;
    }

    @Override
    public void run(double frac) {
        PrefuseUtil.zoomAndPanToFit(visualization, display);
        super.run(frac);
    }
}
