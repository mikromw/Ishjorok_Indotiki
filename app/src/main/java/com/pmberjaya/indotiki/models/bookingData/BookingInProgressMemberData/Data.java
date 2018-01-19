package com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData;

/**
 * Created by edwin on 4/26/2016.
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

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