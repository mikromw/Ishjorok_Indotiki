package com.pmberjaya.indotiki.models.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gilbert on 24/11/17.
 */

public class SubMenuData implements Parcelable{

    private String submenu_id;
    private String status;
    private String device;
    private String display_submenu;

    public SubMenuData() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public SubMenuData(Parcel in) {
        submenu_id = in.readString();
        status = in.readString();
        device = in.readString();
        display_submenu = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(submenu_id);
        parcel.writeString(status);
        parcel.writeString(device);
        parcel.writeString(display_submenu);
    }

    public static final Parcelable.Creator<SubMenuData> CREATOR = new Parcelable.Creator<SubMenuData>() {
        @Override
        public SubMenuData createFromParcel(Parcel in) {
            return new SubMenuData(in);
        }

        @Override
        public SubMenuData[] newArray(int size) {
            return new SubMenuData[size];
        }
    };

    public String getSubmenu_id() {
        return submenu_id;
    }

    public void setSubmenu_id(String submenu_id) {
        this.submenu_id = submenu_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDisplay_submenu() {
        return display_submenu;
    }

    public void setDisplay_submenu(String display_submenu) {
        this.display_submenu = display_submenu;
    }
}
