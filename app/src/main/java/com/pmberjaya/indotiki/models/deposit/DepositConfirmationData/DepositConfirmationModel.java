package com.pmberjaya.indotiki.models.deposit.DepositConfirmationData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 11/09/2017.
 */

public class DepositConfirmationModel {

    @SerializedName("result")
    @Expose
    private List<DepositConfirmationData> resultArray = new ArrayList<DepositConfirmationData>();
    @SerializedName("total")
    @Expose
    private String total;
    /**
     *
     * @return
     * The resultArray
     */
    public List<DepositConfirmationData> getResultArray() {
        return resultArray;
    }

    /**
     *
     * @param resultArray
     * The result_array
     */
    public void setResultArray(List<DepositConfirmationData> resultArray) {
        this.resultArray = resultArray;
    }
    public String getTotal() {
        return total;
    }


    public void setTotal(String total) {
        this.total = total;
    }
}