package com.pmberjaya.indotiki.models.bookingNew;

import android.opengl.Visibility;
import android.view.View;

/**
 * Created by edwin on 22/05/2017.
 */

public class PaymentData {
    String paymentName;
    String paymentId;

    public PaymentData(String paymentId, String paymentName){
        this.paymentId = paymentId;
        this.paymentName = paymentName;
    }
    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
