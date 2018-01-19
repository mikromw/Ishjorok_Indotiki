package com.pmberjaya.indotiki.interfaces.account;

import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.callbacks.account.EditProfilCallback;

/**
 * Created by edwin on 03/10/2017.
 */

public interface EditProfileInterface {

    void onSuccessEditProfile(EditProfilCallback editProfilCallback);
    void onErrorEditProfile(APIErrorCallback apiErrorCallback);
}
