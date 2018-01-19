package com.pmberjaya.indotiki.interfaces.bookingData;

import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData.BookingCompleteModel;

/**
 * Created by edwin on 01/04/2017.
 */

public interface BookingCompleteInterface {
    void onGetBookingCompletedSuccess(BaseGenericCallback<BookingCompleteModel> baseGenericCallback);
    void onGetBookingCompletedError(APIErrorCallback apiErrorCallback);
}
