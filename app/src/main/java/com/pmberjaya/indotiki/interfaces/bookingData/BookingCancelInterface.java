package com.pmberjaya.indotiki.interfaces.bookingData;

import com.pmberjaya.indotiki.callbacks.bookingData.BookingCancelCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface BookingCancelInterface {
    void onSuccessCancelBooking(BookingCancelCallback data);
    void onErrorCancelBooking(APIErrorCallback data);
}
