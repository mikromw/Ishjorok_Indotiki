package com.pmberjaya.indotiki.models.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwin on 29/07/2017.
 */

public class PromoCodeDataParcelable implements Parcelable{
    public String title;
    public String description;
    public String cover;

    public PromoCodeDataParcelable(){

    }
    protected PromoCodeDataParcelable(Parcel in) {
        title = in.readString();
        description = in.readString();
        cover = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(cover);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PromoCodeDataParcelable> CREATOR = new Creator<PromoCodeDataParcelable>() {
        @Override
        public PromoCodeDataParcelable createFromParcel(Parcel in) {
            return new PromoCodeDataParcelable(in);
        }

        @Override
        public PromoCodeDataParcelable[] newArray(int size) {
            return new PromoCodeDataParcelable[size];
        }
    };
}
