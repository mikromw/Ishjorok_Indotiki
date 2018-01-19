package com.pmberjaya.indotiki.models.bookingData;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverData implements Parcelable{
    private String driver_fullname;
    private String driver_avatar;
    private String driver_phone;
    private String number_plate;
    private String transportation_name;
    private String driver_id;

    public DriverData(){

    }
    protected DriverData(Parcel in) {
        driver_fullname = in.readString();
        driver_avatar = in.readString();
        driver_phone = in.readString();
        number_plate = in.readString();
        transportation_name = in.readString();
        driver_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(driver_fullname);
        dest.writeString(driver_avatar);
        dest.writeString(driver_phone);
        dest.writeString(number_plate);
        dest.writeString(transportation_name);
        dest.writeString(driver_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DriverData> CREATOR = new Creator<DriverData>() {
        @Override
        public DriverData createFromParcel(Parcel in) {
            return new DriverData(in);
        }

        @Override
        public DriverData[] newArray(int size) {
            return new DriverData[size];
        }
    };

    public String getDriver_fullname() {
        return driver_fullname;
    }

    public void setDriver_fullname(String driver_fullname) {
        this.driver_fullname = driver_fullname;
    }

    public String getDriver_avatar() {
        return driver_avatar;
    }

    public void setDriver_avatar(String driver_avatar) {
        this.driver_avatar = driver_avatar;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
    }

    public String getTransportation_name() {
        return transportation_name;
    }

    public void setTransportation_name(String transportation_name) {
        this.transportation_name = transportation_name;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }
}

