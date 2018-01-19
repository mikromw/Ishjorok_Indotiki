package com.pmberjaya.indotiki.callbacks.deposit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.models.deposit.DepositData.DepositLogModel;

public class DepositCallback extends BaseCallback {

    public String deposit;
    private int debit;
    private int credit;
    @SerializedName("data")
    @Expose
    private DepositLogModel data;

    public void setDeposit(String deposit){
        this.deposit= deposit;
    }

    public String getDeposit(){
        return deposit;
    }
    public int getCredit() {
        return credit;
    }


    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }


    public DepositLogModel getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(DepositLogModel data) {
        this.data = data;
    }

}