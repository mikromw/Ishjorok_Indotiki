package com.pmberjaya.indotiki.models.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gilbert on 4/15/2017.
 */

public class DepositDataParcelable implements Parcelable {
    private String nominal_deposit;
    private String kode_unik;
    private int request_id;
    private String bank_type;
    private String bank_account_number;
    private String transaction_time;


    public DepositDataParcelable() {

    }


    public DepositDataParcelable(Parcel in) {
        this.nominal_deposit = in.readString();
        this.kode_unik = in.readString();
        this.request_id = in.readInt();
        this.bank_type = in.readString();
        this.bank_account_number = in.readString();
        this.transaction_time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nominal_deposit);
        dest.writeString(kode_unik);
        dest.writeInt(request_id);
        dest.writeString(bank_type);
        dest.writeString(bank_account_number);
        dest.writeString(transaction_time);
    }

    public static final Creator<DepositDataParcelable> CREATOR = new Creator<DepositDataParcelable>() {
        @Override
        public DepositDataParcelable createFromParcel(Parcel in) {
            return new DepositDataParcelable(in);
        }

        @Override
        public DepositDataParcelable[] newArray(int size) {
            return new DepositDataParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getNominal_deposit() {
        return nominal_deposit;
    }

    public void setNominal_deposit(String nominal_deposit) {
        this.nominal_deposit = nominal_deposit;
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

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getBank_account_number() {
        return bank_account_number;
    }

    public void setBank_account_number(String bank_account_number) {
        this.bank_account_number = bank_account_number;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }
}
