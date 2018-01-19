package com.pmberjaya.indotiki.callbacks.gmaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pmberjaya.indotiki.models.gmaps.PlaceNearbyGmapsData.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 5/30/2016.
 */
public class PlaceNearbyGmapsCallback {
    String callback;
    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();
    @SerializedName("status")
    @Expose
    private String status;

    /**
     *
     * @return
     * The rows
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The rows
     */
    public void setResults(List<Result> results) {
        this.results = results;
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
