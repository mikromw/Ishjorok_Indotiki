package com.pmberjaya.indotiki.callbacks.bookingData;

import com.pmberjaya.indotiki.base.BaseCallback;

/**
 * Created by edwin on 4/27/2016.
 */
public class BookingCancelCallback extends BaseCallback {
    public String data;
    public boolean suspend;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean getSuspend() {
        return suspend;
    }

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }
}
