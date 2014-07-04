package org.cheminfo.scripting.image.extraction;

import java.awt.Polygon;

public class EPolygon {

    private final Polygon polygon;

    public EPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public int getNpoints() {
        return polygon.npoints;
    }

    public int[] getXpoints() {
        return polygon.xpoints;
    }

    public int[] getYpoints() {
        return polygon.ypoints;
    }

    public boolean contains(double x, double y) {
        return polygon.contains(x, y);
    }

}