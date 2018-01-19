package com.pmberjaya.indotiki.models.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwin on 3/11/2016.
 */
public class AppVersionData implements Parcelable {
    int app_version_code;
    String app_version_name;
    String changelog;
    public String getApp_version_name() {
        return app_version_name;
    }

    public void setApp_version_name(String app_version_name) {
        this.app_version_name = app_version_name;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public int getApp_version_code() {
        return app_version_code;
    }

    public void setApp_version_code(int app_version_code) {
        this.app_version_code = app_version_code;
    }

    protected AppVersionData(Parcel in) {
        this.app_version_code = in.readInt();
        this.app_version_name = in.readString();
        this.changelog = in.readString();

    }

    public static final Parcelable.Creator<AppVersionData> CREATOR = new Parcelable.Creator<AppVersionData>() {
        @Override
        public AppVersionData createFromParcel(Parcel in) {
            return new AppVersionData(in);
        }

        @Override
        public AppVersionData[] newArray(int size) {
            return new AppVersionData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(app_version_code);
        dest.writeString(app_version_name);
        dest.writeString(changelog);
    }
}
