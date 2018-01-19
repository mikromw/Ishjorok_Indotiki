package com.pmberjaya.indotiki.models.account.YahooProfile;

import java.util.List;

/**
 * Created by edwin on 08/08/2016.
 */
public class Profile {
    String guid;
    String givenName;
    String familyName;
    Image image;
    List<Email> emails;
    List<Phone> phones;
    String profileStatus;
    String profileMode;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Image getImageName() {
        return image;
    }

    public void setImageName(Image image) {
        this.image = image;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getProfileMode() {
        return profileMode;
    }

    public void setProfileMode(String profileMode) {
        this.profileMode = profileMode;
    }
}
