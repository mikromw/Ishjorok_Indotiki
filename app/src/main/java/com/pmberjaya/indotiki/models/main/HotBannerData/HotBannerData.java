package com.pmberjaya.indotiki.models.main.HotBannerData;

/**
 * Created by edwin on 08/04/2017.
 */

public class HotBannerData {
    public String id;
    public String title;
    public String description;
    public String start_time;
    public String end_time;
    public String avatar_app;
    public String avatar;
    public String cover_member;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAvatar_app() {
        return avatar_app;
    }

    public void setAvatar_app(String avatar_app) {
        this.avatar_app = avatar_app;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover_member() {
        return cover_member;
    }

    public void setCover_member(String cover_member) {
        this.cover_member = cover_member;
    }
}
