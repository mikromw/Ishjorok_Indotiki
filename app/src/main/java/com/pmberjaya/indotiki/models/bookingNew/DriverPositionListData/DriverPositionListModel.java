package com.pmberjaya.indotiki.models.bookingNew.DriverPositionListData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 11/09/2017.
 */

public class DriverPositionListModel  {

    @SerializedName("result")
    @Expose
    private List<DriverPositionListData> result = new ArrayList<DriverPositionListData>();

    /**
     *
     * @return
     * The resultArray
     */
    public List<DriverPositionListData> getResultArray() {
        return result;
    }

    /**
     *
     * @param resultArray
     * The result_array
     */
    public void setResultArray(List<DriverPositionListData> resultArray) {
        this.result = resultArray;
    }

}