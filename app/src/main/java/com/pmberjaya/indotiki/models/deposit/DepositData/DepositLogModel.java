package com.pmberjaya.indotiki.models.deposit.DepositData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 11/09/2017.
 */

public class DepositLogModel {

    @SerializedName("result")
    @Expose
    private List<DepositData> resultArray = new ArrayList<DepositData>();
    @SerializedName("total")
    @Expose
    private String total;
    /**
     *
     * @return
     * The resultArray
     */
    public List<DepositData> getResultArray() {
        return resultArray;
    }

    /**
     *
     * @param resultArray
     * The result_array
     */
    public void setResultArray(List<DepositData> resultArray) {
        this.resultArray = resultArray;
    }
    public String getTotal() {
        return total;
    }


    public void setTotal(String total) {
        this.total = total;
    }
}