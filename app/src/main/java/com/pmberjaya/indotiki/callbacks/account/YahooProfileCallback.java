package com.pmberjaya.indotiki.callbacks.account;

import com.pmberjaya.indotiki.models.account.YahooProfile.Profile;

/**
 * Created by edwin on 09/08/2016.
 */
public class YahooProfileCallback {
    String callback;
    Profile profile;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
