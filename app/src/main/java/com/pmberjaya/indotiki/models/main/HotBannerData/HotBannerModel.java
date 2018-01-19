package com.pmberjaya.indotiki.models.main.HotBannerData;

/**
 * Created by edwin on 4/26/2016.
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotBannerModel {

    @SerializedName("result_array")
    @Expose
    private List<HotBannerData> result_array = new ArrayList<HotBannerData>();
    @SerializedName("total_rows")
    @Expose
    private String total_rows;
    /**
     *
     * @return
     * The resultArray
     */
    public List<HotBannerData> getResultArray() {
        return result_array;
    }

    /**
     *
     * @param result_array
     * The result_array
     */
    public void setResultArray(List<HotBannerData> result_array) {
        this.result_array = result_array;
    }
    public String getTotal() {
        return total_rows;
    }


    public void setTotal(String total_rows) {
        this.total_rows = total_rows;
    }
}
