package com.pmberjaya.indotiki.callbacks.gmaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pmberjaya.indotiki.models.gmaps.PlaceDetailGmapsData.ResultDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 5/30/2016.
 */
public class PlaceDetailGmapsCallback {
    String callback;
    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    @SerializedName("results")
    @Expose
    private List<ResultDetail> results = new ArrayList<ResultDetail>();
    @SerializedName("status")
    @Expose
    private String status;

    /**
     *
     * @return
     * The rows
     */
    public List<ResultDetail> getResults() {
        return results;
    }
    /**
     *
     * @param results
     * The rows
     */
    public void setResults(List<ResultDetail> results) {
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
