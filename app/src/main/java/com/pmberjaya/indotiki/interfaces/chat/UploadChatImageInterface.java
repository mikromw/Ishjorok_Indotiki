package com.pmberjaya.indotiki.interfaces.chat;

import com.pmberjaya.indotiki.callbacks.chat.UploadChatImageCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

/**
 * Created by edwin on 11/09/2017.
 */

public interface UploadChatImageInterface {
    void onSuccessUploadImage(UploadChatImageCallback data);
    void onErrorUploadImage(APIErrorCallback data);
}
