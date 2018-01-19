package com.pmberjaya.indotiki.models.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 11/09/2017.
 */

public class EventPromoModel {
    @SerializedName("result_array")
    @Expose
    private List<EventPromoData> result_array = new ArrayList<EventPromoData>();
    @SerializedName("total_rows")
    @Expose
    private String total_rows;
    /**
     *
     * @return
     * The resultArray
     */
    public List<EventPromoData> getResultArray() {
        return result_array;
    }

    /**
     *
     * @param result_array
     * The result_array
     */
    public void setResultArray(List<EventPromoData> result_array) {
        this.result_array = result_array;
    }
    public String getTotal() {
        return total_rows;
    }


    public void setTotal(String total_rows) {
        this.total_rows = total_rows;
    }
}
