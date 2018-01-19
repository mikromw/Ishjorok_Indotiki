package com.pmberjaya.indotiki.models.gmaps.DirectionRouteGmapsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edwin on 5/28/2016.
 */
public class Steps {
    @SerializedName("polyline")
    @Expose
    private PolylineData polyline = new PolylineData();

    /**
     * @return The polyline
     */
    public PolylineData getPolyline() {
        return polyline;
    }

    /**
     * @param polyline The polyline
     */
    public void setPolyline(PolylineData polyline) {
        this.polyline = polyline;
    }
}
