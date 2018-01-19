package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.callbacks.account.UploadProfilePhotoCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 12/09/2017.
 */

public interface UploadImageProfileInterface {
    void onSuccessUploadImageProfile(UploadProfilePhotoCallback data);
    void onErrorUploadImageProfile(APIErrorCallback data);
}
