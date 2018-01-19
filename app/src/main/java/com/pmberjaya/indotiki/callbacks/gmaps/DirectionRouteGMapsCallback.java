package com.pmberjaya.indotiki.callbacks.gmaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pmberjaya.indotiki.models.gmaps.DirectionRouteGmapsData.Routes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willy on 5/31/2016.
 */
public class DirectionRouteGMapsCallback {
    String callback;
    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    @SerializedName("routes")
    @Expose
    private List<Routes> routes = new ArrayList<Routes>();
    @SerializedName("status")
    @Expose
    private String status;

    /**
     *
     * @return
     * The rows
     */
    public List<Routes> getRoutes() {
        return routes;
    }

    /**
     *
     * @param routes
     * The rows
     */
    public void setRoute(List<Routes> routes) {
        this.routes = routes;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
