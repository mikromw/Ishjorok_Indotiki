package com.pmberjaya.indotiki.models.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwin on 11/10/2017.
 */

public class EventNewData implements Parcelable{

    public String id;
    public String topic;
    public String avatar;
    public String news;
    public String status;
    public String date;
    public String by;
    public String district;
    public String userlevel;
    public String news_category_id;

    protected EventNewData(Parcel in) {
        id = in.readString();
        topic = in.readString();
        avatar = in.readString();
        news = in.readString();
        status = in.readString();
        date = in.readString();
        by = in.readString();
        district = in.readString();
        userlevel = in.readString();
        news_category_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(topic);
        dest.writeString(avatar);
        dest.writeString(news);
        dest.writeString(status);
        dest.writeString(date);
        dest.writeString(by);
        dest.writeString(district);
        dest.writeString(userlevel);
        dest.writeString(news_category_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventNewData> CREATOR = new Creator<EventNewData>() {
        @Override
        public EventNewData createFromParcel(Parcel in) {
            return new EventNewData(in);
        }

        @Override
        public EventNewData[] newArray(int size) {
            return new EventNewData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUserlevel() {
        return userlevel;
    }

    public void setUserlevel(String userlevel) {
        this.userlevel = userlevel;
    }

    public String getNews_category_id() {
        return news_category_id;
    }

    public void setNews_category_id(String news_category_id) {
        this.news_category_id = news_category_id;
    }
}
