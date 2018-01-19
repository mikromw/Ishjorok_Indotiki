package com.pmberjaya.indotiki.models.account;

public class VerificationData {
	UserData user;
	String promo;

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public String getPromo() {
		return promo;
	}

	public void setPromo(String promo) {
		this.promo = promo;
	}
}
