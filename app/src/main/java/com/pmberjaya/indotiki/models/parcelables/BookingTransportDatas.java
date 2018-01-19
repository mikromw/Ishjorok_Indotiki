package com.pmberjaya.indotiki.models.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwin on 09/11/2016.
 */

public class BookingTransportDatas implements Parcelable{
    public String locationDetail;


    public BookingTransportDatas() {

    }
    protected BookingTransportDatas(Parcel in) {
        this.locationDetail = in.readString();
    }

    public static final Parcelable.Creator<BookingTransportDatas> CREATOR = new Parcelable.Creator<BookingTransportDatas>() {
        @Override
        public BookingTransportDatas createFromParcel(Parcel in) {
            return new BookingTransportDatas(in);
        }

        @Override
        public BookingTransportDatas[] newArray(int size) {
            return new BookingTransportDatas[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationDetail);
    }

    public String getLocation_detail() {
        return locationDetail;
    }

    public void setLocation_detail(String location_detail) {
        this.locationDetail = location_detail;
    }

}
