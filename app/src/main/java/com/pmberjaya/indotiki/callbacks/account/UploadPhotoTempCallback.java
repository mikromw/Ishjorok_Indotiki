package com.pmberjaya.indotiki.callbacks.account;

import com.pmberjaya.indotiki.base.BaseCallback;

/**
 * Created by willy on 5/30/2017.
 */

public class UploadPhotoTempCallback extends BaseCallback {
    private String avatar;
    private String status;
    private String avatar_path;

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

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }
}
