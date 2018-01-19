package com.pmberjaya.indotiki.models.bookingData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwin on 15/08/2017.
 */

public class ChangePriceData implements Parcelable {
    public String oldPrice;
    public String newPrice;
    public String cashPaid;
    public String depositPaid;
    public String receiptImagePath;
    public String requestId;
    public String requestType;

    public ChangePriceData(){}
    protected ChangePriceData(Parcel in) {
        oldPrice = in.readString();
        newPrice = in.readString();
        cashPaid = in.readString();
        depositPaid = in.readString();
        receiptImagePath = in.readString();
        requestId = in.readString();
        requestType = in.readString();
    }

    public static final Creator<ChangePriceData> CREATOR = new Creator<ChangePriceData>() {
        @Override
        public ChangePriceData createFromParcel(Parcel in) {
            return new ChangePriceData(in);
        }

        @Override
        public ChangePriceData[] newArray(int size) {
            return new ChangePriceData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oldPrice);
        dest.writeString(newPrice);
        dest.writeString(cashPaid);
        dest.writeString(depositPaid);
        dest.writeString(receiptImagePath);
        dest.writeString(requestId);
        dest.writeString(requestType);
    }
}