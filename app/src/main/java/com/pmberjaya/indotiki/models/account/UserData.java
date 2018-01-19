package com.pmberjaya.indotiki.models.account;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwin on 07/12/2017.
 */

public class UserData implements Parcelable {
    public String user_id;
    public String fullname;
    public String email;
    public String avatar;
    public String phone;
    public String deposit;
    public String loginType;
    public String socialId;
    public String hide_phone;
    public String account_for;

    public UserData(String user_id, String fullname, String email, String avatar, String phone, String deposit, String loginType, String socialId, String hide_phone) {
        this.user_id =user_id;
        this.fullname = fullname;
        this.email = email;
        this.avatar = avatar;
        this.phone = phone;
        this.deposit = deposit;
        this.loginType= loginType;
        this.socialId = socialId;
        this.hide_phone = hide_phone;
    }

    public UserData(){}

    protected UserData(Parcel in) {
        this.user_id = in.readString();
        this.fullname = in.readString();
        this.email = in.readString();
        this.avatar = in.readString();
        this.phone = in.readString();
        this.deposit = in.readString();
        this.loginType = in.readString();
        this.socialId = in.readString();
        this.hide_phone = in.readString();
        this.account_for = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(fullname);
        dest.writeString(email);
        dest.writeString(avatar);
        dest.writeString(phone);
        dest.writeString(deposit);
        dest.writeString(loginType);
        dest.writeString(socialId);
        dest.writeString(hide_phone);
        dest.writeString(account_for);
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHide_phone() {
        return hide_phone;
    }

    public void setHide_phone(String hide_phone) {
        this.hide_phone = hide_phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHide_number() {
        return hide_phone;
    }

    public void setHide_number(String hide_phone) {
        this.hide_phone = hide_phone;
    }

    public String getAccount_for() {
        return account_for;
    }

    public void setAccount_for(String account_for) {
        this.account_for = account_for;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit =deposit;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }
}
