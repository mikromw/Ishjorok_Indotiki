package com.pmberjaya.indotiki.callbacks.deposit;

import com.pmberjaya.indotiki.base.BaseCallback;

/**
 * Created by Gilbert on 20/04/2017.
 */

public class DepositNewConfirmationCallback extends BaseCallback {
    private String data;
    private String kode_unik;
    private int request_id;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKode_unik() {
        return kode_unik;
    }

    public void setKode_unik(String kode_unik) {
        this.kode_unik = kode_unik;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }
}
