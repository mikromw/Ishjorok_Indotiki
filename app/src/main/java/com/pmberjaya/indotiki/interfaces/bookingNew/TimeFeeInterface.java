package com.pmberjaya.indotiki.interfaces.bookingNew;

import com.pmberjaya.indotiki.callbacks.bookingNew.TimeFeeCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

import retrofit2.Call;

/**
 * Created by edwin on 04/08/2017.
 */

public interface TimeFeeInterface {
    void onSuccessGetTimeFee(TimeFeeCallback timeFeeCallback);
    void onErrorGetTimefee(APIErrorCallback apiErrorCallback);
    void callCancel(Call<TimeFeeCallback> timeFeeCallbackCall);
}
