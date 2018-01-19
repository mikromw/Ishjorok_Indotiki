package com.pmberjaya.indotiki.models.account;

/**
 * Created by Gilbert on 01/07/2017.
 */

public class EmailData {

    String email;
    String name;

    public EmailData(String email, String name){
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
