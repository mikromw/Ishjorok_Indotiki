package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.callbacks.account.YahooProfileCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface YahooProfileInterface {
    void onSuccessYahooProfile(YahooProfileCallback getUserDataCallback);
    void onErrorYahooProfile(APIErrorCallback apiErrorCallback);
}
