package com.pmberjaya.indotiki.callbacks.account;

/**
 * Created by edwin on 4/13/2016.
 */
public class UploadProfilePhotoCallback  {
    public String avatar;
    public String status;
    int sukses;
    String callback;


    public String getCallback(){
        return callback;
    }
    public void setCallback(String callback){
        this.callback  = callback;
    }


    public int getSukses(){
        return sukses;
    }
    public void setSukses(int sukses){
        this.sukses  = sukses;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
