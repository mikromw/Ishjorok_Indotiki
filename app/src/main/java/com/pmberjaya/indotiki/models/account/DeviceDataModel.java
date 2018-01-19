package com.pmberjaya.indotiki.models.account;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwin on 08/11/2017.
 */

public class DeviceDataModel implements Parcelable {
    public String imei;
    public String deviceName;
    public String simCard;
    public String phoneNumber;
    public String os;

    public DeviceDataModel(){}
    protected DeviceDataModel(Parcel in) {
        imei = in.readString();
        deviceName = in.readString();
        simCard = in.readString();
        phoneNumber = in.readString();
        os = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imei);
        dest.writeString(deviceName);
        dest.writeString(simCard);
        dest.writeString(phoneNumber);
        dest.writeString(os);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeviceDataModel> CREATOR = new Creator<DeviceDataModel>() {
        @Override
        public DeviceDataModel createFromParcel(Parcel in) {
            return new DeviceDataModel(in);
        }

        @Override
        public DeviceDataModel[] newArray(int size) {
            return new DeviceDataModel[size];
        }
    };
}
