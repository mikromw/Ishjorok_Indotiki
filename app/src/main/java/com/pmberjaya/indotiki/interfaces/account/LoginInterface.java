package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.callbacks.account.LoginCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 05/04/2017.
 */

public interface LoginInterface {
    void onSuccessLogin(LoginCallback loginCallback);
    void onErrorLogin(APIErrorCallback apiErrorCallback);
}
