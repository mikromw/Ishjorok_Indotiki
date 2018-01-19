package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.callbacks.account.RegisterFirebaseCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface RegisterFirebaseInterface {
    void onSuccessRegisterFirebase(RegisterFirebaseCallback registerGcmCallback);
    void onErrorRegisterFirebase(APIErrorCallback apiErrorCallback);
}
