package com.pmberjaya.indotiki.callbacks.account;

import com.pmberjaya.indotiki.base.BaseCallback;

/**
 * Created by edwin on 2/27/2016.
 */
public class RegisterFirebaseCallback extends BaseCallback {
    String user_id;
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
