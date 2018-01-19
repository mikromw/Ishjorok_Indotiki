package com.pmberjaya.indotiki.models.main;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by edwin on 06/01/2017.
 */

public class MainMenuItemData implements Parcelable {
    private String display_menu;
    private String menu_id;
    private String status;
    private String device;
    private ArrayList<SubMenuData> submenu = null;

    public MainMenuItemData(String menu, String s) {

    }

    public MainMenuItemData() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MainMenuItemData(Parcel in) {
        display_menu = in.readString();
        menu_id = in.readString();
        status = in.readString();
        device = in.readString();
        submenu = in.createTypedArrayList(SubMenuData.CREATOR);
    }

    public static final Parcelable.Creator<MainMenuItemData> CREATOR = new Parcelable.Creator<MainMenuItemData>() {
        @Override
        public MainMenuItemData createFromParcel(Parcel in) {
            return new MainMenuItemData(in);
        }

        @Override
        public MainMenuItemData[] newArray(int size) {
            return new MainMenuItemData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(display_menu);
        parcel.writeString(menu_id);
        parcel.writeString(status);
        parcel.writeString(device);
        parcel.writeTypedList(submenu);
    }

    public String getDisplay_menu() {
        return display_menu;
    }

    public void setDisplay_menu(String display_menu) {
        this.display_menu = display_menu;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
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

    public ArrayList<SubMenuData> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(ArrayList<SubMenuData> submenu) {
        this.submenu = submenu;
    }
}
