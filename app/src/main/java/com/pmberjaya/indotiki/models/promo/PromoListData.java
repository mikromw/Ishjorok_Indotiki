package com.pmberjaya.indotiki.models.promo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by edwin on 27/07/2017.
 */

public class PromoListData implements Parcelable{
    public String id;
    public String code_promo;
    public String start_promo;
    public String end_promo;
    public String type;
    public String status;
    public String payment_method;
    public String request_type;
    public String limit_per_day;
    public String limit_total;
    public String limit_total_one_user;
    public String category_voucher;
    public String open_time;
    public String close_time;
    public String auto_input;
    public String userlevel;
    public String type_voucher;
    public String restrict_imei;
    public String promo_relation_id;
    public String description;
    public String cover;
    public String user_today_quota_left;
    public String promo_quota_left;
    public String user_overall_quota_left;
    public List<TermKmData> terms_km = null;
    public String caption_type;


    protected PromoListData(Parcel in) {
        id = in.readString();
        code_promo = in.readString();
        start_promo = in.readString();
        end_promo = in.readString();
        type = in.readString();
        status = in.readString();
        payment_method = in.readString();
        request_type = in.readString();
        limit_per_day = in.readString();
        limit_total = in.readString();
        limit_total_one_user = in.readString();
        category_voucher = in.readString();
        open_time = in.readString();
        close_time = in.readString();
        auto_input = in.readString();
        userlevel = in.readString();
        type_voucher = in.readString();
        restrict_imei = in.readString();
        promo_relation_id = in.readString();
        description = in.readString();
        cover = in.readString();
        user_today_quota_left = in.readString();
        promo_quota_left = in.readString();
        user_overall_quota_left = in.readString();
        caption_type = in.readString();
//        terms_km = new ArrayList<TermKmData>();
//        in.readList(terms_km,BusinessHourData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(code_promo);
        dest.writeString(start_promo);
        dest.writeString(end_promo);
        dest.writeString(type);
        dest.writeString(status);
        dest.writeString(payment_method);
        dest.writeString(request_type);
        dest.writeString(limit_per_day);
        dest.writeString(limit_total);
        dest.writeString(limit_total_one_user);
        dest.writeString(category_voucher);
        dest.writeString(open_time);
        dest.writeString(close_time);
        dest.writeString(auto_input);
        dest.writeString(userlevel);
        dest.writeString(type_voucher);
        dest.writeString(restrict_imei);
        dest.writeString(promo_relation_id);
        dest.writeString(description);
        dest.writeString(cover);
        dest.writeString(user_today_quota_left);
        dest.writeString(promo_quota_left);
        dest.writeString(user_overall_quota_left);
        dest.writeString(caption_type);
//        dest.writeList(this.terms_km);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PromoListData> CREATOR = new Creator<PromoListData>() {
        @Override
        public PromoListData createFromParcel(Parcel in) {
            return new PromoListData(in);
        }

        @Override
        public PromoListData[] newArray(int size) {
            return new PromoListData[size];
        }
    };
}
