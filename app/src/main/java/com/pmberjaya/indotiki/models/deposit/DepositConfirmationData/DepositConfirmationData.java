package com.pmberjaya.indotiki.models.deposit.DepositConfirmationData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwin on 4/7/2016.
 */
public class DepositConfirmationData implements Parcelable{

    public String id;
    public String deposit_order_code;
    public String user_id;
    public String status;
    public String confirm_time;
    public String bank;
    public String bank_account_name;
    public String bank_account_number;
    public String amount;
    public String info;
    public String deposit_kode_unik;
    public String receipt_send_time;
    public String receipt_image;

    public DepositConfirmationData(Parcel in) {
        id = in.readString();
        deposit_order_code = in.readString();
        user_id = in.readString();
        status = in.readString();
        confirm_time = in.readString();
        bank = in.readString();
        bank_account_name = in.readString();
        bank_account_number = in.readString();
        amount = in.readString();
        info = in.readString();
        deposit_kode_unik = in.readString();
        receipt_send_time = in.readString();
        receipt_image = in.readString();
    }

    public static final Creator<DepositConfirmationData> CREATOR = new Creator<DepositConfirmationData>() {
        @Override
        public DepositConfirmationData createFromParcel(Parcel in) {
            return new DepositConfirmationData(in);
        }

        @Override
        public DepositConfirmationData[] newArray(int size) {
            return new DepositConfirmationData[size];
        }
    };

    public DepositConfirmationData() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(deposit_order_code);
        dest.writeString(user_id);
        dest.writeString(status);
        dest.writeString(confirm_time);
        dest.writeString(bank);
        dest.writeString(bank_account_name);
        dest.writeString(bank_account_number);
        dest.writeString(amount);
        dest.writeString(info);
        dest.writeString(deposit_kode_unik);
        dest.writeString(receipt_send_time);
        dest.writeString(receipt_image);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeposit_order_code() {
        return deposit_order_code;
    }

    public void setDeposit_order_code(String deposit_order_code) {
        this.deposit_order_code = deposit_order_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfirm_time() {
        return confirm_time;
    }

    public void setConfirm_time(String confirm_time) {
        this.confirm_time = confirm_time;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBank_account_name() {
        return bank_account_name;
    }

    public void setBank_account_name(String bank_account_name) {
        this.bank_account_name = bank_account_name;
    }

    public String getBank_account_number() {
        return bank_account_number;
    }

    public void setBank_account_number(String bank_account_number) {
        this.bank_account_number = bank_account_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDeposit_kode_unik() {
        return deposit_kode_unik;
    }

    public void setDeposit_kode_unik(String deposit_kode_unik) {
        this.deposit_kode_unik = deposit_kode_unik;
    }

    public String getReceipt_send_time() {
        return receipt_send_time;
    }

    public void setReceipt_send_time(String receipt_send_time) {
        this.receipt_send_time = receipt_send_time;
    }

    public String getReceipt_image() {
        return receipt_image;
    }

    public void setReceipt_image(String receipt_image) {
        this.receipt_image = receipt_image;
    }
}
