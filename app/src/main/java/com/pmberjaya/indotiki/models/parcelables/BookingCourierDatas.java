package com.pmberjaya.indotiki.models.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwin on 09/11/2016.
 */

public class BookingCourierDatas implements Parcelable {
    public String location_detail_sender;
    public String location_detail_receiver;
    public String name_sender;
    public String phone_sender;
    public String name_receiver;
    public String phone_receiver;
    public String item;
    public String item_photo;
    public String item_photo_small;

    public BookingCourierDatas() {

    }

    protected BookingCourierDatas(Parcel in) {
        this.location_detail_sender = in.readString();
        this.location_detail_receiver = in.readString();
        this.name_sender = in.readString();
        this.phone_sender = in.readString();
        this.name_receiver = in.readString();
        this.phone_receiver = in.readString();
        this.item = in.readString();
        this.item_photo = in.readString();
        this.item_photo_small = in.readString();
    }

    public static final Parcelable.Creator<BookingCourierDatas> CREATOR = new Parcelable.Creator<BookingCourierDatas>() {
        @Override
        public BookingCourierDatas createFromParcel(Parcel in) {
            return new BookingCourierDatas(in);
        }

        @Override
        public BookingCourierDatas[] newArray(int size) {
            return new BookingCourierDatas[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location_detail_sender);
        dest.writeString(location_detail_receiver);
        dest.writeString(name_sender);
        dest.writeString(phone_sender);
        dest.writeString(name_receiver);
        dest.writeString(phone_receiver);
        dest.writeString(item);
        dest.writeString(item_photo);
        dest.writeString(item_photo_small);
    }
}