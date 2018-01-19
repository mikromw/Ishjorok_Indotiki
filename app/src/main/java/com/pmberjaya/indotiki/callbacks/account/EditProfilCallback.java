package com.pmberjaya.indotiki.callbacks.account;

import com.pmberjaya.indotiki.base.BaseCallback;

import java.util.ArrayList;

/**
 * Created by edwin on 4/30/2016.
 */
public class EditProfilCallback extends BaseCallback {
    ArrayList<String> error;
    String data;

    public ArrayList<String>getError() {
        return error;
    }

    public void setError(ArrayList<String> error) {
        this.error = error;
    }

    public String getToken() {
        return data;
    }

    public void setToken(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
