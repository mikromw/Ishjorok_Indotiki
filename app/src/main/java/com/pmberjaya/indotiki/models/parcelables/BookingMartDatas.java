package com.pmberjaya.indotiki.models.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pmberjaya.indotiki.models.mart.MartData;
import com.pmberjaya.indotiki.models.mart.MartItemTempData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilbert on 25/11/17.
 */

public class BookingMartDatas implements Parcelable {

    public String location_detail;
    public int itemCost = 0;
    public int itemsQuantityTotal = 0;
    public double latDestination;
    public double lngDestination;
    public MartData martData;
    public List<MartItemTempData> martItemTempDatas;
    public List<MartItemTempData> martItemTempManuallyDatas;
    public String to_place;
    public String to_detail;
    public String lat_to;
    public String lng_to;
    public String total_item_price_before;
    public String item_receipt;
    public String alreadyselected_mart_id;



    public BookingMartDatas() {
        this.martItemTempDatas = new ArrayList<MartItemTempData>();
    }
    protected BookingMartDatas(Parcel in) {
        this.martData = (MartData) in.readParcelable(MartData.class.getClassLoader());
        this.location_detail = in.readString();
        this.itemCost = in.readInt();
        this.itemsQuantityTotal = in.readInt();
        this.latDestination = in.readDouble();
        this.lngDestination = in.readDouble();
        martItemTempDatas = new ArrayList<MartItemTempData>();
        in.readList(martItemTempDatas,MartItemTempData.class.getClassLoader());
        martItemTempManuallyDatas = new ArrayList<MartItemTempData>();
        in.readList(martItemTempManuallyDatas,MartItemTempData.class.getClassLoader());
        this.to_place = in.readString();
        this.to_detail = in.readString();
        this.lat_to = in.readString();
        this.lng_to = in.readString();
        this.total_item_price_before = in.readString();
        this.item_receipt = in.readString();
        this.alreadyselected_mart_id = in.readString();
    }

    public static final Parcelable.Creator<BookingMartDatas> CREATOR = new Parcelable.Creator<BookingMartDatas>() {
        @Override
        public BookingMartDatas createFromParcel(Parcel in) {
            return new BookingMartDatas(in);
        }
        @Override
        public BookingMartDatas[] newArray(int size) {
            return new BookingMartDatas[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(martData, flags);
        dest.writeString(location_detail);
        dest.writeInt(itemCost);
        dest.writeInt(itemsQuantityTotal);
        dest.writeDouble(latDestination);
        dest.writeDouble(lngDestination);
        dest.writeList(martItemTempDatas);
        dest.writeList(martItemTempManuallyDatas);
        dest.writeString(to_place);
        dest.writeString(to_detail);
        dest.writeString(lat_to);
        dest.writeString(lng_to);
        dest.writeString(total_item_price_before);
        dest.writeString(item_receipt);
        dest.writeString(alreadyselected_mart_id);
    }
}
