package com.pmberjaya.indotiki.interfaces.bookingNew;

import com.pmberjaya.indotiki.callbacks.bookingNew.RequestFoodCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by gilbert on 16/08/17.
 */

public interface RequestFoodInterface {

    void onSuccesRequestFood(RequestFoodCallback requestFoodCallback);
    void onErrorRequestFood(APIErrorCallback apiErrorCallback);
}
