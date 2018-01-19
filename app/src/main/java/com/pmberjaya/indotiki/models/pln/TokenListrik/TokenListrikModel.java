package com.pmberjaya.indotiki.models.pln.TokenListrik;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gilbert on 12/4/2017.
 */

public class TokenListrikModel {

    private String responseCode;
    private String message;
    private List<String> powerPurchaseDenom = null;
    private String powerPurchaseUnsold;
    private String productCode;
    private String refID;
    private CustomerData customer_data;

    public String getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<String> getPowerPurchaseDenom() {
        return powerPurchaseDenom;
    }
    public void setPowerPurchaseDenom(List<String> powerPurchaseDenom) {
        this.powerPurchaseDenom = powerPurchaseDenom;
    }
    public String getPowerPurchaseUnsold() {
        return powerPurchaseUnsold;
    }
    public void setPowerPurchaseUnsold(String powerPurchaseUnsold) {
        this.powerPurchaseUnsold = powerPurchaseUnsold;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getRefID() {
        return refID;
    }
    public void setRefID(String refID) {
        this.refID = refID;
    }
    public CustomerData getCustomer_data() {
        return customer_data;
    }
    public void setCustomer_data(CustomerData customer_data) {
        this.customer_data = customer_data;
    }
}
