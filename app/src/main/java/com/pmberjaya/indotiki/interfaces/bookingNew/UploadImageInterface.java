package com.pmberjaya.indotiki.interfaces.bookingNew;

import com.pmberjaya.indotiki.callbacks.bookingNew.UploadPhotoCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 06/03/2017.
 */

public interface UploadImageInterface {
    void onSuccessUploadImage(UploadPhotoCallback data);
    void onErrorUploadImage(APIErrorCallback data);
}
