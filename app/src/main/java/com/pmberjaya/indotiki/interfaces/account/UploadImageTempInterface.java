package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.callbacks.account.UploadPhotoTempCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface UploadImageTempInterface {
    void onSuccessUploadImageTemp(UploadPhotoTempCallback data);
    void onErrorUploadImageTemp(APIErrorCallback data);
}
