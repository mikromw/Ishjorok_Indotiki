package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.callbacks.account.VerificationCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface VerificationSmsInterface {
    void onSuccessVerificationSms(VerificationCallback getUserDataCallback);
    void onErrorVerificationSms(APIErrorCallback apiErrorCallback);
}
