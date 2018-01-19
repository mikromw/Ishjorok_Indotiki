package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.callbacks.main.KeyCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface APIKeyInterface {
    void onSuccessGetAPIKey(KeyCallback data);
    void onErrorGetAPIKey(APIErrorCallback data);
}
