package com.pmberjaya.indotiki.callbacks.bookingNew;

/**
 * Created by edwin on 4/13/2016.
 */
public class UploadPhotoCallback  {
    public String photo;
    public String path;
    public String status;
    private String avatar;
    private String file;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
