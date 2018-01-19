package com.pmberjaya.indotiki.base;

import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface BaseInterface {
    void onSuccess(BaseCallback baseCallback);
    void onError(APIErrorCallback apiErrorCallback);
}
