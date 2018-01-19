package com.pmberjaya.indotiki.interfaces.bookingData;

import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.models.bookingData.BookingReasonCancelData;

import java.util.List;

/**
 * Created by edwin on 24/02/2017.
 */
public interface BookingReasonCancelInterface {
    void onSuccessGetBookingReasonCancel(BaseGenericCallback<List<BookingReasonCancelData>> data);
    void onErrorGetBookingReasonCancel(APIErrorCallback data);
}
