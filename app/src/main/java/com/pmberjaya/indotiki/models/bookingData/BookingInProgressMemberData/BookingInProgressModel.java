package com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 11/09/2017.
 */

public class BookingInProgressModel {

    @SerializedName("result_array")
    @Expose
    private List<BookingInProgressMemberData> resultArray = new ArrayList<BookingInProgressMemberData>();

    /**
     *
     * @return
     * The resultArray
     */
    public List<BookingInProgressMemberData> getResultArray() {
        return resultArray;
    }

    /**
     *
     * @param resultArray
     * The result_array
     */
    public void setResultArray(List<BookingInProgressMemberData> resultArray) {
        this.resultArray = resultArray;
    }

}