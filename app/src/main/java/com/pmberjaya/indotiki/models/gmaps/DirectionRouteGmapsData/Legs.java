package com.pmberjaya.indotiki.models.gmaps.DirectionRouteGmapsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 5/28/2016.
 */
public class Legs {
    @SerializedName("steps")
    @Expose
    private List<Steps> steps = new ArrayList<Steps>();

    /**
     * @return The elements
     */
    public List<Steps> getSteps() {
        return steps;
    }

    /**
     * @param steps The elements
     */
    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }
}
