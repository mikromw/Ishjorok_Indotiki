package com.pmberjaya.indotiki.models.gmaps.DirectionRouteGmapsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by edwin on 5/28/2016.
 */
public class PolylineData {
    @SerializedName("points")
    @Expose
    private String points;

    /**
     * @return The points
     */
    public String getPoints() {
        return points;
    }

    /**
     * @param points
     */
    public void setPoints(String points) {
        this.points = points;
    }
}
