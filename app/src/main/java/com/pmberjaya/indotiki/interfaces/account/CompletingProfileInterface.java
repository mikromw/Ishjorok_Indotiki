package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.callbacks.account.CompletingDataCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface CompletingProfileInterface {
    void onSuccessCompletingData(CompletingDataCallback getUserDataCallback);
    void onErrorCompletingData(APIErrorCallback apiErrorCallback);
}
