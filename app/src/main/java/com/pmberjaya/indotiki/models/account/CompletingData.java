package com.pmberjaya.indotiki.models.account;

/**
 * Created by willy on 5/24/2017.
 */

public class CompletingData {
    private String email;
    private String user_id;
    private String login_id;
    private String phone;
    private String fullname;
    private String avatar;
    private String reset_password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReset_password() {
        return reset_password;
    }

    public void setReset_password(String reset_password) {
        this.reset_password = reset_password;
    }
}
