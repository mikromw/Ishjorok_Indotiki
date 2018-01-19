package com.pmberjaya.indotiki.callbacks.gmaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pmberjaya.indotiki.models.gmaps.DistanceTimeGMapsData.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 4/23/2016.
 */
public class DistanceTimeGMapsCallback {
    String callback;
    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    @SerializedName("rows")
    @Expose
    private List<Row> rows = new ArrayList<Row>();
    @SerializedName("status")
    @Expose
    private String status;

    /**
     *
     * @return
     * The rows
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     *
     * @param rows
     * The rows
     */
    public void setRows(List<Row> rows) {
        this.rows = rows;
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

