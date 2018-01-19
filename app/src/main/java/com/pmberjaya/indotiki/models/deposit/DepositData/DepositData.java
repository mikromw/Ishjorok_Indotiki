package com.pmberjaya.indotiki.models.deposit.DepositData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepositData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("admin_id")
    @Expose
    private String adminId;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("request_time")
    @Expose
    private String requestTime;
    @SerializedName("category_request")
    @Expose
    private String categoryRequest;
    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("balance")
    @Expose
    private String balance;

    @SerializedName("bank_name")
    @Expose
    private String bank_name;

    @SerializedName("bank_account_number")
    @Expose
    private String bank_account_number;

    private String promo;
    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     * The amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     *
     * @return
     * The time
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time
     * The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     *
     * @return
     * The driverId
     */
    public String getDriverId() {
        return driverId;
    }

    /**
     *
     * @param driverId
     * The driver_id
     */
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    /**
     *
     * @return
     * The adminId
     */
    public String getAdminId() {
        return adminId;
    }

    /**
     *
     * @param adminId
     * The admin_id
     */
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    /**
     *
     * @return
     * The requestId
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     *
     * @param requestId
     * The request_id
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     *
     * @return
     * The requestTime
     */
    public String getRequestTime() {
        return requestTime;
    }

    /**
     *
     * @param requestTime
     * The request_time
     */
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    /**
     *
     * @return
     * The categoryRequest
     */
    public String getCategoryRequest() {
        return categoryRequest;
    }

    /**
     *
     * @param categoryRequest
     * The category_request
     */
    public void setCategoryRequest(String categoryRequest) {
        this.categoryRequest = categoryRequest;
    }

    /**
     *
     * @return
     * The action
     */
    public String getAction() {
        return action;
    }

    /**
     *
     * @param action
     * The action
     */
    public void setAction(String action) {
        this.action = action;
    }
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name= bank_name;
    }

    public String getBank_account_number() {
        return bank_account_number;
    }

    public void setBank_account_number(String bank_account_number) {
        this.bank_account_number = bank_account_number;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }
}