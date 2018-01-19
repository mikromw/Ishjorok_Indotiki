package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.models.account.UserModel;

/**
 * Created by Gilbert on 06/07/2017.
 */

public interface UserDataInterface {
    void onSuccesGetUserData(BaseGenericCallback<UserModel> getUserDataCallback);
    void onErrorGetUserData(APIErrorCallback apiErrorCallback);
}
