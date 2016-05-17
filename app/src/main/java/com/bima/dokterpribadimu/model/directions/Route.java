package com.bima.dokterpribadimu.model.directions;

import java.util.List;

/**
 * Created by apradanas.
 */
public class Route {

    private List<Leg> legs;
    private OverviewPolyline overviewPolyline;

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }
}
