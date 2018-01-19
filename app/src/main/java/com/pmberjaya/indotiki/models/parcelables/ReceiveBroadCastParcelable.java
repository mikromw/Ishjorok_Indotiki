package com.pmberjaya.indotiki.models.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pmberjaya.indotiki.models.bookingData.ChangePriceData;

/**
 * Created by edwin on 15/08/2017.
 */

public class ReceiveBroadCastParcelable implements Parcelable{
    public String itemType;
    public ChangePriceData changePriceData;

    public ReceiveBroadCastParcelable(){}
    protected ReceiveBroadCastParcelable(Parcel in) {
        itemType = in.readString();
        changePriceData = in.readParcelable(ChangePriceData.class.getClassLoader());
    }

    public static final Creator<ReceiveBroadCastParcelable> CREATOR = new Creator<ReceiveBroadCastParcelable>() {
        @Override
        public ReceiveBroadCastParcelable createFromParcel(Parcel in) {
            return new ReceiveBroadCastParcelable(in);
        }

        @Override
        public ReceiveBroadCastParcelable[] newArray(int size) {
            return new ReceiveBroadCastParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemType);
        dest.writeParcelable(changePriceData, flags);
    }


}
