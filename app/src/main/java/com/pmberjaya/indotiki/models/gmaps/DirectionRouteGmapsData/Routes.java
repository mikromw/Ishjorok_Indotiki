package com.pmberjaya.indotiki.models.gmaps.DirectionRouteGmapsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 5/28/2016.
 */
public class Routes {
    @SerializedName("legs")
    @Expose
    private List<Legs> legs = new ArrayList<Legs>();

    /**
     * @return The elements
     */
    public List<Legs> getLegs() {
        return legs;
    }

    /**
     * @param legs The elements
     */
    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }
}
