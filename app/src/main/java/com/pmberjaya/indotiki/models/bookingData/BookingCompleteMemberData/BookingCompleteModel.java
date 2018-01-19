package com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwin on 11/09/2017.
 */

public class BookingCompleteModel {
    @SerializedName("result_array")
    @Expose
    private List<BookingCompleteData> resultArray = new ArrayList<BookingCompleteData>();

    /**
     *
     * @return
     * The resultArray
     */
    public List<BookingCompleteData> getResultArray() {
        return resultArray;
    }

    /**
     *
     * @param resultArray
     * The result_array
     */
    public void setResultArray(List<BookingCompleteData> resultArray) {
        this.resultArray = resultArray;
    }
}
