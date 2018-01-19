package com.pmberjaya.indotiki.interfaces.bookingNew;

import com.pmberjaya.indotiki.callbacks.bookingNew.RequestTransportCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by gilbert on 16/08/17.
 */

public interface RequestTransportInterface {
    void onSuccessRequestTransportCallback(RequestTransportCallback requestTransportCallback);
    void onErrorRequestTransportCallback(APIErrorCallback apiErrorCallback);
}
