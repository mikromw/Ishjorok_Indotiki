package com.pmberjaya.indotiki.base;

import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by Gilbert on 07/07/2017.
 */

public interface BaseGenericInterface {

    <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback);
    void onError(APIErrorCallback apiErrorCallback);
}
