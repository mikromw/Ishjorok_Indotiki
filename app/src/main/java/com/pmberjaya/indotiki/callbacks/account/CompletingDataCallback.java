package com.pmberjaya.indotiki.callbacks.account;

import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.models.account.CompletingData;

import java.util.ArrayList;

/**
 * Created by willy on 5/24/2017.
 */

public class CompletingDataCallback extends BaseCallback {
    private CompletingData data;
    private ArrayList<String> error;

    public CompletingData getData() {
        return data;
    }

    public void setData(CompletingData data) {
        this.data = data;
    }

    public ArrayList<String> getError_form() {
        return error;
    }

    public void setError_form(ArrayList<String> error_form) {
        this.error = error_form;
    }
}
