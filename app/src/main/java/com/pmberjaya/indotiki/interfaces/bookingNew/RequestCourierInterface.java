package com.pmberjaya.indotiki.interfaces.bookingNew;

import com.pmberjaya.indotiki.callbacks.bookingNew.RequestCourierCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by Gilbert on 02/08/2017.
 */

public interface RequestCourierInterface {

    void onSuccessRequestCourier(RequestCourierCallback requestCourierCallback);

    void onErrorRequestCourier(APIErrorCallback apiErrorCallback);
}
